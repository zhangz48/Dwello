package com.example.dwello.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _user = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val user: StateFlow<FirebaseUser?> = _user

    private val _signInState = MutableStateFlow<SignInState>(SignInState.Idle)
    val signInState: StateFlow<SignInState> = _signInState

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("AuthViewModel", "signInWithEmail:success")
                        _user.value = auth.currentUser
                        _signInState.value = SignInState.Success
                    } else {
                        Log.w("AuthViewModel", "signInWithEmail:failure", task.exception)
                        val errorMessage = when (task.exception) {
                            is FirebaseAuthInvalidCredentialsException -> "Invalid email or password."
                            is FirebaseAuthInvalidUserException -> "No account found with this email. Please sign up."
                            is FirebaseAuthUserCollisionException -> "This email is already in use."
                            else -> "Authentication failed. Please try again."
                        }
                        _signInState.value = SignInState.Failure(errorMessage)
                    }
                }
        }
    }

    fun signUp(email: String, password: String, firstName: String, lastName: String, onSignUpSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val user = result.user
                if (user != null) {
                    val userProfile = hashMapOf(
                        "first_name" to firstName,
                        "last_name" to lastName,
                        "email" to email,
                        "saved_listings" to emptyList<String>() // Initialize saved listings as an empty list
                    )
                    try {
                        firestore.collection("users").document(user.uid).set(userProfile).await()
                        Log.d("AuthViewModel", "User profile created")
                        onSignUpSuccess()
                    } catch (firestoreException: Exception) {
                        // If Firestore write fails, delete the created FirebaseAuth user
                        user.delete().await()
                        throw firestoreException
                    }
                }
            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is FirebaseAuthWeakPasswordException -> "Weak password. Please use a stronger password."
                    is FirebaseAuthUserCollisionException -> "This email is already in use."
                    else -> "Sign up failed. Please try again."
                }
                _signInState.value = SignInState.Failure(errorMessage)
            }
        }
    }

    fun signOut() {
        auth.signOut()
        _user.value = null
    }

    fun sendPasswordReset(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("AuthViewModel", "Email sent.")
                }
            }
    }

    fun resetSignInState() {
        _signInState.value = SignInState.Loading
    }
}

sealed class SignInState {
    object Idle : SignInState() // Initial or idle state
    object Success : SignInState()
    data class Failure(val errorMessage: String) : SignInState()
    object Loading : SignInState() // Loading state during sign-in attempt
}