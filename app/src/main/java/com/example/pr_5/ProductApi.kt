package com.example.pr_5

import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApi{
    //HTTP-аннотация Retrofit, которая указывает, что метод будет делать GET-запрос на сервер
    @GET("products/{id}")
    //объявление асинхронного метода
    suspend fun getProductById(@Path("id") id: Int): Product
}