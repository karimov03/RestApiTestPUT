package com.example.restapitest.Servis

import com.example.restapitest.models.Todo
import com.example.restapitest.models.TodoPost
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface apiSerivs {
    @GET("contacts")
    fun getResponse():retrofit2.Call<List<Todo>>
    @POST("contacts/")
    fun postRetrofit(@Body todoPost: TodoPost):retrofit2.Call<Todo>
    @PUT("application/json/{phone}")
    fun putretrofit(@Path("phone") id: String, @Body todoPost: Todo): Call<Todo>

}