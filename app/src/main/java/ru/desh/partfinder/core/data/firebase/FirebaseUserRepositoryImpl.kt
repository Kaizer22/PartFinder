package ru.desh.partfinder.core.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await
import ru.desh.partfinder.core.di.module.UserDbReference
import ru.desh.partfinder.core.domain.model.User
import ru.desh.partfinder.core.domain.repository.UserRepository
import ru.desh.partfinder.core.utils.DataOrErrorResult
import javax.inject.Inject

class FirebaseUserRepositoryImpl @Inject constructor(
    @UserDbReference private val database: CollectionReference,
    private val auth: FirebaseAuth
) : UserRepository {
    override suspend fun createUser(user: User): DataOrErrorResult<Boolean, Exception?> {
        val res = DataOrErrorResult<Boolean, Exception?>()

        try {
            database.document(user.uid).set(user).await()
            auth.currentUser?.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName("${user.name} ${user.lastName}")
                    .build()
            )?.await()
            res.setData(true)
        } catch (e: Exception) {
            res.setException(e)
        }
        return res
    }

    override suspend fun editUser(user: User): DataOrErrorResult<Boolean, Exception?> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(user: User): DataOrErrorResult<Boolean, Exception?> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserByUid(uid: String): DataOrErrorResult<User?, Exception?> {
        TODO("Not yet implemented")
    }
}