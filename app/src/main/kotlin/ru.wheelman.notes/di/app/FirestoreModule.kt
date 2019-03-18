package ru.wheelman.notes.di.app

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import ru.wheelman.notes.model.entities.CurrentUser

@Module
class FirestoreModule {

    private companion object {
        private const val NOTES_COLLECTION = "notes"
        private const val USERS_COLLECTION = "users"
    }

    @Provides
    @AppScope
    fun firestore() = FirebaseFirestore.getInstance()

    @Provides
    fun notesCollection(firestore: FirebaseFirestore, currentUser: CurrentUser) =
        firestore.collection(USERS_COLLECTION).document(currentUser.id).collection(NOTES_COLLECTION)
}