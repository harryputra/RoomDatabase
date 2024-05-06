package com.example.roomdatabase

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.roomdatabase.data.AppDatabase
import com.example.roomdatabase.data.entity.User

class TambahActivity : AppCompatActivity() {
    private lateinit var editName: EditText
    private lateinit var editEmail: EditText
    private lateinit var btnSave: Button
    private lateinit var database: AppDatabase
    private var uid = 0
    private var isEdit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah)
        editName = findViewById(R.id.full_name)
        editEmail = findViewById(R.id.email)
        btnSave = findViewById(R.id.btn_save)

        database = AppDatabase.getInstance(applicationContext)

        val intent = intent
        uid = intent.getIntExtra("uid", 0)
        isEdit = uid > 0
        if (isEdit) {
            val user = database.userDao().getUserById(uid)
            editEmail.setText(user.email)
            editName.setText(user.fullName)
        }

        btnSave.setOnClickListener {
            val name = editName.text.toString()
            val email = editEmail.text.toString()

            if (isEdit) {
                val user = User(uid, name, email) // Membuat objek User untuk update
                database.userDao().update(user)
            } else {
                val user = User(0, name, email) // Membuat objek User untuk insert
                database.userDao().insertAll(user)
            }
            finish()
        }
    }
}