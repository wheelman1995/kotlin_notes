package ru.wheelman.notes

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.runner.AndroidJUnit4
import io.mockk.mockk
import org.junit.Test
import org.junit.runner.RunWith
import ru.wheelman.notes.presentation.main.MainFragmentDirections
import ru.wheelman.notes.presentation.noteeditor.NoteEditorFragment

@RunWith(AndroidJUnit4::class)
class NoteEditorFragmentTest {

    @Test
    fun should_show_color_picker() {
        val scenario = launchFragmentInContainer<NoteEditorFragment>(
            fragmentArgs = MainFragmentDirections.actionMainFragmentToNoteEditorFragment("123").arguments,
            themeResId = R.style.Theme_MaterialComponents_NoActionBar
        ).onFragment { Navigation.setViewNavController(it.requireView(), mockk()) }

        onView(withId(R.id.color_picker)).perform(click())
        onView(withId(R.id.cpv)).check(matches(isCompletelyDisplayed()))
    }
}