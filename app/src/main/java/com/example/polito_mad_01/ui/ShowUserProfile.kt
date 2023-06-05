package com.example.polito_mad_01.ui

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.polito_mad_01.R
import com.example.polito_mad_01.SportTimeApplication
import com.example.polito_mad_01.model.User
import com.example.polito_mad_01.util.UIUtils
import com.example.polito_mad_01.viewmodel.*
import com.google.android.material.chip.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import io.getstream.avatarview.AvatarView
import io.getstream.avatarview.coil.loadImage

class ShowUserProfile : Fragment(R.layout.fragment_show_user_profile) {

    private val vm: ShowUserProfileViewModel by viewModels {
        ShowUserProfileViewModelFactory(
            (activity?.application as SportTimeApplication).userRepository,
        )
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

        val friendUser = requireArguments().getString("userId").toString()

        vm.getUserById(friendUser).observe(viewLifecycleOwner) { user->
            user.let {
                UIUtils.setTextView(R.id.fullname, it.name + " " + it.surname, view)
                UIUtils.setTextView(R.id.nickname, it.nickname, view)
                UIUtils.setTextView(R.id.description, it.achievements, view)
                UIUtils.setTextView(R.id.birthdate, it.birthdate, view)
                UIUtils.setTextView(R.id.email_text, it.email, view)
                UIUtils.setTextView(R.id.phoneNumber_text, it.phoneNumber, view)
                UIUtils.setTextView(R.id.gender, it.gender, view)
                UIUtils.setTextView(R.id.location, it.location, view)
                setAllButtons(it)
            }

            val skills = user.skills
            for(skill in skills){
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


            val button = view?.findViewById<Button>(R.id.removeFriendButton)
            if(friendUser == FirebaseAuth.getInstance().currentUser?.uid)
                button?.visibility = View.GONE

            button?.setOnClickListener{
                vm.removeFriend(friendUser)
                Snackbar.make(it, "Friend removed", Snackbar.LENGTH_SHORT).show()
                findNavController().navigate(R.id.reservationsFragment)
            }

        }

        vm.getUserImage(friendUser).observe(viewLifecycleOwner) { image ->
            setImage(image)
        }
    }


    private fun setAllButtons(user: User) {
        setButtonColor(R.id.mondayButton, user.availability["monday"]!!)
        setButtonColor(R.id.tuesdayButton, user.availability["tuesday"]!!)
        setButtonColor(R.id.wednesdayButton, user.availability["wednesday"]!!)
        setButtonColor(R.id.thursdayButton, user.availability["thursday"]!!)
        setButtonColor(R.id.fridayButton, user.availability["friday"]!!)
        setButtonColor(R.id.saturdayButton, user.availability["saturday"]!!)
        setButtonColor(R.id.sundayButton, user.availability["sunday"]!!)
    }

    private fun setButtonColor(id: Int, value: Boolean) {

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