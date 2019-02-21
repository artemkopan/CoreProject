@file:Suppress("unused")

package io.project.core.android.adapter

import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView

class AdapterViewInteractionListener : AdapterView.OnItemSelectedListener, View.OnTouchListener {

    constructor()

    constructor(view: AdapterView<*>) {
        view.setOnTouchListener(this)
        view.onItemSelectedListener = this
    }

    var onUserSelectionListener: (Int) -> Unit = {}

    private var userSelect = false

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (userSelect) {
            onUserSelectionListener(position)
            userSelect = false
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        userSelect = true
        return false
    }
}
