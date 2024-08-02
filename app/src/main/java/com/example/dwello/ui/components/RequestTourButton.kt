package com.example.dwello.ui.components

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import android.Manifest
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.dwello.ui.theme.Red100

@Composable
fun RequestTourButton(
    phoneNumber: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    val callPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                makePhoneCall(context, phoneNumber)
            } else {
                Toast.makeText(context, "Permission denied to make calls", Toast.LENGTH_SHORT).show()
            }
        }
    )

    Button(
        onClick = {
            showDialog = true
        },
        modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Red100),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = "Request a tour",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
    }

    handlePhoneCall(
        context = context,
        phoneNumber = phoneNumber,
        callPermissionLauncher = { callPermissionLauncher.launch(Manifest.permission.CALL_PHONE) },
        showDialog = showDialog,
        setShowDialog = { showDialog = it }
    )
}

@Composable
fun handlePhoneCall(
    context: Context,
    phoneNumber: String,
    callPermissionLauncher: () -> Unit,
    showDialog: Boolean,
    setShowDialog: (Boolean) -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { setShowDialog(false) },
            title = { Text(text = "Confirm Call") },
            text = { Text("Do you want to make a call to this number?") },
            confirmButton = {
                Button(
                    onClick = {
                        setShowDialog(false)
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) ==
                            PackageManager.PERMISSION_GRANTED) {
                            makePhoneCall(context, phoneNumber)
                        } else {
                            callPermissionLauncher()
                        }
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = { setShowDialog(false) }
                ) {
                    Text("No")
                }
            },
            containerColor = Color.White
        )
    }
}

fun makePhoneCall(context: Context, phoneNumber: String) {
    val intent = Intent(Intent.ACTION_CALL).apply {
        data = Uri.parse("tel:$phoneNumber")
    }
    try {
        context.startActivity(intent)
    } catch (e: SecurityException) {
        Toast.makeText(context, "Permission denied to make calls", Toast.LENGTH_SHORT).show()
    }
}