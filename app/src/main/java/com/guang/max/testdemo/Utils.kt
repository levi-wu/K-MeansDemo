package com.guang.max.testdemo

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlin.math.pow
import kotlin.math.sqrt


object Utils {

  private fun calculateInSampleSize(
    options: BitmapFactory.Options,
    reqWidth: Int, reqHeight: Int
  ): Int {
    // 源图片的高度和宽度
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1
    if (height > reqHeight || width > reqWidth) {
      // 计算出实际宽高和目标宽高的比率
      val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
      val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
      // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
      // 一定都会大于等于目标的宽和高。
      inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
    }
    return inSampleSize
  }

  fun decodeSampledBitmapFromResource(
    res: Resources, resId: Int,
    reqWidth: Int, reqHeight: Int
  ): Bitmap? {
    // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    // 读取图片长宽
    BitmapFactory.decodeResource(res, resId, options)
    // 调用上面定义的方法计算inSampleSize值
    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
    // 使用获取到的inSampleSize值再次解析图片
    options.inJustDecodeBounds = false
    return BitmapFactory.decodeResource(res, resId, options)
  }

  fun distance(c: ClusterPoint, c1: ClusterPoint): Double {
    val r = (c.red() - c1.red()).toDouble().pow(2)
    val g = (c.green() - c1.green()).toDouble().pow(2)
    val b = (c.blue() - c1.blue()).toDouble().pow(2)
    return sqrt(r + g + b)
  }
}