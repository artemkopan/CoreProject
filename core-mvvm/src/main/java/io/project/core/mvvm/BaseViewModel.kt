package io.project.core.mvvm

import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel(), BaseViewModelDelegate {

    override fun onCleared() {
        super.onCleared()
    }

}