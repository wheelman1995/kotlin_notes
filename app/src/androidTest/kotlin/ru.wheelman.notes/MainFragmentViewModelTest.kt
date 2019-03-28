package ru.wheelman.notes

import androidx.test.runner.AndroidJUnit4
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith
import ru.wheelman.notes.model.entities.CurrentUser
import ru.wheelman.notes.presentation.main.MainFragmentViewModel

@RunWith(AndroidJUnit4::class)
class MainFragmentViewModelTest {

    private val fb = mockk<FirebaseFirestore>()
    private val currentUser = CurrentUser().apply { id = "1" }

    @Test
    fun should_call_subscribeToAllNotes_once() {
        every { FirebaseFirestore.getInstance() } returns fb
        every { fb.collection(any()).document(any()).collection(any()) } returns mockk()
        val viewModel = spyk(MainFragmentViewModel(mockk()))
        verify { viewModel["subscribeToAllNotes"] }
    }
}