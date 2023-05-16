package com.example.polito_mad_01.ui

import android.icu.text.SimpleDateFormat
import android.net.ParseException
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.*
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.polito_mad_01.*
import com.example.polito_mad_01.adapters.ShowProfilePageAdapter
import com.google.android.material.tabs.TabLayout
import com.example.polito_mad_01.viewmodel.*
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*


class EditProfileContainer : Fragment(R.layout.fragment_show_profile_container) {

    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager2
    lateinit var showProfilePageAdapter: ShowProfilePageAdapter

    private val vm: EditProfileViewModel by viewModels {
        EditProfileViewModelFactory((activity?.application as SportTimeApplication).userRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            setHasOptionsMenu(true)
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_edit_profile, menu)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_save_profile) {
            return trySaveData()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager = view.findViewById(R.id.reservationsViewPager)
        showProfilePageAdapter = ShowProfilePageAdapter(requireActivity(), vm)
        viewPager.adapter = showProfilePageAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tabLayout.visibility = View.VISIBLE
            when(position){
                0 -> tab.text = "Profile"
                1 -> tab.text = "Skills"
            }
        }.attach()

    }

    private fun trySaveData(): Boolean {
        return try {
            isNotValid()
            vm.updateUser()
            findNavController().navigate(R.id.action_editProfileContainer_to_profileFragment)
            true
        } catch (e: Exception) {
            Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
            false
        }
    }


    private fun isNotValid() {
        val user = vm.user.value!!.user
        fieldIsValid(user.name, "Full Name")
        fieldIsValid(user.nickname, "Nickname")
        fieldIsValid(user.description, "Description")
        fieldIsValid(user.email, "Email")
        fieldIsValid(user.phoneNumber, "Phone Number")
        fieldIsValid(user.location, "Location")
        fieldIsValid(user.birthdate, "BirthDate")

        val regexMail = Regex("^[A-Za-z\\d+_.-]+@(.+)\$")
        if (!regexMail.matches(user.email)) {
            throw Exception("invalid email format")
        }

        try {
            val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            formatter.parse(user.birthdate)
        } catch (e: ParseException) {
            throw Exception("Birthdate should be in dd-MM-yyyy format")
        }

        val regexPhone = Regex("^\\d{10}\$")
        if (!regexPhone.matches(user.phoneNumber)) {
            throw Exception("Phone number should be a 10 digit number")
        }

        //imageUri?.let { user.image_uri = it.toString() }

    }

    private fun fieldIsValid(field: String?, fieldName: String) {
        if (field.isNullOrEmpty())
            throw Exception("$fieldName is invalid")
    }
}