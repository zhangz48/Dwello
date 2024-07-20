package com.example.dwello.utils

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.drawToBitmap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

fun getBitmapFromComposable(context: Context, content: @Composable () -> Unit): Bitmap {
    val composeView = ComposeView(context).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            content()
        }
        measure(
            android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED),
            android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED)
        )
        layout(0, 0, measuredWidth, measuredHeight)
    }
    return composeView.drawToBitmap()
}

fun bitmapDescriptorFromComposable(context: Context, content: @Composable () -> Unit): BitmapDescriptor {
    val bitmap = getBitmapFromComposable(context, content)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}