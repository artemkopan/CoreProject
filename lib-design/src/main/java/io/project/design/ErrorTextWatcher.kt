package io.project.design

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout


fun EditText.addErrorInputWatcher() {
    if (parent?.parent is TextInputLayout) {
        addTextChangedListener(ErrorTextWatcher(parent.parent as TextInputLayout))
    } else {
        throw IllegalArgumentException("Edit text parent must be TextInputLayout, parent = $parent")
    }
}

class ErrorTextWatcher constructor(private val inputLayout: TextInputLayout) : TextWatcher {

    override fun afterTextChanged(text: Editable?) {
        inputLayout.error = null
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // no-op
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // no-op
    }

}