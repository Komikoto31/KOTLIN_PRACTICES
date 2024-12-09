package com.example.pr10

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageDownloaderApp(context = this)
        }
        // Запускаем WorkManager для очистки старых изображений
        val cleanupRequest = PeriodicWorkRequestBuilder<CleanupWorker>(1, TimeUnit.DAYS).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "ImageCleanupWork",
            ExistingPeriodicWorkPolicy.KEEP,
            cleanupRequest
        )

    }
}
fun loadSavedImages(context: Context): List<Bitmap?> {
    val preferences = context.getSharedPreferences("ImagePrefs", Context.MODE_PRIVATE)
    val savedImages = preferences.getStringSet("saved_images", setOf()) ?: setOf()
    return savedImages.map { path ->
        BitmapFactory.decodeFile(path)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageDownloaderApp(context: Context) {
    var urlText by remember { mutableStateOf(TextFieldValue("")) }
    var imageList by remember { mutableStateOf(loadSavedImages(context)) }
    var currentScreen by remember { mutableStateOf("Загрузчик изображений") }
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    // Используем ModalNavigationDrawer для бокового меню
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Навигация", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    currentScreen = "Загрузчик изображений"
                    CoroutineScope(Dispatchers.Main).launch { drawerState.close() }
                }) {
                    Text("Загрузчик изображений")
                }
                Button(onClick = {
                    currentScreen = "Галерея"
                    CoroutineScope(Dispatchers.Main).launch { drawerState.close() }
                }) {
                    Text("Галерея")
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(currentScreen) },
                    navigationIcon = {
                        IconButton(onClick = { CoroutineScope(Dispatchers.Main).launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Меню")
                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar {
                    Button(onClick = { currentScreen = "Загрузчик изображений" }) {
                        Text("Загрузить")
                    }
                    Button(onClick = { currentScreen = "Галерея" }) {
                        Text("Галерея")
                    }
                }
            }
        ) { paddingValues ->
            // Отображаем контент в зависимости от текущего экрана
            when (currentScreen) {
                "Загрузчик изображений" -> ImageDownloaderScreen(
                    context = context,
                    urlText = urlText,
                    onUrlChange = { urlText = it },
                    imageList = imageList,
                    onImageDownloaded = { bitmap ->
                        imageList = imageList + bitmap
                        CoroutineScope(Dispatchers.IO).launch {
                            saveImageToDisk(bitmap, context, urlText.text)
                        }
                    },
                    modifier = Modifier.padding(paddingValues)
                )
                "Галерея" -> GalleryScreen(
                    imageList = imageList,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}


@Composable
fun ImageDownloaderScreen(
    context: Context,
    urlText: TextFieldValue,
    onUrlChange: (TextFieldValue) -> Unit,
    imageList: List<Bitmap?>,
    onImageDownloaded: (Bitmap) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = urlText,
            onValueChange = onUrlChange,
            label = { Text("Введите URL изображения") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = {
            val imageUrl = urlText.text
            CoroutineScope(Dispatchers.Main).launch {
                val bitmap = downloadImage(imageUrl)
                if (bitmap != null) {
                    onImageDownloaded(bitmap)
                } else {
                    Toast.makeText(context, "Ошибка загрузки изображения", Toast.LENGTH_SHORT).show()
                }
            }
        }) {
            Text("Загрузить изображение")
        }
    }
}

@Composable
fun GalleryScreen(imageList: List<Bitmap?>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(imageList) { bitmap ->
            bitmap?.let {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

suspend fun downloadImage(imageUrl: String): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            val url = URL(imageUrl)
            val connection = url.openConnection()
            connection.doInput = true
            connection.connect()
            val input = connection.getInputStream()
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

suspend fun saveImageToDisk(bitmap: Bitmap, context: Context, imageUrl: String) {
    withContext(Dispatchers.IO) {
        try {
            val extension = when {
                imageUrl.endsWith(".png", ignoreCase = true) -> "png"
                imageUrl.endsWith(".jpg", ignoreCase = true) || imageUrl.endsWith(".jpeg", ignoreCase = true) -> "jpg"
                else -> "jpg"
            }

            val file = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "downloaded_image_$extension"
            )

            FileOutputStream(file).use { outputStream ->
                val format = if (extension == "png") Bitmap.CompressFormat.PNG else Bitmap.CompressFormat.JPEG
                bitmap.compress(format, 100, outputStream)
                outputStream.flush()
            }

            // Сохранение пути к изображению в SharedPreferences
            val preferences = context.getSharedPreferences("ImagePrefs", Context.MODE_PRIVATE)
            val editor = preferences.edit()
            val savedImages = preferences.getStringSet("saved_images", mutableSetOf()) ?: mutableSetOf()
            savedImages.add(file.absolutePath)
            editor.putStringSet("saved_images", savedImages)
            editor.apply()

            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Изображение сохранено", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Ошибка сохранения изображения", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

class CleanupWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    private val preferences = appContext.getSharedPreferences("CleanupPrefs", Context.MODE_PRIVATE)
    private val lastCleanupKey = "last_cleanup_time"

    override fun doWork(): Result {
        val currentTime = System.currentTimeMillis()
        val lastCleanupTime = preferences.getLong(lastCleanupKey, 0)

        // Проверяем, прошло ли 24 часа с последней очистки
        if (currentTime - lastCleanupTime >= TimeUnit.DAYS.toMillis(1)) {
            val imagesDir = applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            imagesDir?.listFiles()?.forEach { file ->
                if (file.isFile && file.name.startsWith("downloaded_image")) {
                    file.delete()
                }
            }
            // Обновляем время последней очистки
            preferences.edit().putLong(lastCleanupKey, currentTime).apply()
        }
        return Result.success()
    }
}
