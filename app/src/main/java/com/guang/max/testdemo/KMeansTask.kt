package com.guang.max.testdemo

import android.graphics.Bitmap

/**
 * kMeansTask
 */
object KMeansTask {
  var K = 4
  var MAX_COUNT = 50
  val points = mutableListOf<ClusterPoint>()
  val centers = mutableListOf<ClusterPoint>()

  /**
   * 开始
   */
  fun start(b: Bitmap, callback: (c: List<ClusterPoint>) -> Unit) {
    val w = b.width
    val h = b.height

    points.clear()
    centers.clear()

    // 1. 添加 points
    fun first() {
      for (i in 0 until w) {
        for (j in 0 until h) {
          val p = b.getPixel(i, j)
          points.add(ClusterPoint(p))
        }
      }
    }

    // 2. 确定centers
    fun second() {
      points.random()
      for (i in 0 until K) {
        // 列表分割
        val step = points.size / K
        val list = points.subList(i * step, (i + 1) * step)
        val r = list.random()
        centers.add(r)
      }
    }

    // 3. 开始执行比较归类
    fun third() {
      for (p in points) {
        var min = Double.MAX_VALUE
        var currentIndex = 0
        for (c in centers) {
          val d = Utils.distance(p, c)
          if (min > d) {
            min = d
            currentIndex = centers.indexOf(c)
          }
        }
        p.clusterIndex = currentIndex
      }
    }

    // 4. 重新计算中心点，比较
    fun fourth() {
      centers.forEachIndexed { index, clusterPoint ->
        var sum = 0
        var r = 0
        var g = 0
        var b = 0
        points.forEach {
          // 当前中心点--所有的点
          if (it.clusterIndex == index) {
            sum++
            r += it.red()
            g += it.green()
            b += it.blue()
          }
        }
        clusterPoint.child = sum
        clusterPoint.oldColor = clusterPoint.color
        if (sum != 0) {
          clusterPoint.rgb(r / sum, g / sum, b / sum)
        }
      }
    }

    // 5. 比较 新旧两个中心点是否一致
    fun fifth(): Boolean {
      var isSame = false
      centers.forEach {
        isSame = it.oldColor == it.color
      }
      return isSame
    }

    // 执行
    first()
    second()
    var i = 0
    while (i < MAX_COUNT) {
      third()
      fourth()
      if (fifth()) {
        callback(centers.sortedByDescending { it.child })
        break
      }
      i++
    }
  }
}