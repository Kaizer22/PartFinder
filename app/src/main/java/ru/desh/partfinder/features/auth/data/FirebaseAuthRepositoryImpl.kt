package ru.desh.partfinder.features.auth.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.desh.partfinder.core.domain.model.Account
import ru.desh.partfinder.core.utils.DataOrErrorResult
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
): AuthRepository{
    override fun createUserWithEmailAndPassword(email: String, password: String): StateFlow<DataOrErrorResult<Account?, Exception?>> {
        val dataOrException: MutableStateFlow<DataOrErrorResult<Account?, Exception?>> = MutableStateFlow(
            DataOrErrorResult(Account("", ""), null, false)
        )
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

    override fun createUserWithGoogleAccount(): StateFlow<DataOrErrorResult<Account?, Exception?>> {
        TODO("Not yet implemented")
    }

    override fun createUserWithPhone(): StateFlow<DataOrErrorResult<Account?, Exception?>> {
        TODO("Not yet implemented")
    }

    override fun signInWithGoogle(): StateFlow<DataOrErrorResult<Account?, Exception?>> {
        TODO("Not yet implemented")
    }

    override fun signInWithPhone(): StateFlow<DataOrErrorResult<Account?, Exception?>> {
        TODO("Not yet implemented")
    }

    override fun signInWithEmailAndPassword(email: String, password: String): StateFlow<DataOrErrorResult<Account?, Exception?>> {
        val dataOrException: MutableStateFlow<DataOrErrorResult<Account?, Exception?>> = MutableStateFlow(
            DataOrErrorResult(Account("", ""), null, false)
        )
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
        return if (currentUser == null) null else
            Account(currentUser.uid, currentUser.email ?: "empty")
    }

    override fun createUserWithFacebook() {
        TODO("Not yet implemented")
    }

    override fun signInWithFacebook() {
        TODO("Not yet implemented")
    }
}