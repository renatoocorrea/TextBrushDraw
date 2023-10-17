package com.renatoocorrea.textbrush

import android.R.attr.path
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import com.renatoocorrea.textbrush.ui.theme.TextBrushTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow


class MainActivity : ComponentActivity() {

    private val mutableState =
        MutableStateFlow(
            Path()
        )

    val state: Flow<Path> = mutableState
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val path = Path()
        val auxX = 411
        val auxY = 656
        path.lineTo(auxX.toFloat(), auxY.toFloat())
        setContent {
            TextBrushTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    /*Box(modifier = Modifier.fillMaxSize()) {
//                    Greeting("Android")
                        ArcTextExample()
                    }*/
                    Box(
                        Modifier.pointerInput(Unit) {
                                detectDragGestures { change, dragAmount ->

                                    val x = change.position.x
                                    val y = change.position.y
                                    Log.e("TESTE", "X: " + x)
                                    Log.e("TESTE", "Y: " + y)
                                    path.lineTo(x,y)


//                                    change.consume()
                                    //...
                                }
                            }

                    ){
                        NewArcTextExample(path)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TextBrushTheme {
        Greeting("Android")
    }
}

@Composable
fun ArcTextExample() {
    val paint = Paint().asFrameworkPaint()
    Canvas(modifier = Modifier.fillMaxSize()) {
        paint.apply {
            isAntiAlias = true
            textSize = 24f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        drawIntoCanvas {
            val path = Path()
            path.addArc(RectF(0f, 100f, 200f, 300f), 270f, 180f)
            it.nativeCanvas.drawTextOnPath("Hello World Example", path, 0f, 0f, paint)
        }
    }
}

@Composable
fun NewArcTextExample(path: Path) {
    Log.e("TESTE", "NEW ARCTEST:  " + path.toString())
    val paint = Paint().asFrameworkPaint()
    Canvas(modifier = Modifier.fillMaxSize()) {
        paint.apply {
            isAntiAlias = true
            textSize = 24f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        drawIntoCanvas {
            it.nativeCanvas.drawTextOnPath("Hello World Example", path, 0f, 0f, paint)
        }
    }
}

@Composable
fun CanvasCircleExample() {
    // [START android_compose_graphics_canvas_circle]
    Canvas(modifier = Modifier.fillMaxSize()) {
        val canvasQuadrantSize = size / 2F
        drawRect(
            color = Color.Magenta,
            size = canvasQuadrantSize
        )
    }
    // [END android_compose_graphics_canvas_circle]
}
