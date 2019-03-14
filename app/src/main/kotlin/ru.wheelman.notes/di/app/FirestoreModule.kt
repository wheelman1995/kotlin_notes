package ru.wheelman.notes.di.app

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides

@Module
class FirestoreModule {

    private companion object {
        private const val NOTES_COLLECTION_PATH = "notes"
    }

    @Provides
    @AppScope
    fun firestore() = FirebaseFirestore.getInstance()

    @Provides
    @AppScope
    fun notesCollection(firestore: FirebaseFirestore) = firestore.collection(NOTES_COLLECTION_PATH)
}