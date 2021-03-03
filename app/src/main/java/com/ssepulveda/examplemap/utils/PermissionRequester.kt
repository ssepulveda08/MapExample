package com.ssepulveda.examplemap.utils

import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts

class PermissionRequester(activity: ComponentActivity) {

    private var onGrated: () -> Unit = {}

    private var onDenied: () -> Unit = {}

    private val permissionLauncher = activity.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        when {
            isGranted -> onGrated()
            //Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(permission) -> toast("shouldShow")
            else -> onDenied()
        }
    }

    fun runWithPermission(permission: String, action: () -> Unit) {
        onGrated = action
        permissionLauncher.launch(permission)
    }

}