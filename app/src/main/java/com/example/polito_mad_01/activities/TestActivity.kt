package com.example.polito_mad_01.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.AndroidViewModel
import com.example.polito_mad_01.R
import com.example.polito_mad_01.viewmodel.TestViewModel

class TestActivity : AppCompatActivity() {

    val vm by viewModels<TestViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        print(vm.getUser())
    }
}