package com.example.roomdatabase


import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdatabase.adapter.UserAdapter
import com.example.roomdatabase.data.AppDatabase
import com.example.roomdatabase.data.entity.User


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnTambah: Button
    private lateinit var database: AppDatabase
    private lateinit var userAdapter: UserAdapter
    private var list: MutableList<User> = ArrayList()
    private lateinit var dialog: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recycler_view)
        btnTambah = findViewById(R.id.btn_tambah)

        database = AppDatabase.getInstance(applicationContext)
        list.clear()
        list.addAll(database.userDao().getAll())
        userAdapter = UserAdapter(applicationContext, list)
        userAdapter.setDialog(object : UserAdapter.Dialog {
            override fun onClick(position: Int) {
                val dialogItem = arrayOf<CharSequence>("Edit", "Hapus")
                dialog = AlertDialog.Builder(this@MainActivity)
                dialog.setItems(dialogItem) { dialogInterface: DialogInterface, i: Int ->
                    when (i) {
                        0 -> {
                            val intent = Intent(this@MainActivity, TambahActivity::class.java)
                            intent.putExtra("uid", list[position].uid)
                            startActivity(intent)
                        }
                        1 -> {
                            val user = list[position]
                            database.userDao().delete(user)
                            onStart()
                        }
                    }
                }
                dialog.show()
            }
        })

        val layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = userAdapter

        btnTambah.setOnClickListener {
            startActivity(Intent(this@MainActivity, TambahActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        list.clear()
        list.addAll(database.userDao().getAll())
        userAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        // Update checkbox status based on data in database when resuming the activity
        updateCheckboxStatus()
        // Update spinner selection based on data in database when resuming the activity
        updateSpinnerSelection()
    }

    private fun updateCheckboxStatus() {
        val currentUser = list.firstOrNull { it.uid == intent.getIntExtra("uid", -1) }
        currentUser?.let {
            findViewById<CheckBox>(R.id.checkBox_membaca).isChecked = it.hobby.contains("Membaca")
            findViewById<CheckBox>(R.id.checkBox_menulis).isChecked = it.hobby.contains("Menulis")
            findViewById<CheckBox>(R.id.checkBox_nonton).isChecked = it.hobby.contains("Nonton")
            findViewById<CheckBox>(R.id.checkBox_jalan_jalan).isChecked = it.hobby.contains("Jalan-jalan")
            findViewById<CheckBox>(R.id.checkBox_main_game).isChecked = it.hobby.contains("Main game")
        }
    }
    private fun updateSpinnerSelection() {
        val currentUser = list.firstOrNull { it.uid == intent.getIntExtra("uid", -1) }
        currentUser?.let {
            val spinner = findViewById<Spinner>(R.id.spinner_jurusan)
            val jurusanArray = resources.getStringArray(R.array.jurusan_options)
            val jurusanIndex = jurusanArray.indexOf(currentUser.jurusan)
            spinner.setSelection(jurusanIndex)
        }
    }
}