package ru.wheelman.notes.model.datasources.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import io.mockk.every
import io.mockk.excludeRecords
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.wheelman.notes.model.entities.Result
import javax.inject.Provider

class FirestoreDataSourceTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val notesCollection = mockk<Provider<CollectionReference>>()
    private val remoteDataSource: RemoteDataSource = FirestoreDataSource(notesCollection)
    private val noteId = "123"

    @Before
    fun setup() {
    }

    @Test
    fun `test successful note deletion`() = runBlocking {
        val slot = slot<OnSuccessListener<in Void>>()
        val documentReference = mockk<DocumentReference>()
        every { notesCollection.get().document(any()) } returns documentReference
        every {
            documentReference.delete().addOnSuccessListener { capture(slot) }
        } returns mockk()
        excludeRecords { documentReference.delete().addOnFailureListener { any() } }
        val channel = remoteDataSource.removeNote(noteId)
        slot.captured.onSuccess(null)
        val result = channel.receive()
        assertEquals(result, Result.Success(noteId))
    }

    @Test
    fun `test failed note deletion`() = runBlocking {
        val slot = slot<OnFailureListener>()
        every {
            notesCollection.get().document(any()).delete().addOnFailureListener { capture(slot) }
        } returns mockk()
        excludeRecords { notesCollection.get().document().delete().addOnSuccessListener { any() } }
        val channel = remoteDataSource.removeNote(noteId)
        delay(100)
        slot.captured.onFailure(Exception())
        val result = channel.receive()
        assertEquals(result, Result.Error(Exception()))
    }
}