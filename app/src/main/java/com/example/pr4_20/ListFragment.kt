package com.example.pr4_20

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class ListFragment : Fragment() {
    // Компонент списка
    private lateinit var recyclerView: RecyclerView
    // Адаптер, который управляет отображением элементов списка
    private lateinit var photoListAdapter: PhotoListAdapter
    // Список строк, которые будут отображены
    private lateinit var photoList: MutableList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Загрузка данных из файла
        photoList = loadPhotoList()
        //Создание адаптера
        photoListAdapter = PhotoListAdapter(photoList)
        //Привязывает адаптер к RecyclerView, чтобы начать отображение списка.
        recyclerView.adapter = photoListAdapter

        return view
    }

    private fun loadPhotoList(): MutableList<String> {
        val file = File(requireContext().filesDir, "photos/data")
        val list = mutableListOf<String>()
        if (file.exists()) {
            file.forEachLine { list.add(it) }
        }
        return list
    }
}