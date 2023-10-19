package com.renatoocorrea.textbrush

import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitTouchSlopOrCancellation
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput


enum class MotionEvent {
    Idle, Down, Move, Up
}

suspend fun AwaitPointerEventScope.awaitDragMotionEvent(
    onStartingDrag: (PointerInputChange) -> Unit = {},
    onDrag: (PointerInputChange) -> Unit = {},
    onEndingDrag: (PointerInputChange) -> Unit = {}
) {
    val down: PointerInputChange = awaitFirstDown()
    onStartingDrag(down)

    var pointer = down

    val change: PointerInputChange? =
        awaitTouchSlopOrCancellation(down.id) { change: PointerInputChange, over: Offset ->
            change.consumePositionChange()
        }

    if (change != null) {
        drag(change.id) { pointerInputChange: PointerInputChange ->
            pointer = pointerInputChange
            onDrag(pointer)
        }
        onEndingDrag(pointer)
    } else {
        onEndingDrag(pointer)
    }
}

fun Modifier.dragMotionEvent(
    onStartingDrag: (PointerInputChange) -> Unit = {},
    onDrag: (PointerInputChange) -> Unit = {},
    onEndingDrag: (PointerInputChange) -> Unit = {}
) = this.then(
    Modifier.pointerInput(Unit) {
        forEachGesture {
            awaitPointerEventScope {
                awaitDragMotionEvent(onStartingDrag, onDrag, onEndingDrag)
            }
        }
    }
)