package io.project.core.ui

interface ProgressBarController {

    fun onViewCreated()

    fun hide()
    /**
     * Show the progress view after waiting for a minimum delay. If
     * during that time, hide() is called, the view is never made visible.
     */
    fun show()
}