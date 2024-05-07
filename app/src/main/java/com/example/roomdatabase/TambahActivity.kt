package com.example.roomdatabase

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.roomdatabase.adapter.SpinnerJurusanAdapter
import com.example.roomdatabase.data.AppDatabase
import com.example.roomdatabase.data.entity.User

class TambahActivity : AppCompatActivity() {
    private lateinit var editName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPhone: EditText
    private lateinit var btnSave: Button
    private lateinit var spinnerJurusan: Spinner
    private lateinit var database: AppDatabase
    private var uid = 0
    private var isEdit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah)
        editName = findViewById(R.id.full_name)
        editEmail = findViewById(R.id.email)
        editPhone = findViewById(R.id.phone)
        btnSave = findViewById(R.id.btn_save)
        spinnerJurusan = findViewById(R.id.spinner_jurusan)

        database = AppDatabase.getInstance(applicationContext)

        // Daftar jurusan
        val jurusanList = listOf("TRO", "TRMO", "TRIN")

        // Inisialisasi adapter untuk spinner jurusan
        val adapter = SpinnerJurusanAdapter(this, android.R.layout.simple_spinner_item, jurusanList)

        // Set adapter ke spinner
        spinnerJurusan.adapter = adapter

        val intent = intent
        uid = intent.getIntExtra("uid", 0)
        isEdit = uid > 0
        if (isEdit) {
            val user = database.userDao().getUserById(uid)
            editEmail.setText(user.email)
            editName.setText(user.fullName)
            editPhone.setText(user.notelpon)
            setCheckedHobbies(user.hobby)
            setSpinnerSelection(user.jurusan)
        }

        btnSave.setOnClickListener {
            val name = editName.text.toString()
            val email = editEmail.text.toString()
            val phone = editPhone.text.toString()
            val hobby = StringBuilder()

            // Menambahkan hobi yang dipilih ke dalam string
            if (findViewById<CheckBox>(R.id.checkBox_membaca).isChecked) {
                hobby.append("Membaca, ")
            }
            if (findViewById<CheckBox>(R.id.checkBox_menulis).isChecked) {
                hobby.append("Menulis, ")
            }
            if (findViewById<CheckBox>(R.id.checkBox_nonton).isChecked) {
                hobby.append("Nonton, ")
            }
            if (findViewById<CheckBox>(R.id.checkBox_jalan_jalan).isChecked) {
                hobby.append("Jalan-jalan, ")
            }
            if (findViewById<CheckBox>(R.id.checkBox_main_game).isChecked) {
                hobby.append("Main game")
            }

            val jurusan = spinnerJurusan.selectedItem.toString() // Mendapatkan jurusan dari Spinner

            val user = if (isEdit) {
                User(uid, name, email, phone, hobby.toString(), jurusan) // Membuat objek User untuk update
            } else {
                User(0, name, email, phone, hobby.toString(), jurusan) // Membuat objek User untuk insert
            }
            database.userDao().apply {
                if (isEdit) {
                    update(user)
                } else {
                    insertAll(user)
                }
            }
            finish()
        }
    }

    private fun setCheckedHobbies(hobbies: String) {
        val hobbyList = hobbies.split(", ")
        hobbyList.forEach { hobby ->
            when (hobby) {
                "Membaca" -> findViewById<CheckBox>(R.id.checkBox_membaca).isChecked = true
                "Menulis" -> findViewById<CheckBox>(R.id.checkBox_menulis).isChecked = true
                "Nonton" -> findViewById<CheckBox>(R.id.checkBox_nonton).isChecked = true
                "Jalan-jalan" -> findViewById<CheckBox>(R.id.checkBox_jalan_jalan).isChecked = true
                "Main game" -> findViewById<CheckBox>(R.id.checkBox_main_game).isChecked = true
            }
        }
    }
    private fun setSpinnerSelection(jurusan: String) {
        val adapter = spinnerJurusan.adapter
        if (adapter is SpinnerJurusanAdapter) {
            val position = adapter.getPosition(jurusan)
            if (position != -1) {
                spinnerJurusan.setSelection(position)
            }
        }
    }
}
