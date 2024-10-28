package app.loococo.presentation.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@Composable
fun rememberPermissionLauncher(onResult: (Map<String, Boolean>) -> Unit) =
    rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
        onResult
    )

fun handlePermissionResult(permissions: Map<String, Boolean>, onSuccess: () -> Unit) {
    if (permissions.all { it.value }) {
        onSuccess()
    }
}

fun getPermissionsToRequest(): Array<String> {
    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
        }
        else -> {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }
}

fun checkPermission(
    context: Context,
    permissionLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
    onSuccess: () -> Unit
) {
    val permissionsToRequest = getPermissionsToRequest()

    if (permissionsToRequest.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }) {
        onSuccess()
    } else {
        if (permissionsToRequest.any { permission ->
                ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission)
            }) {
            permissionLauncher.launch(permissionsToRequest)
        } else {
            Toast.makeText(context, "권한을 직접 설정해야 합니다.", Toast.LENGTH_SHORT).show()
        }
    }
}