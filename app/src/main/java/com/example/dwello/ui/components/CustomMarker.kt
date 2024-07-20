package com.example.dwello.ui.components

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dwello.ui.theme.*
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import java.util.Locale


private val paintFill = Paint().apply {
    style = Paint.Style.FILL
    color = Color.Black.toArgb()
}

private val paintText = Paint().apply {
    color = Color.White.toArgb()
    textAlign = Paint.Align.CENTER
    textSize = 40f
    isAntiAlias = true
    typeface = android.graphics.Typeface.DEFAULT
}

fun setCustomMapIcon(message: String, isSelected: Boolean): BitmapDescriptor {
    val height = 90f
    val widthPadding = 50.dp.value
    val width = (paintText.measureText(message, 0, message.length) + widthPadding).coerceAtLeast(100f)
    val roundStart = height / 3
    val triangleHeight = height * 23 / 30  // Lower height for flatter triangle
    val triangleWidthFraction = 3f  // Larger fraction for wider base

    val path = Path().apply {
        arcTo(0f, 0f, roundStart * 2, roundStart * 2, -90f, -180f, true)
        lineTo(width / 2 - roundStart / triangleWidthFraction, height * 2 / 3)
        lineTo(width / 2, triangleHeight)
        lineTo(width / 2 + roundStart / triangleWidthFraction, height * 2 / 3)
        lineTo(width - roundStart, height * 2 / 3)
        arcTo(width - roundStart * 2, 0f, width, height * 2 / 3, 90f, -180f, true)
        lineTo(roundStart, 0f)
    }

    // Change the fill color based on isSelected
    paintFill.color = if (!isSelected) Blue80.toArgb() else Red100.toArgb()
    paintFill.setShadowLayer(5f, 0f, 3f, Color.Black.toArgb())

    val bm = Bitmap.createBitmap(width.toInt(), height.toInt(), Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bm)
    canvas.drawPath(path, paintFill)
    val textHeight = (paintText.descent() + paintText.ascent()) / 2
    canvas.drawText(message, width / 2, (height * 2 / 3) / 2 - textHeight, paintText)
    return BitmapDescriptorFactory.fromBitmap(bm)
}

fun formatPrice(price: Long): String {
    return when {
        price < 1_000 -> "$price"
        price < 10_000 -> String.format(Locale.US, "%.2fK", price / 1_000.0)
        price < 100_000 -> String.format(Locale.US, "%.1fK", price / 1_000.0)
        price < 1_000_000 -> "${price / 1_000}K"
        price < 10_000_000 -> String.format(Locale.US, "%.2fM", price / 1_000_000.0)
        price < 100_000_000 -> String.format(Locale.US, "%.1fM", price / 1_000_000.0)
        price < 1_000_000_000 -> "${price / 1_000_000}M"
        price < 10_000_000_000 -> String.format(Locale.US, "%.2fB", price / 1_000_000_000.0)
        price < 100_000_000_000 -> String.format(Locale.US, "%.1fB", price / 1_000_000_000.0)
        price < 1_000_000_000_000 -> "${price / 1_000_000_000}B"
        else -> String.format(Locale.US, "%.2fB", price / 1_000_000_000.0)
    }
}




// For Preview Only
fun createBitmapForCompose(message: String, isSelected: Boolean): Bitmap {
    val height = 90f
    val widthPadding = 50.dp.value
    val width = (paintText.measureText(message, 0, message.length) + widthPadding).coerceAtLeast(100f)
    val roundStart = height / 3
    val triangleHeight = height * 23 / 30  // Lower height for flatter triangle
    val triangleWidthFraction = 3f  // Larger fraction for wider base

    val path = Path().apply {
        arcTo(0f, 0f, roundStart * 2, roundStart * 2, -90f, -180f, true)
        lineTo(width / 2 - roundStart / triangleWidthFraction, height * 2 / 3)
        lineTo(width / 2, triangleHeight)
        lineTo(width / 2 + roundStart / triangleWidthFraction, height * 2 / 3)
        lineTo(width - roundStart, height * 2 / 3)
        arcTo(width - roundStart * 2, 0f, width, height * 2 / 3, 90f, -180f, true)
        lineTo(roundStart, 0f)
    }

    // Change the fill color based on isSelected
    paintFill.color = if (!isSelected) Blue80.toArgb() else Red100.toArgb()
    paintFill.setShadowLayer(5f, 0f, 3f, Color.Black.toArgb())

    val bm = Bitmap.createBitmap(width.toInt(), height.toInt(), Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bm)
    canvas.drawPath(path, paintFill)
    val textHeight = (paintText.descent() + paintText.ascent()) / 2
    canvas.drawText(message, width / 2, (height * 2 / 3) / 2 - textHeight, paintText)
    return bm
}

@Composable
fun MarkerPreview(message: String, isSelected: Boolean) {
    val bitmap = createBitmapForCompose(message, isSelected)
    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = null,
        modifier = Modifier.padding(16.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun MarkerPreviews() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .background(Color.LightGray)
        ) {
            MarkerPreview(formatPrice(1), isSelected = false)
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .background(Color.LightGray)
        ) {
            MarkerPreview(formatPrice(2_000_000_000), isSelected = true)
        }
    }
}