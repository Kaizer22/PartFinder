package ru.desh.partfinder.core.di.module

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class UserDbReference

@Qualifier
annotation class AdDbReference

@Qualifier
annotation class ChatDbReference

@Module
class FirebaseModule {
    private val USER_COLLECTION = "users"
    private val AD_COLLECTION = "ads"
    private val CHAT_COLLECTION = "chats"

    @Provides
    @Singleton
    fun providesFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    @UserDbReference
    fun providesFirebaseUserReference(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(USER_COLLECTION)
    }

    @Provides
    @Singleton
    @AdDbReference
    fun providesFirebaseAdReference(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(AD_COLLECTION)
    }

    @Provides
    @Singleton
    @ChatDbReference
    fun providesFirebaseChatReference(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(CHAT_COLLECTION)
    }
}