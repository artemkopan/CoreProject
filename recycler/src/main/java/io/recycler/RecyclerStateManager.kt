package com.artemkopan.presentation.base.recycler

import android.os.Bundle
import android.os.Parcelable

@Suppress("MemberVisibilityCanBePrivate")
open class RecyclerStateManager {

    private var recyclerState: Parcelable? = null

    fun hasState() = recyclerState != null

    fun applyState(recyclerView: RecyclerView): Boolean {
        recyclerState?.let { state ->
            recyclerView.layoutManager?.apply {
                onRestoreInstanceState(state)
                recyclerState = null
                return true
            } ?: Logger.w(LOG_TAG, "Recycler's layout manager null")
        } ?: Logger.d(
            LOG_TAG,
            "Make sure that status was restored firstly RecyclerStateManager#restoreState"
        )
        return false
    }

    fun restoreState(bundle: Bundle?) {
        if (bundle == null) {
            return
        }
        recyclerState = bundle.getParcelable(getStateTag())
    }

    fun saveState(recyclerView: RecyclerView?, bundle: Bundle?) {
        recyclerView?.let { saveState(recyclerView.layoutManager, bundle) }
                ?: Logger.d(LOG_TAG, "RecyclerView is null")
    }

    fun saveState(layoutManager: RecyclerView.LayoutManager?, bundle: Bundle?) {
        if (bundle == null || layoutManager == null) {
            Logger.w(
                LOG_TAG, "Wrong parameters for saving status, bundle = $bundle, " +
                        "layout manager = $layoutManager"
            )
            return
        }
        bundle.putParcelable(getStateTag(), layoutManager.onSaveInstanceState())
    }

    open fun getStateTag() = KEY_STATE_GROUP

    companion object {
        private const val LOG_TAG = "RecyclerStateManager"
        private const val KEY_STATE_GROUP = "STATE_GROUP"
    }
}