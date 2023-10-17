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
import androidx.compose.ui.res.painterResource

enum class DrawMode {
    Draw
}

@Composable
fun DrawingPropertiesMenu(
    modifier: Modifier = Modifier,
    drawMode: DrawMode,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onDrawModeChanged: (DrawMode) -> Unit
) {

    var currentDrawMode = drawMode

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(
            onClick = {
                currentDrawMode = if (currentDrawMode == DrawMode.Draw) {
                    DrawMode.Draw
                } else {
                    DrawMode.Draw
                }
                onDrawModeChanged(currentDrawMode)
            }
        ) {
            Icon(
                Icons.Filled.TouchApp,
                contentDescription = null,
                tint = if (currentDrawMode == DrawMode.Draw) Color.Black else Color.LightGray
            )
        }
        /*IconButton(
            onClick = {
                currentDrawMode = if (currentDrawMode == DrawMode.Draw) {
                    DrawMode.Draw
                } else {
                    DrawMode.Draw
                }
                onDrawModeChanged(currentDrawMode)
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                tint = if (currentDrawMode == DrawMode.Erase) Color.Black else Color.LightGray
            )
        }*/


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
