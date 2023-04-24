package com.example.polito_mad_01.ui

import android.os.Bundle
import android.view.*
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
                setTextView(R.id.monHours_textView, it.monday_availability.toString())
                setTextView(R.id.tueHours_textView, it.tuesday_availability.toString())
                setTextView(R.id.wedHours_textView, it.wednesday_availability.toString())
                setTextView(R.id.thuHours_textView, it.thursday_availability.toString())
                setTextView(R.id.friHours_textView, it.friday_availability.toString())
                setTextView(R.id.satHours_textView, it.saturday_availability.toString())
                setTextView(R.id.sunHours_textView, it.sunday_availability.toString())
            }

            //set favourite sport
        }
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
