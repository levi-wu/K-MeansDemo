package com.guang.max.testdemo

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.palette.graphics.Palette
import com.guang.max.testdemo.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

  lateinit var binding: ActivityMainBinding

  var bitmap: Bitmap? = null

  var id = 0

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.start.setOnClickListener {
      startTask()
    }

    binding.choose.setOnClickListener {
      id = when (id) {
        R.drawable.labi -> R.drawable.labi1
        R.drawable.labi1 -> R.drawable.labi2
        R.drawable.labi2 -> R.drawable.labi3
        else -> R.drawable.labi
      }
      val size = width()
      bitmap = Utils.decodeSampledBitmapFromResource(resources, id, size, size)
      binding.image.setImageBitmap(bitmap)
    }

    binding.startPalette.setOnClickListener {
      bitmap?.let {
        startPalette(it)
      }
    }
  }

  override fun onResume() {
    super.onResume()
  }

  private fun startTask() {
    binding.color.removeAllViews()
    val pd = ProgressDialog.show(this, null, "执行中...")
    GlobalScope.launch(Dispatchers.IO) {
      bitmap ?: return@launch

      val b = Utils.decodeSampledBitmapFromResource(resources, id, 100, 100)
      KMeansTask.start(b!!) {
        GlobalScope.launch(Dispatchers.Main) {
          Log.e("hhhh", it.map { it.color }.toList().toString())
          pd.dismiss()

          it.forEach { p ->
            ImageView(this@MainActivity).apply {
              setImageDrawable(ColorDrawable(p.color))
              binding.color.addView(this, LinearLayout.LayoutParams(0, 200).apply {
                weight = 1f
              })
            }
          }
        }
      }
    }
  }

  private fun startPalette(b: Bitmap) {
    binding.colorPalette.removeAllViews()
    val list = mutableListOf<Int>()
    Palette.from(b).generate {
      val light = it?.lightVibrantSwatch?.rgb
      val lm = it?.lightMutedSwatch?.rgb
      val d = it?.darkVibrantSwatch?.rgb
      val dm = it?.darkMutedSwatch?.rgb
      val ds = it?.dominantSwatch?.rgb
      val m = it?.mutedSwatch?.rgb

      light?.let {
        list.add(it)
      }
      lm?.let {
        list.add(it)
      }
      d?.let {
        list.add(it)
      }
      dm?.let {
        list.add(it)
      }
      ds?.let {
        list.add(it)
      }
      m?.let {
        list.add(it)
      }

      list.forEach {
        ImageView(this@MainActivity).apply {
          setImageDrawable(ColorDrawable(it))
          binding.colorPalette.addView(this, LinearLayout.LayoutParams(0, 200).apply {
            weight = 1f
          })
        }
      }
    }
  }

  private fun width() = resources.displayMetrics.widthPixels
}