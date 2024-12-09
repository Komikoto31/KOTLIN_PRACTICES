package com.example.pr4_20

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PhotoListAdapter(private val photoList: List<String>) : RecyclerView.Adapter<PhotoListAdapter.PhotoViewHolder>() {

    // Это класс, который хранит ссылки на виджеты (например, TextView), используемые для отображения данных
    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photoTextView: TextView = itemView.findViewById(R.id.photoTextView)
    }


    // Создаёт новый элемент списка (элемент UI)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return PhotoViewHolder(itemView)
    }

    // Заполняет элемент списка данными
    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.photoTextView.text = photoList[position]
    }

    // Возвращает количество элементов в списке
    override fun getItemCount() = photoList.size
}