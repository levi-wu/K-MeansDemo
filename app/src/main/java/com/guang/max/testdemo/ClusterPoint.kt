package com.guang.max.testdemo

import android.graphics.Color

data class ClusterPoint(
  var color: Int,
  var oldColor: Int = color,
  var clusterIndex: Int = 0,
  var child: Int = 0,
)

fun ClusterPoint.red() = Color.red(color)
fun ClusterPoint.green() = Color.green(color)
fun ClusterPoint.blue() = Color.blue(color)
fun ClusterPoint.rgb(r: Int, g: Int, b: Int) {
  color = Color.rgb(r, g, b)
}