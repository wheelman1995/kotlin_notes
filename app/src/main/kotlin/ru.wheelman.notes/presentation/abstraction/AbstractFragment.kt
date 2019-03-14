package ru.wheelman.notes.presentation.abstraction

import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar

abstract class AbstractFragment<T : AbstractViewModel> : Fragment() {

    protected abstract var viewModel: T

    @CallSuper
    protected open fun initListeners() {
        viewModel.showError.observe(this, Observer {
            Snackbar.make(
                requireView(),
                it,
                Snackbar.LENGTH_LONG
            ).show()
        })
    }
}