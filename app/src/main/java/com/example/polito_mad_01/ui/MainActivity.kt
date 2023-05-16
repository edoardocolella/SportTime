package com.example.polito_mad_01.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.*
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import com.example.polito_mad_01.*
import com.example.polito_mad_01.viewmodel.*
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    private val vm: MainActivityViewModel by viewModels {
        MainActivityViewModelFactory((application as SportTimeApplication).userRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = Firebase.firestore
         db.collection("users").document("1").get().addOnSuccessListener {
             println("TEST ${it.data}")
         }

        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
            || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
            || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
        ) {
            val permission =
                arrayOf(Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(permission, 112)
        }

        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.navView)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)


        vm.getUser(1).observe(this) {
            val view = navView.getHeaderView(0)
            val nameSurname = "${it.name} ${it.surname}"
            view.findViewById<TextView>(R.id.nameNav).text = nameSurname
            view.findViewById<TextView>(R.id.usernameNav).text = it.nickname
        }

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val navController = (supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment).navController

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.profilePage -> {
                    navController.navigate(R.id.profileFragment)
                }
                R.id.reservationsPage -> {
                    navController.navigate(R.id.reservationsFragment)
                }
                R.id.browsePage -> {
                    navController.navigate(R.id.browseFragment)
                }
                R.id.oldReservations -> {
                    navController.navigate(R.id.showOldReservations)
                }
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            true
        }

        return super.onOptionsItemSelected(item)
    }
}