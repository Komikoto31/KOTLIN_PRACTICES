package com.example.pr4_20


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraFragment : Fragment() {
    // Этот объект будет управлять фоновыми задачами, связанными с камерой
    private lateinit var cameraExecutor: ExecutorService
    // Этот объект будет представлять элемент, на который выводится камера
    private lateinit var viewFinder: PreviewView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_camera, container, false)
        // Создаем исполнителя
        cameraExecutor = Executors.newSingleThreadExecutor()
        viewFinder = view.findViewById(R.id.previewView)

        //Проверка на предоставленные разрешения
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(requireActivity(),
                REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }


        //Сохранение текущей даты
        val takePhotoButton: Button = view.findViewById(R.id.takePhotoButton)
        takePhotoButton.setOnClickListener {
            saveCurrentDate()
        }

        //Отображение списка сохранненых дат
        val showListButton: Button = view.findViewById(R.id.showListButton)
        showListButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ListFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    private fun startCamera() {
        //Получает экземпляр провайдера камеры для работы с камерой
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({ //Добавляет слушателя, который выполняется после получения провайдера
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also { //Устанавливает, куда будет выводиться изображение с камеры
                it.setSurfaceProvider(viewFinder.surfaceProvider)
            }
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview) //Привязывает работу камеры к жизненному циклу фрагмента
            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    //Проверяет, были ли предоставлены все разрешения
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun saveCurrentDate() {
        //Получаем текущее время
        val currentTime = System.currentTimeMillis()
        //Форматируем текущее время в удобную строку
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(currentTime)

        val dir = File(requireContext().filesDir, "photos")
        //Если папки не существует, создает ее
        if (!dir.exists()) {
            dir.mkdir()
        }

        val file = File(dir, "data")
        file.appendText("$date\n")
        Toast.makeText(requireContext(), "Date saved: $date", Toast.LENGTH_SHORT).show()
    }

    companion object {
        //REQUEST_CODE_PERMISSIONS: Код, который будет использоваться при запросе разрешений.
        private const val REQUEST_CODE_PERMISSIONS = 10
        //REQUIRED_PERMISSIONS: Массив, содержащий необходимые разрешения
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}