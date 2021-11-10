package com.example.comoconectarseainternet

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import com.example.comoconectarseainternet.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var urlList: List<String>
    private lateinit var progessBarList: List<ProgressBar>
    private lateinit var imageViewList: List<ImageView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageLoad()
        initProgessBar()
        initImageViews()

        binding.btnMessage.setOnClickListener {
            AlertDialog
                .Builder(this)
                .setMessage("Si puede ver este mensaje, se demuestra el funcionamiento de las \n corrutinas en background. " +
                        "Y las fotos son de www.bakenem.com")
                .setCancelable(true)
                .show()
        }

        CoroutineScope(Dispatchers.Main).launch {
            for (i in urlList.indices) {
                val image = doInBackground(urlList[i], progessBarList[i])
                Log.d("Corrutinas", image.toString())

                if (image != null) {
                    updateView(image, progessBarList[i], imageViewList[i])
                }
            }
        }

    }

    private fun imageLoad() {
        urlList = listOf<String>(
            "https://bakenem.com/storage/upImgRecetas/LCa1k3UFc5y5yYwajAg8ghqPyaHjGy1ygQl6m2HP.jpg",
            "https://bakenem.com/storage/upImgRecetas/asR2gAxxkPvJD6cppxiAgj9eAR38QgUTwvpDdzen.jpg",
            "https://bakenem.com/storage/upImgRecetas/AcdEz1Lc4i5igcEWTByqvAHtthHqjNlVjO27UA91.jpg",
            "https://bakenem.com/storage/upImgRecetas/TyytI8AG9Ctw7MoJXwHvqUPm9XL78UfrzpBBilBc.jpg"
        )
    }

    private fun initProgessBar() {
        progessBarList = listOf(binding.pBarImg1, binding.pBarImg2, binding.pBarImg3, binding.pBarImg4)
    }

    private fun initImageViews() {
        imageViewList = listOf(binding.img1, binding.img2, binding.img3, binding.img4)
    }

    private suspend fun doInBackground(url: String, progressBar: ProgressBar): Bitmap {
        lateinit var bmp: Bitmap
        withContext(Dispatchers.Default) {
            try {
                progressBar.visibility = View.VISIBLE
                val newURL = URL(url)
                val inputStram = newURL.openConnection().getInputStream()

                Log.d("Corrutinas", inputStram.toString())

                bmp = BitmapFactory.decodeStream(inputStram)
            } catch (e: Exception) {
                Log.d("Corrutinas", e.message.toString())
                e.printStackTrace()
            }
        }
        return bmp
    }

    private fun updateView(image: Bitmap, progressBar: ProgressBar, imageView: ImageView) {
        Log.d("Corrutinas", "Ahora se puede ver lo del updateView ")
        progressBar.visibility = View.GONE
        imageView.setImageBitmap(image)
    }
}