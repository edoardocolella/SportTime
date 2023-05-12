package com.example.polito_mad_01.adapters


import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.polito_mad_01.R
import com.example.polito_mad_01.db.Skill
import com.example.polito_mad_01.ui.ModalBottomSheet
import com.example.polito_mad_01.viewmodel.EditProfileViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


class EditSkill(private val vm: EditProfileViewModel) : Fragment(R.layout.fragment_edit_skill) {

    private lateinit var mView: View
    private lateinit var taskViewModel: TaskViewModel

    @SuppressLint("FragmentLiveDataObserve")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_edit_skill, container, false)


        vm.sport_skills.observe(this){
            addChipToGroup(it)
        }

        mView.findViewById<Chip>(R.id.chip_1).setOnClickListener {
            showBottomSheetDialogFragment()
        }

        setAllView(mView)

        return mView
    }

    private fun addChipToGroup(value: List<Skill>) {
        val chip = Chip(context)
        chip.text = value[0].sport_name

        when(value[0].sport_name){
            "Basket" -> chip.chipIcon = ContextCompat.getDrawable(requireContext(), R.drawable.sports_basketball_48px)
            "Football" -> chip.chipIcon = ContextCompat.getDrawable(requireContext(), R.drawable.sports_soccer_48px)
            "Volley" -> chip.chipIcon = ContextCompat.getDrawable(requireContext(), R.drawable.sports_volleyball_48px)
            "Ping Pong" -> chip.chipIcon = ContextCompat.getDrawable(requireContext(), R.drawable.sports_tennis_48px)
        }

        when(value[0].level){
            "Beginner" -> chip.setChipBackgroundColorResource(R.color.powder_blue)
            "Intermediate" -> chip.setChipBackgroundColorResource(R.color.gray)
            "Expert" -> chip.setChipBackgroundColorResource(R.color.red)
        }

        chip.isChipIconVisible = true
        chip.isCloseIconVisible = true
        // necessary to get single selection working
        chip.isClickable = true
        chip.isCheckable = false
        view?.findViewById<ChipGroup>(R.id.chip_group1)?.addView(chip)
        chip.setOnCloseIconClickListener {
            view?.findViewById<ChipGroup>(R.id.chip_group1)?.removeView(chip as View)
        }
    }


    private fun showBottomSheetDialogFragment() {
        ModalBottomSheet(vm).show(parentFragmentManager, ModalBottomSheet.TAG);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setAllView(view: View) {
        vm.getUser(1).observe(viewLifecycleOwner) { userWithSkills ->
            val skills = userWithSkills.skillList

            for(i in 0 until skills.size){
                val chip = Chip(context)
                chip.text = skills[i].sport_name

                when(skills[i].sport_name){
                    "Basket" -> chip.chipIcon = ContextCompat.getDrawable(requireContext(), R.drawable.sports_basketball_48px)
                    "Football" -> chip.chipIcon = ContextCompat.getDrawable(requireContext(), R.drawable.sports_soccer_48px)
                    "Volley" -> chip.chipIcon = ContextCompat.getDrawable(requireContext(), R.drawable.sports_volleyball_48px)
                    "Ping Pong" -> chip.chipIcon = ContextCompat.getDrawable(requireContext(), R.drawable.sports_tennis_48px)
                }

                /*if(skills[i].sport_name.equals("Basket"))
                    chip.chipIcon = ContextCompat.getDrawable(requireContext(), R.drawable.sports_basketball_48px)
                */chip.isChipIconVisible = true
                chip.isCloseIconVisible = true
                // necessary to get single selection working
                chip.isClickable = true
                chip.isCheckable = false
                view?.findViewById<ChipGroup>(R.id.chip_group1)?.addView(chip)
                chip.setOnCloseIconClickListener { view?.findViewById<ChipGroup>(R.id.chip_group1)?.removeView(chip as View) }
            }

        }

    }


}