package ru.wheelman.notes.model.datasources.remote

import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import ru.wheelman.notes.di.app.AppScope
import ru.wheelman.notes.model.entities.Note
import ru.wheelman.notes.model.entities.Result
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@AppScope
class FirestoreDataSource @Inject constructor(private val notesCollection: CollectionReference) :
    RemoteDataSource {

    private val allNotesChannel = Channel<Result>(Channel.CONFLATED)

    override suspend fun getNoteById(noteId: String): ReceiveChannel<Result> =
        produce { sendChannel ->
            notesCollection.document(noteId).get()
                .addOnSuccessListener { documentSnapshot ->
                    val note = documentSnapshot.toObject(Note::class.java)
                    note?.run {
                        launch { sendChannel.send(Result.Success(this@run)) }
                    }
                }
                .addOnFailureListener {
                    launch { sendChannel.send(Result.Error(it)) }
                }
        }

    override suspend fun saveNote(note: Note): ReceiveChannel<Result> =
        produce { sendChannel ->
            notesCollection.document(note.id).set(note)
                .addOnSuccessListener { launch { sendChannel.send(Result.Success(note)) } }
                .addOnFailureListener { launch { sendChannel.send(Result.Error(it)) } }
        }

    override suspend fun subscribeToAllNotes(): ReceiveChannel<Result> {
        launchInChildScope {
            notesCollection.addSnapshotListener { snapshot, e ->
                e?.let {
                    launch { allNotesChannel.send(Result.Error(it)) }
                    return@addSnapshotListener
                }
                snapshot?.let {
                    val notes = snapshot.documents.mapNotNull { it.toObject(Note::class.java) }
                    launch { allNotesChannel.send(Result.Success(notes)) }
                }
            }
        }
        return allNotesChannel
    }

    private suspend fun produce(block: suspend CoroutineScope.(SendChannel<Result>) -> Unit): ReceiveChannel<Result> {
        val channel = Channel<Result>(Channel.CONFLATED)
        launchInChildScope { block(channel) }
        return channel
    }

    private suspend fun launchInChildScope(block: suspend CoroutineScope.() -> Unit) {
        val childScope = CoroutineScope(coroutineContext)
        childScope.launch { childScope.block() }
    }
}