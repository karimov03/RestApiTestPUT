package com.example.restapitest

import RvAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.restapitest.Servis.ApiClient
import com.example.restapitest.Servis.apiSerivs
import com.example.restapitest.databinding.ActivityMainBinding
import com.example.restapitest.databinding.ItemAddDialogBinding
import com.example.restapitest.databinding.ItemEditDialogBinding
import com.example.restapitest.models.Todo
import com.example.restapitest.models.TodoPost
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var adapter: RvAdapter
    private lateinit var single_user: Todo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.swipeRefreshLayout.setOnRefreshListener {
            LoadData()
        }

        LoadData()
        val itemAddDialogBinding = ItemAddDialogBinding.inflate(layoutInflater)
        val dialogview = itemAddDialogBinding.root
        val alertDialog = AlertDialog.Builder(this).setView(dialogview).create()

        binding.btnAdd.setOnClickListener {
            alertDialog.show()
        }
        itemAddDialogBinding.btnBekorQilish.setOnClickListener {
            alertDialog.hide()
        }
        itemAddDialogBinding.btnSaqlash.setOnClickListener {
            itemAddDialogBinding.apply {
                if (!edtName.text.isNullOrEmpty()) {
                    if (!edtNumber.text.isNullOrEmpty()) {
                        // Ish stoli
                        val todo = TodoPost(
                            edtName.text.toString(),
                            edtNumber.text.toString()
                        )
                        val api = ApiClient.getRetrofit().create(apiSerivs::class.java)
                        api.postRetrofit(todo).enqueue(object : Callback<Todo> {
                            override fun onResponse(call: Call<Todo>, response: Response<Todo>) {
                                LoadData()
                                alertDialog.hide()
                            }

                            override fun onFailure(call: Call<Todo>, t: Throwable) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Yuklashda xatolik",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })


                    } else {
                        edtNumber.hint = "Raqam kiritilmagan!"
                    }
                } else {
                    edtName.hint = "Ism kiritilmagan!"
                }
            }

        }


    }

    fun LoadData() {
        ApiClient.getRetrofit().create(apiSerivs::class.java).getResponse()
            .enqueue(object : Callback<List<Todo>> {
                override fun onResponse(
                    call: Call<List<Todo>>,
                    response: Response<List<Todo>>
                ) {
                    binding.layout1.visibility = View.GONE
                    binding.layout2.visibility = View.VISIBLE
                    val list = ArrayList<Todo>()
                    if (response.isSuccessful) {
                        if (!response.body().isNullOrEmpty()) {
                            response.body()?.let { list.addAll(it) }
                            adapter = RvAdapter(list, object : RvAdapter.rvAction {
                                override fun OnClick(list: ArrayList<Todo>, position: Int) {
                                    // edit qilish
                                    val user = list[position]
                                    single_user = user
                                    val itemeditdialogbinding =
                                        ItemEditDialogBinding.inflate(layoutInflater)
                                    val dialogview2 = itemeditdialogbinding.root
                                    val alertDialog2 =
                                        AlertDialog.Builder(this@MainActivity).setView(dialogview2)
                                            .create()
                                    itemeditdialogbinding.edtName.setText(user.name)
                                    itemeditdialogbinding.edtNumber.setText(user.phone)
                                    alertDialog2.show()
                                    itemeditdialogbinding.btnCansel.setOnClickListener {
                                        alertDialog2.hide()
                                    }
                                    itemeditdialogbinding.btnSave.setOnClickListener {
                                        val editedTodo = Todo(
                                            single_user.id,
                                            itemeditdialogbinding.edtName.text.toString(),
                                            itemeditdialogbinding.edtNumber.text.toString()
                                        )

                                        val api =
                                            ApiClient.getRetrofit().create(apiSerivs::class.java)
                                        api.putretrofit(single_user.phone, editedTodo)
                                            .enqueue(object : Callback<Todo> {
                                                override fun onResponse(
                                                    call: Call<Todo>,
                                                    response: Response<Todo>
                                                ) {
                                                    if (response.isSuccessful) {
                                                        alertDialog2.hide()
                                                        LoadData()
                                                    } else {
                                                        Toast.makeText(
                                                            this@MainActivity,
                                                            "O'zgartirishda xatolik",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }

                                                override fun onFailure(
                                                    call: Call<Todo>,
                                                    t: Throwable
                                                ) {
                                                    Toast.makeText(
                                                        this@MainActivity,
                                                        "Internetda xatolik",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            })
                                    }

                                }
                            })
                            binding.rv.adapter = adapter
                            binding.swipeRefreshLayout.isRefreshing = false

                            Toast.makeText(
                                this@MainActivity,
                                "Ma'lumotlar muvaffaqqiyatli yuklandi",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Ma'lumot topilmadi",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        binding.layout1.visibility = View.GONE
                        binding.layout2.visibility = View.VISIBLE


                        binding.swipeRefreshLayout.isRefreshing = false
                        Toast.makeText(
                            this@MainActivity,
                            "Ma'lumotni yuklashda xatolik",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<Todo>>, t: Throwable) {
                    binding.layout1.visibility = View.VISIBLE
                    binding.layout2.visibility = View.GONE
                    binding.swipeRefreshLayout.isRefreshing = false


                    Toast.makeText(
                        this@MainActivity,
                        "Internet aloqasida xatolik",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
    }
}