package com.example.polito_mad_01.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.AndroidViewModel
import com.example.polito_mad_01.R
import com.example.polito_mad_01.viewmodel.TestViewModel
import org.w3c.dom.Text

class TestActivity : AppCompatActivity() {

    private val vm by viewModels<TestViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        findViewById<Button>(R.id.addButton).setOnClickListener {
            vm.addUser()
        }

        findViewById<Button>(R.id.getButton).setOnClickListener {
            findViewById<TextView>(R.id.myText).text = vm.getUser()
        }

    }
}