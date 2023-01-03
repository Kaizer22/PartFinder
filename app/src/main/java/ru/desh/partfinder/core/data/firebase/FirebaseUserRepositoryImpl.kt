package ru.desh.partfinder.core.data.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.CollectionReference
import ru.desh.partfinder.core.di.module.UserDbReference
import ru.desh.partfinder.core.domain.model.User
import ru.desh.partfinder.core.domain.repository.UserRepository
import ru.desh.partfinder.core.utils.DataOrErrorResult
import javax.inject.Inject

class FirebaseUserRepositoryImpl @Inject constructor(
    @UserDbReference private val database: CollectionReference
): UserRepository {
    override fun createUser(user: User): LiveData<DataOrErrorResult<Boolean, Exception>> {
        val resultObserver = MutableLiveData<DataOrErrorResult<Boolean, Exception>>()
        database.document(user.uid).set(user).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                resultObserver.value = DataOrErrorResult(true, null)
            } else {
                resultObserver.value = DataOrErrorResult(false, task.exception)
            }
        }
        return resultObserver
    }

    override fun editUser(user: User): LiveData<DataOrErrorResult<Boolean, Exception>> {
        TODO("Not yet implemented")
    }

    override fun deleteUser(user: User): LiveData<DataOrErrorResult<Boolean, Exception>> {
        TODO("Not yet implemented")
    }

    override fun getUserByUid(uid: String): LiveData<DataOrErrorResult<User?, Exception>> {
        TODO("Not yet implemented")
    }
}