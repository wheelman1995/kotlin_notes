package ru.wheelman.notes.presentation.auth

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import ru.wheelman.notes.di.app.AppScope
import javax.inject.Inject

@AppScope
class GoogleSignInHelper @Inject constructor(private val context: Context) {

    val client = GoogleSignIn.getClient(
        context,
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
    )
    val lastSignedInAccount: GoogleSignInAccount?
        get() = GoogleSignIn.getLastSignedInAccount(context)

    fun signOut() = client.signOut()
}