package ru.wheelman.notes.presentation.auth

import android.Manifest.permission.READ_PHONE_STATE
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.Lazy
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import ru.wheelman.notes.R
import ru.wheelman.notes.di.app.AppScope
import ru.wheelman.notes.model.entities.AuthMode.*
import ru.wheelman.notes.model.entities.CurrentUser
import ru.wheelman.notes.presentation.utils.PreferenceHelper
import javax.inject.Inject

@AppScope
class Authenticator @Inject constructor(
    private val googleSignInHelper: Lazy<GoogleSignInHelper>,
    private val guestSignInHelper: Lazy<GuestSignInHelper>,
    private val preferenceHelper: PreferenceHelper,
    private val currentUser: CurrentUser,
    private val context: Context
) {

    private val disposable: CompositeDisposable = CompositeDisposable()

    private companion object {
        private const val RC_SIGN_IN = 8735
    }

    internal fun tryToAuthenticate(
        startActivityForResult: (Intent, Int) -> Single<ActivityResult>,
        checkPermission: (String) -> Completable
    ): Completable = when (preferenceHelper.getAuthMode()) {
        GOOGLE -> tryAuthorizingUserWithGoogle(startActivityForResult)
        GUEST -> tryAuthorizingUserAsGuest(checkPermission)
        UNAUTHORIZED -> Completable.never()
    }

    fun tryAuthorizingUserWithGoogle(startActivityForResult: (Intent, Int) -> Single<ActivityResult>): Completable {
        return Completable.create { emitter ->
            val lastSignedInAccount = googleSignInHelper.get().lastSignedInAccount
            if (lastSignedInAccount != null) {
                authorizeUserWithGoogle(lastSignedInAccount)
                emitter.onComplete()
            } else {
                val d = startActivityForResult(
                    googleSignInHelper.get().client.signInIntent,
                    RC_SIGN_IN
                ).subscribe { activityResult ->
                    val (requestCode, resultCode, data) = activityResult
                    if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {
                        // The Task returned from this call is always completed, no need to attach a listener.
                        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                        val account = task.result
                        if (account != null) {
                            authorizeUserWithGoogle(account)
                            emitter.onComplete()
                        } else {
                            emitter.onError(Throwable(context.getString(R.string.could_not_sign_in_with_google)))
                        }
                    }
                }
                disposable.add(d)
            }
        }
    }

    private fun authorizeUserWithGoogle(account: GoogleSignInAccount) {
        preferenceHelper.putAuthMode(GOOGLE)
        currentUser.id = account.id!!
    }

    fun tryAuthorizingUserAsGuest(checkPermission: (String) -> Completable): Completable {
        return Completable.create { emitter ->
            val d = checkPermission(READ_PHONE_STATE).subscribe(
                @SuppressLint("MissingPermission") {
                    authorizeUserAsGuest()
                    emitter.onComplete()
                },
                { emitter.onError(Throwable(context.getString(R.string.phone_permission_denied))) }
            )
            disposable.add(d)
        }
    }

    @RequiresPermission(READ_PHONE_STATE)
    private fun authorizeUserAsGuest() {
        preferenceHelper.putAuthMode(GUEST)
        currentUser.id = guestSignInHelper.get().getId()
    }

    fun onDestroyView() {
        disposable.clear()
    }

    fun logout() {
        if (preferenceHelper.getAuthMode() == GOOGLE) googleSignInHelper.get().signOut()
        preferenceHelper.putAuthMode(UNAUTHORIZED)
    }
}