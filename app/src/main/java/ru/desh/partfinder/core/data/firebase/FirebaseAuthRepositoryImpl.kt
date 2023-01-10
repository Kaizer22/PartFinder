package ru.desh.partfinder.core.data.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import ru.desh.partfinder.core.domain.model.Account
import ru.desh.partfinder.core.domain.repository.AuthRepository
import ru.desh.partfinder.core.utils.DataOrErrorResult
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
): AuthRepository {

    private var currentUser: Account? = null

    override fun getCurrentAccount(): Account? {
        return currentUser?.copy()
    }

    override fun createAccountWithEmailAndPassword(email: String, password: String): LiveData<DataOrErrorResult<Boolean, Exception?>> {
        val dataOrException = MutableLiveData<DataOrErrorResult<Boolean, Exception?>>()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{ task ->
            val res = DataOrErrorResult<Boolean, Exception?>()
            if (task.isSuccessful) {
                currentUser = getUser(auth.currentUser)
                res.data = true
            } else {
                res.exception = task.exception
            }
            dataOrException.value = res
        }
        return dataOrException
    }

    override fun sendVerificationEmail(): LiveData<DataOrErrorResult<Boolean, Exception?>> {
        val user = auth.currentUser
        val dataOrException = MutableLiveData<DataOrErrorResult<Boolean, Exception?>>()
        user?.sendEmailVerification()?.addOnCompleteListener { task ->
            val res = DataOrErrorResult<Boolean, Exception?>()
            if (task.isSuccessful) {
                res.data = true
            } else {
                res.exception = task.exception
            }
        }
        return dataOrException
    }

    override fun sendPasswordResetEmail(email: String): LiveData<DataOrErrorResult<Boolean, Exception?>> {
        val dataOrException = MutableLiveData<DataOrErrorResult<Boolean, Exception?>>()
        auth.sendPasswordResetEmail(email).addOnCompleteListener { result ->
            val res = DataOrErrorResult<Boolean, Exception?>()
            if (result.isSuccessful) {
                res.data = true
            } else {
                res.exception = result.exception
            }
            dataOrException.value = res
        }
        return dataOrException
    }

    override fun createAccountWithGoogleAccount(): LiveData<DataOrErrorResult<Boolean, Exception?>> {
        TODO("Not yet implemented")
    }

    override fun createAccountWithPhone(phoneNumber: String): LiveData<DataOrErrorResult<Boolean, Exception?>> {
        TODO("Not yet implemented")
    }

    companion object {
        private const val OTP_TIMEOUT = 60L
    }
    private var verificationId = ""

    override fun sendVerificationCode(phoneNumber: String): LiveData<DataOrErrorResult<String, Exception?>> {
        val dataOrException = MutableLiveData<DataOrErrorResult<String, Exception?>>()
        val res = DataOrErrorResult<String, Exception?>()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            OTP_TIMEOUT,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            object : OnVerificationStateChangedCallbacks() {
                override fun onCodeSent(vId: String, fRT: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(vId, fRT)
                    verificationId = vId
                    Log.d("FIREBASE_AUTH_REPOSITORY", "Verification code sent to $phoneNumber")
                }
                override fun onVerificationCompleted(pAC: PhoneAuthCredential) {
                    val code = pAC.smsCode
                    if (code != null) {
                        res.data = code
                    } else {
                        res.exception = java.lang.IllegalStateException(
                            "Got empty code on verification completed"
                        )
                    }
                    dataOrException.value = res
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    res.exception = e
                    verificationId = ""
                    dataOrException.value = res
                }
            }
        )
        return dataOrException
    }

    override fun verifyCode(code: String): LiveData<DataOrErrorResult<Boolean, Exception?>> {
        val dataOrException = MutableLiveData<DataOrErrorResult<Boolean, Exception?>>()
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        auth.signInWithCredential(credential).addOnCompleteListener {  result ->
            val res = DataOrErrorResult<Boolean, Exception?>()
            if (result.isSuccessful) {
                currentUser = getUser(auth.currentUser)
                res.data = true
            } else {
                res.exception = result.exception
            }
            dataOrException.value = res
        }
        verificationId = ""
        return dataOrException
    }

    override fun signInWithGoogle(): LiveData<DataOrErrorResult<Account?, Exception?>> {
        TODO("Not yet implemented")
    }

    override fun signInWithEmailAndPassword(email: String, password: String): LiveData<DataOrErrorResult<Boolean, Exception?>> {
        val dataOrException = MutableLiveData<DataOrErrorResult<Boolean, Exception?>>()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            val res = DataOrErrorResult<Boolean, Exception?>()
            if (task.isSuccessful) {
                currentUser = getUser(auth.currentUser)
                res.data = true
            } else {
                res.exception = task.exception
            }
            dataOrException.value = res
        }
        return dataOrException
    }

    private fun getUser(currentUser: FirebaseUser?): Account? {
        return if (currentUser == null) null else {
            val phone = currentUser.phoneNumber ?: ""
            val email = currentUser.email ?: ""
            val regType = if (phone.isEmpty()) Account.RegistrationType.EMAIL
                else Account.RegistrationType.PHONE
            val name = currentUser.displayName ?: ""
            Account(
                currentUser.uid, regType,
                // TODO check phone verification
                currentUser.isEmailVerified,
                phone,
                email,
                name
            )
        }
    }

    override fun createAccountWithFacebook() {
        TODO("Not yet implemented")
    }

    override fun signInWithFacebook() {
        TODO("Not yet implemented")
    }

    override fun signOut() {
        auth.signOut()
    }
}