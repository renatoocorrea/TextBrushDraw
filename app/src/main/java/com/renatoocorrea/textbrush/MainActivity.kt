package com.renatoocorrea.textbrush

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.consumeDownChange
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.unit.dp
import com.renatoocorrea.textbrush.ui.theme.TextBrushTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TextBrushTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TextBrushApp()
                }
            }
        }
    }
}

@Composable
fun TextBrushApp() {

    val pathsToBeDrawn = remember { mutableStateListOf<Pair<Path, PathProperties>>() }
    val undoneStack = remember { mutableStateListOf<Pair<Path, PathProperties>>() }
    var motionEvent by remember { mutableStateOf(MotionEvent.Idle) }
    var currentPosition by remember { mutableStateOf(Offset.Unspecified) }
    var previousPosition by remember { mutableStateOf(Offset.Unspecified) }
    var currentPath by remember { mutableStateOf(Path()) }
    var currentPathProperty by remember { mutableStateOf(PathProperties()) }

    val paint = remember {
        android.graphics.Paint().apply {
            textSize = 40f
            color = Color.Black.toArgb()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        val drawModifier = Modifier
            .padding(8.dp)
            .shadow(1.dp)
            .fillMaxWidth()
            .weight(1f)
            .background(Color.White)
            .dragMotionEvent(
                onStartingDrag = { pointerInputChange ->
                    motionEvent = MotionEvent.Down
                    currentPosition = pointerInputChange.position
                    pointerInputChange.consumeDownChange()

                },
                onDrag = { pointerInputChange ->
                    motionEvent = MotionEvent.Move
                    currentPosition = pointerInputChange.position
                    pointerInputChange.consumePositionChange()

                },
                onEndingDrag = { pointerInputChange ->
                    motionEvent = MotionEvent.Up
                    pointerInputChange.consumeDownChange()
                }
            )

        Canvas(modifier = drawModifier) {
            when (motionEvent) {

                MotionEvent.Down -> {
                    currentPath.moveTo(currentPosition.x, currentPosition.y)
                    previousPosition = currentPosition
                }

                MotionEvent.Move -> {
                    //Using this quadratic because using lines only, gave a quadratic look at
                    //brush path.
                    currentPath.quadraticBezierTo(
                        previousPosition.x,
                        previousPosition.y,
                        (previousPosition.x + currentPosition.x) / 2,
                        (previousPosition.y + currentPosition.y) / 2

                    )
                    previousPosition = currentPosition
                }

                MotionEvent.Up -> {
                    currentPath.lineTo(currentPosition.x, currentPosition.y)

                    pathsToBeDrawn.add(Pair(currentPath, currentPathProperty))
                    currentPath = Path()
                    currentPathProperty = PathProperties(
                        strokeWidth = currentPathProperty.strokeWidth,
                        color = currentPathProperty.color,
                        strokeCap = currentPathProperty.strokeCap,
                        strokeJoin = currentPathProperty.strokeJoin,
                        eraseMode = currentPathProperty.eraseMode
                    )

                    undoneStack.clear()
                    currentPosition = Offset.Unspecified
                    previousPosition = currentPosition
                    motionEvent = MotionEvent.Idle
                }

                else -> Unit
            }

            with(drawContext.canvas.nativeCanvas) {
                val checkPoint = saveLayer(null, null)
                pathsToBeDrawn.forEach {
                    val path = it.first
                    drawIntoCanvas {
                        it.nativeCanvas.drawTextOnPath(
                            "Hello World Example ",
                            path.asAndroidPath(),
                            0f,
                            0f,
                            paint
                        )
                    }
                }

                if (motionEvent != MotionEvent.Idle) {
                    drawPath(
                        color = currentPathProperty.color,
                        path = currentPath,
                        style = Stroke(
                            width = currentPathProperty.strokeWidth,
                            cap = currentPathProperty.strokeCap,
                            join = currentPathProperty.strokeJoin
                        )
                    )
                }
                restoreToCount(checkPoint)
            }
        }

        BottomMenu(
            modifier = Modifier
                .padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
                .shadow(1.dp, RoundedCornerShape(8.dp))
                .fillMaxWidth()
                .background(Color.White)
                .padding(4.dp),
            onUndo = {
                if (pathsToBeDrawn.isNotEmpty()) {
                    val lastItem = pathsToBeDrawn.last()
                    val lastPath = lastItem.first
                    val lastPathProperty = lastItem.second
                    pathsToBeDrawn.remove(lastItem)
                    undoneStack.add(Pair(lastPath, lastPathProperty))
                }
            },
            onRedo = {
                if (undoneStack.isNotEmpty()) {
                    val lastPath = undoneStack.last().first
                    val lastPathProperty = undoneStack.last().second
                    undoneStack.removeLast()
                    pathsToBeDrawn.add(Pair(lastPath, lastPathProperty))
                }
            }
        )
    }
}