package ru.kettuproj.cloudalbum.ui

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

class RandomGradient {
    companion object{
        fun generate(seed: Int):GradientColors{
            val random = Random(seed)
            val colors = mutableListOf(
                Color(244, 67, 54, 255),
                Color(233, 30, 99, 255),
                Color(156, 39, 176, 255),
                Color(103, 58, 183, 255),
                Color(63, 81, 181, 255),
                Color(33, 150, 243, 255),
                Color(3, 169, 244, 255),
                Color(0, 188, 212, 255),
                Color(0, 150, 136, 255),
                Color(76, 175, 80, 255),
                Color(139, 195, 74, 255),
                Color(205, 220, 57, 255),
                Color(255, 235, 59, 255),
                Color(255, 193, 7, 255),
                Color(255, 152, 0, 255),
                Color(255, 87, 34, 255),
            )
            val f = colors.random(random)
            colors.remove(f)
            val s = colors.random(random)
            return GradientColors(f, s)
        }

        data class GradientColors(
            val first: Color,
            val second: Color
        )
    }
}