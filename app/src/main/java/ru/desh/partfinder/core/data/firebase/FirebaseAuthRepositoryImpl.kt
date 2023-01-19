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
import kotlinx.coroutines.tasks.await
import ru.desh.partfinder.core.domain.model.Account
import ru.desh.partfinder.core.domain.repository.AuthRepository
import ru.desh.partfinder.core.utils.DataOrErrorResult
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    private var currentUser: Account? = null

    override fun getCurrentAccount(): Account? {
        return currentUser?.copy()
    }

    override suspend fun createAccountWithEmailAndPassword(
        email: String,
        password: String
    ): DataOrErrorResult<Boolean, Exception?> {
        val res = DataOrErrorResult<Boolean, Exception?>()
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            currentUser = getUser(auth.currentUser)
            res.setData(true)
        } catch (e: Exception) {
            res.setException(e)
        }
        return res
    }

    override suspend fun sendVerificationEmail(): DataOrErrorResult<Boolean, Exception?> {
        val user = auth.currentUser
        val res = DataOrErrorResult<Boolean, Exception?>()
        try {
            user?.sendEmailVerification()?.await()
            res.setData(true)
        } catch (e: Exception) {
            res.setException(e)
        }
        return res
    }

    override suspend fun sendPasswordResetEmail(email: String): DataOrErrorResult<Boolean, Exception?> {
        val res = DataOrErrorResult<Boolean, Exception?>()
        try {
            auth.sendPasswordResetEmail(email).await()
            res.setData(true)
        } catch (e: Exception) {
            res.setException(e)
        }
        return res
    }

    override suspend fun createAccountWithGoogleAccount(): DataOrErrorResult<Boolean, Exception?> {
        TODO("Not yet implemented")
    }

    override suspend fun createAccountWithPhone(phoneNumber: String): DataOrErrorResult<Boolean, Exception?> {
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
                        res.setData(code)
                    } else {
                        res.setException(
                            java.lang.IllegalStateException(
                                "Got empty code on verification completed"
                            )
                        )
                    }
                    dataOrException.value = res
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    res.setException(e)
                    verificationId = ""
                    dataOrException.value = res
                }
            }
        )
        return dataOrException
    }

    override suspend fun verifyCode(code: String): DataOrErrorResult<Boolean, Exception?> {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        val res = DataOrErrorResult<Boolean, Exception?>()
        try {
            auth.signInWithCredential(credential).await()
            currentUser = getUser(auth.currentUser)
            res.setData(true)
        } catch (e: Exception) {
            res.setException(e)
        }
        verificationId = ""
        return res
    }

    override suspend fun signInWithGoogle(): DataOrErrorResult<Account?, Exception?> {
        TODO("Not yet implemented")
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): DataOrErrorResult<Boolean, Exception?> {
        val res = DataOrErrorResult<Boolean, Exception?>()
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            currentUser = getUser(auth.currentUser)
            res.setData(true)
        } catch (e: Exception) {
            res.setException(e)
        }
        return res
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