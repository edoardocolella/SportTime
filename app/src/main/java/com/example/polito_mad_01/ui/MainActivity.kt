package com.example.polito_mad_01.ui

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.*
import androidx.core.os.bundleOf
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import com.example.polito_mad_01.*
import com.example.polito_mad_01.viewmodel.*
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

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

        val parentLayout: View = findViewById(android.R.id.content)

        val navController = (supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment).navController

        if(intent.getBooleanExtra("friendRequests", false))
                navController.navigate(R.id.showProfileContainer, bundleOf("goto" to "friendRequests"))
        else if (intent.getBooleanExtra("gameRequests", false))
            navController.navigate(R.id.invitationContainer, bundleOf("goto" to "gameRequests"))

        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.navView)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)

        FirebaseApp.initializeApp(this)

        auth.currentUser?.let {
            vm.subscribeToNotifications()
            var showSnackbar = true

            vm.getUser().observe(this) { user ->
                if(user == null) return@observe // TODO: replace

                if(showSnackbar){
                    Snackbar.make(parentLayout, "Welcome back ${user.name}", Snackbar.LENGTH_LONG).show()
                    showSnackbar = false
                }

                val view = navView.getHeaderView(0)
                val nameSurname = "${user.name} ${user.surname}"
                view.findViewById<TextView>(R.id.nameNav).text = nameSurname
                view.findViewById<TextView>(R.id.usernameNav).text = user.nickname
            }
        }

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.profilePage -> navController.navigate(R.id.showProfileContainer)
                R.id.reservationsPage -> navController.navigate(R.id.reservationsFragment)
                R.id.browsePage -> navController.navigate(R.id.browseFragment)
                R.id.oldReservations -> navController.navigate(R.id.showOldReservations)
                R.id.invitations -> navController.navigate(R.id.invitationContainer)
                R.id.findFriends -> navController.navigate(R.id.findFriendsWithFilters)
                R.id.logout -> {
                    vm.logout()
                    auth.signOut()
                    val intent = Intent(this, LandingPageActivity::class.java)
                    startActivity(intent)
                }
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    fun setTitle(title: String){
        supportActionBar?.title = title
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}