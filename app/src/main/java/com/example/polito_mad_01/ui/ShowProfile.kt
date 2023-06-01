package com.example.polito_mad_01.ui

import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import com.example.polito_mad_01.*
import com.example.polito_mad_01.model.User
import com.example.polito_mad_01.util.UIUtils.setTextView
import com.example.polito_mad_01.viewmodel.*
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import io.getstream.avatarview.AvatarView
import io.getstream.avatarview.coil.loadImage

class  ShowProfile(val vm: ShowProfileViewModel) : Fragment(R.layout.fragment_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {}.isEnabled = true
        try {
            setAllView()
        } catch (e: NotImplementedError) {
            e.printStackTrace()
        }
    }

    private fun setAllView() {
        val currUser = FirebaseAuth.getInstance().currentUser

        vm.getUser().observe(viewLifecycleOwner) {user->
            user.let {
                setTextView(R.id.fullname, it.name + " " + it.surname, view)
                setTextView(R.id.nickname, it.nickname,view)
                setTextView(R.id.description, it.achievements.toString(),view)
                setTextView(R.id.birthdate, it.birthdate,view)
                setTextView(R.id.email_text, currUser?.email ,view)
                setTextView(R.id.phoneNumber_text, it.phoneNumber,view)
                setTextView(R.id.gender, it.gender,view)
                setTextView(R.id.location, it.location,view)
                setAllButtons(it)
            }

            val skills = user.skills
            for(skill in skills){

                //if(skill.level == "none") continue

                val chip = Chip(context)

                chip.text = skill.key

                when(skill.key){
                    "Basket" -> chip.chipIcon = ContextCompat.getDrawable(requireContext(), R.drawable.sports_basketball_48px)
                    "Football" -> chip.chipIcon = ContextCompat.getDrawable(requireContext(), R.drawable.sports_soccer_48px)
                    "Volley" -> chip.chipIcon = ContextCompat.getDrawable(requireContext(), R.drawable.sports_volleyball_48px)
                    "Ping Pong" -> chip.chipIcon = ContextCompat.getDrawable(requireContext(), R.drawable.sports_tennis_48px)
                }

                when(skill.value){
                    "Beginner" -> chip.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, R.drawable.stars_48px)
                    "Intermediate" -> chip.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, R.drawable.stars_double)
                    "Expert" -> chip.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, R.drawable.stars_triple)
                }

                chip.isChipIconVisible = true
                view?.findViewById<ChipGroup>(R.id.chip_group)?.addView(chip)
            }

        }

        vm.getUserImage().observe(viewLifecycleOwner) { image ->
            setImage(image)
        }
    }


    private fun setAllButtons(user: User) {
        setButtonColor(R.id.mondayButton, user.availability["monday"]!!, "monday")
        setButtonColor(R.id.tuesdayButton, user.availability["tuesday"]!!, "tuesday")
        setButtonColor(R.id.wednesdayButton, user.availability["wednesday"]!!, "wednesday")
        setButtonColor(R.id.thursdayButton, user.availability["thursday"]!!, "thursday")
        setButtonColor(R.id.fridayButton, user.availability["friday"]!!, "friday")
        setButtonColor(R.id.saturdayButton, user.availability["saturday"]!!, "saturday")
        setButtonColor(R.id.sundayButton, user.availability["sunday"]!!, "sunday")
    }

    private fun setButtonColor(id: Int, value: Boolean, attribute: String) {

        val button = requireView().findViewById<Button>(id)

        val colorTrue = ContextCompat.getColor(requireContext(), R.color.powder_blue)
        val colorFalse = ContextCompat.getColor(requireContext(), R.color.gray)

        if(value) button.setBackgroundColor(colorTrue)
        else button.setBackgroundColor(colorFalse)
    }

    private fun setImage(image: Uri?) {
        val frame = view?.findViewById<AvatarView>(R.id.profileImage_imageView)!!
        if (image == null) {
            val user = vm.user.value!!
            val name = user.name
            val surname = user.surname
            frame.avatarInitials = name.substring(0, 1) + surname.substring(0, 1)
        }
        else{
            if (image != Uri.EMPTY) {
                frame.loadImage(image)
            }
        }
    }

}
