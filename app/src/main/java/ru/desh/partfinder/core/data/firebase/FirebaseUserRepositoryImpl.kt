package ru.desh.partfinder.core.data.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.CollectionReference
import ru.desh.partfinder.core.di.module.UserDbReference
import ru.desh.partfinder.core.domain.model.User
import ru.desh.partfinder.core.domain.repository.UserRepository
import ru.desh.partfinder.core.utils.DataOrErrorResult
import javax.inject.Inject

class FirebaseUserRepositoryImpl @Inject constructor(
    @UserDbReference private val database: CollectionReference,
    private val auth: FirebaseAuth
) : UserRepository {
    override fun createUser(user: User): LiveData<DataOrErrorResult<Boolean, Exception>> {
        val resultObserver = MutableLiveData<DataOrErrorResult<Boolean, Exception>>()
        database.document(user.uid).set(user).addOnSuccessListener {
            auth.currentUser?.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName("${user.name} ${user.lastName}")
                    .build()
            )?.addOnSuccessListener {
                resultObserver.value = DataOrErrorResult(true, null)
            }?.addOnFailureListener {
                resultObserver.value = DataOrErrorResult(false, it)
            }
        }.addOnFailureListener {
            resultObserver.value = DataOrErrorResult(false, it)
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