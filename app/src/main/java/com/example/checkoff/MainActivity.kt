package com.example.checkoff

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.checkoff.databinding.ActivityAddTaskBinding
import com.example.checkoff.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: AddDatabaseHelper
    private lateinit var taskAdaptor: TaskAdaptor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AddDatabaseHelper(this)
        taskAdaptor = TaskAdaptor(db.getAllTasks(), this)

        binding.TaskRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.TaskRecyclerView.adapter = taskAdaptor


        binding.addButton.setOnClickListener{
            val intent = Intent(this,AddTaskActivity::class.java)
            startActivity(intent)

        }
    }
    override fun onResume(){
        super.onResume()
        taskAdaptor.refreshData(db.getAllTasks())
    }
}