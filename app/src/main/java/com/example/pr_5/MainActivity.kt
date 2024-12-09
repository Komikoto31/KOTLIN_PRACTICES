package com.example.pr_5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.pr_5.ProductApi
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var db: AppDatabase

    @Inject
    lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tv = findViewById<TextView>(R.id.tv)
        val b = findViewById<Button>(R.id.button)
        val buttonList = findViewById<Button>(R.id.buttonList)

        //Инициализация локальной базы данных
        db = AppDatabase.getDatabase(this)


        val productApi = retrofit.create(ProductApi::class.java)

        b.setOnClickListener {
            //Используется для запуска фоновой задачи в потоке ввода-вывода (IO), чтобы не блокировать основной поток.
            CoroutineScope(Dispatchers.IO).launch {
                //Выполняет запрос к API для получения информации о продукте с id = 2
                val product = productApi.getProductById(2)

                // Сохранение в базу данных
                val productEntity = Product(
                    id = product.id,
                    title = product.title,
                    description = product.description,
                    price = product.price,
                    discountPercentage = product.discountPercentage,
                    rating = product.rating,
                    stock = product.stock,
                    brand = product.brand,
                    category = product.category,
                    thumbnail = product.thumbnail,
                    availabilityStatus = product.availabilityStatus,
                    dimensions = product.dimensions,
                    images = product.images,
                    meta = product.meta,
                    minimumOrderQuantity = product.minimumOrderQuantity,
                    returnPolicy = product.returnPolicy,
                    reviews = product.reviews,
                    shippingInformation = product.shippingInformation,
                    sku = product.sku,
                    tags = product.tags,
                    warrantyInformation = product.warrantyInformation,
                    weight = product.weight)
                //Сохранения объекта в локальную бд
                db.productDao().insertProduct(productEntity)


                //Обновление пользовательского интерфейса
                runOnUiThread {
                    tv.text = product.title
                }
            }
        }
        buttonList.setOnClickListener {
            startActivity(Intent(this, ProductListActivity::class.java))
        }

    }
}
