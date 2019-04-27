package io.project.core.mvvm

import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel(), BaseViewModelDelegate {

    abstract override fun onCleared()

}