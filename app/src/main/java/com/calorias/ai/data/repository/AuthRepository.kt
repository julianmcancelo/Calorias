package com.calorias.ai.data.repository

import com.calorias.ai.data.local.dao.UserDao
import com.calorias.ai.data.local.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val userDao: UserDao
) {
    val currentUser: FirebaseUser?
        get() = auth.currentUser

    fun getCurrentUserFlow(uid: String): Flow<User?> = userDao.getUserFlow(uid)

    suspend fun signInAnonymously(): Result<FirebaseUser> {
        return try {
            val result = auth.signInAnonymously().await()
            val user = result.user ?: return Result.failure(Exception("No user"))
            syncUserToLocal(user)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signInWithEmail(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user ?: return Result.failure(Exception("No user"))
            syncUserToLocal(user)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUpWithEmail(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user ?: return Result.failure(Exception("No user"))
            syncUserToLocal(user)
            createUserInFirestore(user)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signOut() {
        auth.signOut()
    }

    private suspend fun syncUserToLocal(firebaseUser: FirebaseUser) {
        val localUser = User(
            uid = firebaseUser.uid,
            email = firebaseUser.email,
            displayName = firebaseUser.displayName,
            photoUrl = firebaseUser.photoUrl?.toString(),
            isPremium = false,
            dailyScanLimit = 5
        )
        userDao.insert(localUser)
    }

    private suspend fun createUserInFirestore(firebaseUser: FirebaseUser) {
        val userData = hashMapOf(
            "email" to firebaseUser.email,
            "displayName" to firebaseUser.displayName,
            "photoUrl" to firebaseUser.photoUrl?.toString(),
            "isPremium" to false,
            "dailyScanLimit" to 5,
            "createdAt" to System.currentTimeMillis()
        )
        firestore.collection("users").document(firebaseUser.uid).set(userData).await()
    }

    suspend fun fetchUserFromFirestore(uid: String): User? {
        return try {
            val doc = firestore.collection("users").document(uid).get().await()
            if (doc.exists()) {
                User(
                    uid = uid,
                    email = doc.getString("email"),
                    displayName = doc.getString("displayName"),
                    photoUrl = doc.getString("photoUrl"),
                    isPremium = doc.getBoolean("isPremium") ?: false,
                    dailyScanLimit = doc.getLong("dailyScanLimit")?.toInt() ?: 5,
                    subscriptionId = doc.getString("subscriptionId"),
                    subscriptionExpiresAt = doc.getLong("subscriptionExpiresAt")
                )
            } else null
        } catch (e: Exception) {
            null
        }
    }
}
