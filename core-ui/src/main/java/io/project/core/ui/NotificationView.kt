package io.project.core.ui

interface NotificationView {
    fun showError(message: String)
    fun showMessage(message: String)
}