package com.example.polito_mad_01.ui

import android.app.AlertDialog
import android.icu.text.SimpleDateFormat
import android.net.ParseException
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.*
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.polito_mad_01.*
import com.example.polito_mad_01.adapters.EditProfilePageAdapter
import com.google.android.material.tabs.TabLayout
import com.example.polito_mad_01.viewmodel.*
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*


class EditProfileContainer : Fragment(R.layout.fragment_edit_profile_container) {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var editProfilePageAdapter: EditProfilePageAdapter
    private lateinit var mView: View
    private lateinit var backCallBack: OnBackPressedCallback


    private val vm: EditProfileViewModel by viewModels {
        EditProfileViewModelFactory((activity?.application as SportTimeApplication).userRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            setHasOptionsMenu(true)

    }

    override fun onStop() {
        super.onStop()
        backCallBack.remove()
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
        mView = view
        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager = view.findViewById(R.id.reservationsViewPager)
        editProfilePageAdapter = EditProfilePageAdapter(requireActivity(), vm)
        viewPager.adapter = editProfilePageAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tabLayout.visibility = View.VISIBLE
            when(position){
                0 -> tab.text = "Profile"
                1 -> tab.text = "Skills"
            }
        }.attach()


        backCallBack = requireActivity().onBackPressedDispatcher
            .addCallback(this) {
                showExitDialog()
            }
        backCallBack.isEnabled = true
    }

    private fun trySaveData(): Boolean {
        return try {
            isNotValid()
            vm.updateUser()
            findNavController().navigate(R.id.showProfileContainer)
            Snackbar.make(requireView(), "Profile updated successfully.", Snackbar.LENGTH_LONG).show()
            true
        } catch (e: Exception) {
            Snackbar.make(requireView(), "An error has occurred", Snackbar.LENGTH_LONG).show()
            println(e.message)
            false
        }
    }

    private fun isNotValid() {
        val user = vm.user.value!!
        fieldIsValid(user.name, "Full Name")
        fieldIsValid(user.nickname, "Nickname")
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

    }

    private fun fieldIsValid(field: String?, fieldName: String) {
        if (field.isNullOrEmpty())
            throw Exception("$fieldName is invalid")
    }

    private fun showExitDialog(): Boolean {
        AlertDialog.Builder(activity)
            .setTitle("Are you sure?").setMessage("All changes will be lost")
            .setPositiveButton("YES")
            { _, _ -> findNavController().navigate(R.id.showProfileContainer) }
            .setNegativeButton("NO") { _, _ -> }.show()
        return true
    }
}