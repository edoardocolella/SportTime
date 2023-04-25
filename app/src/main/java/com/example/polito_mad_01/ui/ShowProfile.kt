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
                setTextView(R.id.fullname, it.name + " " + it.surname)
                setTextView(R.id.nickname, it.nickname)
                setTextView(R.id.description, it.description)
                setTextView(R.id.birthdate, it.birthdate)
                setTextView(R.id.email_text, it.email)
                setTextView(R.id.phoneNumber_text, it.phoneNumber)
                setTextView(R.id.gender, it.gender)
                setTextView(R.id.location, it.location)
                setTextView(R.id.favouriteSport_text, it.favouriteSport)
                setCheckBox(R.id.mondayAvailability, it.monday_availability)
                setCheckBox(R.id.tuesdayAvailability, it.tuesday_availability)
                setCheckBox(R.id.wednesdayAvailability, it.wednesday_availability)
                setCheckBox(R.id.thursdayAvailability, it.thursday_availability)
                setCheckBox(R.id.fridayAvailability, it.friday_availability)
                setCheckBox(R.id.saturdayAvailability, it.saturday_availability)
                setCheckBox(R.id.sundayAvailability, it.sunday_availability)
            }
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
