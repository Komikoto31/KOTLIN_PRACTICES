package com.example.pr7

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileOutputStream
import java.net.MalformedURLException
import java.net.URL

class MainActivity : AppCompatActivity() {

    lateinit var imageView: ImageView
    lateinit var downloadButton: Button
    lateinit var urlEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)
        downloadButton = findViewById(R.id.downloadButton)
        urlEditText = findViewById(R.id.urlEditText)

        downloadButton.setOnClickListener {
            val imageUrl = urlEditText.text.toString()
            downloadAndSaveImage(imageUrl)
        }
    }

    fun downloadAndSaveImage(url: String) {
        // Запускаем корутину в основном потоке
        CoroutineScope(Dispatchers.Main).launch {
            // Скачивание изображения происходит в фоновом потоке (Dispatchers.IO)
            val bitmap = withContext(Dispatchers.IO) {
                loadImageFromNetwork(url)
            }

            // Когда загрузка завершена, изображение отображается на экране
            bitmap?.let {
                imageView.setImageBitmap(it)
                saveImageToDisk(it) // Сохранение изображения
            }
        }
    }

    fun loadImageFromNetwork(url: String): Bitmap? {

        return try {
            // Открытие сетевого соединения и загрузка изображения
            val imageUrl = URL(url)
            BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream())
        }
            catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    fun saveImageToDisk(bitmap: Bitmap) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val filename = "downloaded_image.png"
                // Открытие потока для записи файла во внутреннюю память
                val fileOutputStream: FileOutputStream = openFileOutput(filename, MODE_PRIVATE)
                // Сохранение изображения в формате PNG
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
                fileOutputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}