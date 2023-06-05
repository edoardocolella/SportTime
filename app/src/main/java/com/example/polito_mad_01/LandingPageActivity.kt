package com.example.polito_mad_01

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.*
import com.example.polito_mad_01.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth

class LandingPageActivity : AppCompatActivity() {

    private lateinit var backCallBack: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        backCallBack = onBackPressedDispatcher
            .addCallback(this) {showExitDialog()}
        backCallBack.isEnabled = true

        val auth = FirebaseAuth.getInstance()
        if(auth.currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
            || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
            || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
        ) {
            val permission =
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(permission, 112)
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED) {
                val permission =
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS)
                requestPermissions(permission, 112)
            }
        }


        findViewById<Button>(R.id.registerButton).setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            backCallBack.remove()
            startActivity(intent)
        }

        findViewById<Button>(R.id.signInButton).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            backCallBack.remove()
            startActivity(intent)
        }
    }

    private fun showExitDialog() {
        AlertDialog.Builder(this)
            .setTitle("Do you want to exit?")
            .setMessage("Are you sure?")
            .setPositiveButton("YES")
            { _, _ -> this.finishAffinity()}
            .setNegativeButton("NO") { _, _ -> }.show()
    }

}