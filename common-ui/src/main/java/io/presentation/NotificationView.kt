package io.presentation

interface NotificationView {
    fun showError(message: String)
    fun showMessage(message: String)
}