package ru.desh.partfinder.core.data.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ru.desh.partfinder.core.domain.model.Account
import ru.desh.partfinder.core.domain.repository.AuthRepository
import ru.desh.partfinder.core.utils.DataOrErrorResult
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
): AuthRepository {
    override fun createAccountWithEmailAndPassword(email: String, password: String): LiveData<DataOrErrorResult<Account?, Exception?>> {
//        val dataOrException: MutableStateFlow<DataOrErrorResult<Account?, Exception?>> = MutableStateFlow(
//            DataOrErrorResult(Account.getEmpty(), null, false)
//        )
        val dataOrException = MutableLiveData<DataOrErrorResult<Account?, Exception?>>()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{ task ->
            val res = DataOrErrorResult<Account?, Exception?>()
            if (task.isSuccessful) {
                Log.d("AUTH", "created user $email $password")
                res.data = getUser(auth.currentUser)
            } else {
                Log.d("AUTH", "error ${task.exception.toString()}")
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

    override fun createAccountWithGoogleAccount(): LiveData<DataOrErrorResult<Account?, Exception?>> {
        TODO("Not yet implemented")
    }

    override fun createAccountWithPhone(phoneNumber: String): LiveData<DataOrErrorResult<Account?, Exception?>> {
        TODO("Not yet implemented")
    }

    override fun sendVerificationCode(phoneNumber: String): LiveData<DataOrErrorResult<Account?, Exception?>> {
        TODO("Not yet implemented")
    }

    override fun verifyCode(code: String): LiveData<DataOrErrorResult<Account?, Exception?>> {
        TODO("Not yet implemented")
    }

    override fun signInWithGoogle(): LiveData<DataOrErrorResult<Account?, Exception?>> {
        TODO("Not yet implemented")
    }

    override fun signInWithEmailAndPassword(email: String, password: String): LiveData<DataOrErrorResult<Account?, Exception?>> {
//        val dataOrException: MutableStateFlow<DataOrErrorResult<Account?, Exception?>> = MutableStateFlow(
//            DataOrErrorResult(Account.getEmpty(), null, false)
//        )
        val dataOrException = MutableLiveData<DataOrErrorResult<Account?, Exception?>>()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            val res = DataOrErrorResult<Account?, Exception?>()
            if (task.isSuccessful) {
                Log.d("AUTH", "logged in as $email $password")
                res.data = getUser(auth.currentUser)
            } else {
                Log.d("AUTH", "error ${task.exception.toString()}")
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
            Account(
                currentUser.uid, regType,
                // TODO check phone verification
                currentUser.isEmailVerified,
                phone,
                email
            )
        }
    }

    override fun createAccountWithFacebook() {
        TODO("Not yet implemented")
    }

    override fun signInWithFacebook() {
        TODO("Not yet implemented")
    }
}