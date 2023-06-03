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
import com.example.polito_mad_01.adapters.EditProfilePageAdapter
import com.google.android.material.tabs.TabLayout
import com.example.polito_mad_01.viewmodel.*
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.TextInputLayout
import java.util.*


class EditProfileContainer : Fragment(R.layout.fragment_edit_profile_container) {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var editProfilePageAdapter: EditProfilePageAdapter
    private lateinit var mView: View

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

    }

    private fun trySaveData(): Boolean {
        return try {
            saveAllView()
            isNotValid()
            vm.updateUser()
            findNavController().navigate(R.id.showProfileContainer)
            Snackbar.make(requireView(), "Profile updated successfully.", Snackbar.LENGTH_LONG).show()
            true
        } catch (e: Exception) {
            Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
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

        //imageUri?.let { user.image_uri = it.toString() }

    }

    private fun fieldIsValid(field: String?, fieldName: String) {
        if (field.isNullOrEmpty())
            throw Exception("$fieldName is invalid")
    }

    private fun saveAllView() {
        val nameInputLayout = mView.findViewById<TextInputLayout>(R.id.nameInputLayout)
        val surnameInputLayout = mView.findViewById<TextInputLayout>(R.id.surnameInputLayout)
        val locationInputLayout = mView.findViewById<TextInputLayout>(R.id.locationInputLayout)
        val nicknameInputLayout = mView.findViewById<TextInputLayout>(R.id.nicknameInputLayout)
        val achievementsInputLayout = mView.findViewById<TextInputLayout>(R.id.achievementsInputLayout)
        val genderInputLayout = mView.findViewById<TextInputLayout>(R.id.genderInputLayout)
        val birthdayInputLayout = mView.findViewById<TextInputLayout>(R.id.birthdayInputLayout)
        val phoneNumberInputLayout = mView.findViewById<TextInputLayout>(R.id.phonenumberInputLayout)
        val emailInputLayout = mView.findViewById<TextInputLayout>(R.id.emailInputLayout)
        vm.user.value?.name = nameInputLayout.editText?.text.toString()
        vm.user.value?.surname = surnameInputLayout.editText?.text.toString()
        vm.user.value?.nickname = nicknameInputLayout.editText?.text.toString()
        vm.user.value?.gender = genderInputLayout.editText?.text.toString()
        vm.user.value?.location = locationInputLayout.editText?.text.toString()
        vm.user.value?.achievements = listOf( achievementsInputLayout.editText?.text.toString() )
        vm.user.value?.birthdate = birthdayInputLayout.editText?.text.toString()
        vm.user.value?.phoneNumber = phoneNumberInputLayout.editText?.text.toString()
        vm.user.value?.email = emailInputLayout.editText?.text.toString()
    }
}