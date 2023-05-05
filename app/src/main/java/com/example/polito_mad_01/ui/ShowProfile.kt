package com.example.polito_mad_01.ui

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.polito_mad_01.*
import com.example.polito_mad_01.db.User
import com.example.polito_mad_01.viewmodel.*
import com.google.android.material.textfield.TextInputLayout
import de.hdodenhof.circleimageview.CircleImageView

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
               setImage(user)
                setTextView(R.id.fullname, it.name + " " + it.surname)
                setTextView(R.id.nickname, it.nickname)
                setTextView(R.id.description, it.description)
                setTextView(R.id.birthdate, it.birthdate)
                setTextView(R.id.email_text, it.email)
                setTextView(R.id.phoneNumber_text, it.phoneNumber)
                setTextView(R.id.gender, it.gender)
                setTextView(R.id.location, it.location)
                setTextView(R.id.favouriteSport_text, it.favouriteSport)

                setAllButtons(it)

            }
        }
    }

    private fun setAllButtons(user: User) {
        setButtonColor(R.id.mondayButton, user.monday_availability, "monday")
        setButtonColor(R.id.tuesdayButton, user.tuesday_availability, "tuesday")
        setButtonColor(R.id.wednesdayButton, user.wednesday_availability, "wednesday")
        setButtonColor(R.id.thursdayButton, user.thursday_availability, "thursday")
        setButtonColor(R.id.fridayButton, user.friday_availability, "friday")
        setButtonColor(R.id.saturdayButton, user.saturday_availability, "saturday")
        setButtonColor(R.id.sundayButton, user.sunday_availability, "sunday")
    }

    private fun setButtonColor(id: Int, value: Boolean, attribute: String) {

        val button = requireView().findViewById<Button>(id)

        val colorTrue = ContextCompat.getColor(requireContext(), R.color.powder_blue)
        val colorFalse = ContextCompat.getColor(requireContext(), R.color.gray)

        if(value) button.setBackgroundColor(colorTrue)
        else button.setBackgroundColor(colorFalse)
    }

    private fun setImage(user: User) {
        /*
        try {
            val uri = user.image_uri?.toUri()
            val imageView = view?.findViewById<CircleImageView>(R.id.profileImage_imageView)
            imageView?.setImageURI(uri)
        } catch (e: Exception) {
            e.printStackTrace()
        }

         */
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
        if (item.itemId == R.id.action_save_profile)
            findNavController().navigate(R.id.action_profileFragment_to_editProfileContainer)
        return true
    }

}
