package com.example.polito_mad_01.ui

import android.os.Bundle
import android.view.*
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.polito_mad_01.*
import com.example.polito_mad_01.viewmodel.*

class ShowProfile : Fragment(R.layout.fragment_profile) {

    private val vm: ShowProfileViewModel by viewModels {
        ShowProfileViewModelFactory((activity?.application as SportTimeApplication).userRepository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            setAllView()
        } catch (e: NotImplementedError) {
            e.printStackTrace()
        }
    }

    private fun setAllView() {
        vm.getUser(1).observe(viewLifecycleOwner) { user ->
            user.let {
                setTextView(R.id.fullName_textView, it.name + " " + it.surname)
                setTextView(R.id.nickName_textView, it.nickname)
                setTextView(R.id.description_textView, it.description)
                setTextView(R.id.age_textView, it.birthdate)
                setTextView(R.id.mail_textView, it.email)
                setTextView(R.id.phoneNumber_textView, it.phoneNumber)
                setTextView(R.id.gender_textView, it.gender)
                setTextView(R.id.location_textView, it.location)
                setCheckBox(R.id.mondayAvailability, it.monday_availability)
                setCheckBox(R.id.tuesdayAvailability, it.tuesday_availability)
                setCheckBox(R.id.wednesdayAvailability, it.wednesday_availability)
                setCheckBox(R.id.thursdayAvailability, it.thursday_availability)
                setCheckBox(R.id.fridayAvailability, it.friday_availability)
                setCheckBox(R.id.saturdayAvailability, it.saturday_availability)
                setCheckBox(R.id.sundayAvailability, it.sunday_availability)
            }

            //set favourite sport
        }
    }

    private fun setCheckBox(id: Int, field: Boolean) {
        view?.findViewById<CheckBox>(id)?.isChecked = field
    }


    private fun setTextView(id: Int, field: String?) {
        field?.let { view?.findViewById<TextView>(id)?.text = field }
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_show_profile, menu)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)

        return true
    }
}
