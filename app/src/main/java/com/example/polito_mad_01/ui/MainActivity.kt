package com.example.polito_mad_01.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.*
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import com.example.polito_mad_01.*
import com.example.polito_mad_01.repositories.UserRepository
import com.example.polito_mad_01.viewmodel.*
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    private val auth = FirebaseAuth.getInstance()

    private val vm: MainActivityViewModel by viewModels {
        MainActivityViewModelFactory((application as SportTimeApplication).userRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.navView)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)

        FirebaseApp.initializeApp(this)

        // NOTE: currentUser deve esistere a questo punto, altrimenti qualcosa non va
        auth.currentUser?.let {
            vm.subscribeToNotifications()
            vm.getUser().observe(this) { user ->
                if(user == null) return@observe // TODO: replace

                val view = navView.getHeaderView(0)
                val nameSurname = "${user.name} ${user.surname}"
                view.findViewById<TextView>(R.id.nameNav).text = nameSurname
                view.findViewById<TextView>(R.id.usernameNav).text = user.nickname
            }
        }

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val navController = (supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment).navController

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.profilePage -> {
                    supportActionBar?.title = "Profile"
                    navController.navigate(R.id.showProfileContainer)
                }
                R.id.reservationsPage -> {
                    supportActionBar?.title = "Reservations"
                    navController.navigate(R.id.reservationsFragment)
                }
                R.id.browsePage -> {
                    supportActionBar?.title = "Free slots"
                    navController.navigate(R.id.browseFragment)
                }
                R.id.oldReservations -> {
                    supportActionBar?.title = "Past Reservations"
                    navController.navigate(R.id.showOldReservations)
                }
                R.id.gameInvitation -> {
                    supportActionBar?.title = "Game invitation"
                    navController.navigate(R.id.gameInvitation)
                }
                R.id.logout -> {
                    auth.signOut()
                    vm.logout()
                    val intent = Intent(this, LandingPageActivity::class.java)
                    startActivity(intent)
                }
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}