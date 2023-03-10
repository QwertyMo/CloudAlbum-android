package ru.kettuproj.cloudalbum.ui.component.animation

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kettuproj.cloudalbum.common.dp
import ru.kettuproj.cloudalbum.common.sp
import kotlin.random.Random

@Composable
fun Shimmer(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit = {}
) {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.4f),
        Color.LightGray.copy(alpha = 0.1f),
        Color.LightGray.copy(alpha = 0.4f),
    )

    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )


    Box(modifier = modifier
        .background(brush)) {
            content()
    }
}

@Preview
@Composable
private fun ShimmerPreview(){
    Shimmer(modifier = Modifier.size(128.dp))
}

@Preview
@Composable
private fun ShimmerPreviewText(){
    Column {
        for (i in 0..10) {
            Spacer(modifier = Modifier.size(4.dp))
            Shimmer(
                modifier = Modifier
                    .width((64 + Math.random() * 64).dp)
                    .height(12.sp.dp())
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    }
}