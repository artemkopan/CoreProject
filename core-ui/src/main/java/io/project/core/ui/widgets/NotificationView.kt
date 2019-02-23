package io.project.core.ui.widgets

interface NotificationView {
    fun showError(message: String)
    fun showMessage(message: String)
}