package com.renatoocorrea.textbrush

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun DrawingPropertiesMenu(
    modifier: Modifier = Modifier,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(
            onClick = {
            }
        ) {
            Icon(
                Icons.Filled.TouchApp,
                contentDescription = null,
                tint = Color.Blue
            )
        }

        IconButton(onClick = {
            onUndo()
        }) {
            Icon(Icons.Filled.Undo, contentDescription = null, tint = Color.LightGray)
        }

        IconButton(onClick = {
            onRedo()
        }) {
            Icon(Icons.Filled.Redo, contentDescription = null, tint = Color.LightGray)
        }
    }
}
