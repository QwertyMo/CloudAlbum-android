package ru.kettuproj.cloudalbum.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

@Composable
fun Float.dp() = with(LocalDensity.current) {  Dp(this@dp).toSp() }

@Composable
fun Int.dp() = with(LocalDensity.current) {  Dp(this@dp.toFloat()).toSp()  }

@Composable
fun Int.sp() = with(LocalDensity.current) { this@sp.toSp() }

@Composable
fun TextUnit.dp() = with(LocalDensity.current) {this@dp.toDp()}