package ru.wheelman.notes.model.datasources.remote

import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.channels.ReceiveChannel
import ru.wheelman.notes.di.app.AppScope
import ru.wheelman.notes.model.entities.Note
import ru.wheelman.notes.model.entities.Result
import javax.inject.Inject
import javax.inject.Provider
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@AppScope
class FirestoreDataSource @Inject constructor(private val notesCollection: Provider<CollectionReference>) :
    RemoteDataSource {

    override suspend fun getNoteById(noteId: String): Result = suspendCoroutine { continuation ->
        notesCollection.get().document(noteId).get()
            .addOnSuccessListener { documentSnapshot ->
                val note = documentSnapshot.toObject(Note::class.java)
                continuation.resume(
                    if (note == null) Result.Error(Throwable("note conversion error"))
                    else Result.Success(note)
                )
            }
            .addOnFailureListener {
                continuation.resume(Result.Error(it))
            }
    }

    override suspend fun saveNote(note: Note): Result = suspendCoroutine { continuation ->
        notesCollection.get().document(note.id).set(note)
            .addOnSuccessListener { continuation.resume(Result.Success(note)) }
            .addOnFailureListener { continuation.resume(Result.Error(it)) }
    }

    override suspend fun removeNote(noteId: String): Result = suspendCoroutine { continuation ->
        notesCollection.get().document(noteId).delete()
            .addOnSuccessListener { continuation.resume(Result.Success(noteId)) }
            .addOnFailureListener { continuation.resume(Result.Error(it)) }
    }

    override suspend fun subscribeToAllNotes(): ReceiveChannel<Result> =
        Channel<Result>(CONFLATED).apply {
            val registration = notesCollection.get().addSnapshotListener { snapshot, e ->
                e?.let {
                    offer(Result.Error(it))
                    return@addSnapshotListener
                }
                snapshot?.let {
                    val notes = snapshot.documents.mapNotNull { it.toObject(Note::class.java) }
                    offer(Result.Success(notes))
                } ?: offer(Result.Error(Throwable("No data received")))
            }
            invokeOnClose { registration.remove() }
        }
}