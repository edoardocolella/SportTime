package com.example.polito_mad_01.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import com.example.polito_mad_01.R
import com.example.polito_mad_01.db.Skill
import com.example.polito_mad_01.viewmodel.EditProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputLayout

class ModalBottomSheet(private val vm: EditProfileViewModel) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet, container, false)
    }


    companion object {
        const val TAG = "ModalBottomSheet"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button).setOnClickListener {
            saveAction()
            dismiss()
        }

        setSpinners()

    }

    private fun saveAction() {

        val sportName =
            view?.findViewById<TextInputLayout>(R.id.sportName)?.editText?.text.toString()
        val sportLevel =
            view?.findViewById<TextInputLayout>(R.id.sportLevel)?.editText?.text.toString()

        if(sportLevel != "Beginner" && sportLevel != "Intermediate" && sportLevel != "Expert"){
            Toast.makeText(context, "Choose a level", Toast.LENGTH_SHORT).show()
            return
        }

        val newSkill = Skill(1, sportName, sportLevel)

        val oldList = vm.user.value?.skillList!!

        for (skill in oldList) {
            if(skill.sport_name == newSkill.sport_name && skill.level != "none"){
                Toast.makeText(context, "There is already that skill", Toast.LENGTH_SHORT).show()
                return
            }
            if(skill.sport_name == newSkill.sport_name){
                var skillList = vm.user.value?.skillList!!
                skillList = skillList.filter { it.sport_name != newSkill.sport_name }.toMutableList()
                skillList.add(newSkill)
                vm.user.value?.skillList = skillList
                addChipToGroup(newSkill)
                return
            }
        }
    }

    private fun addChipToGroup(skill: Skill) {

        val chipGroup = vm.chipGroup.value!! as ChipGroup

        val chip = Chip(context)
        chip.text = skill.sport_name

        if(skill.level == "none") return

        when (skill.sport_name) {
            "Basket" -> chip.chipIcon = getIcon(R.drawable.sports_basketball_48px)
            "Football" -> chip.chipIcon = getIcon(R.drawable.sports_soccer_48px)
            "Volley" -> chip.chipIcon = getIcon(R.drawable.sports_volleyball_48px)
            "Ping Pong" -> chip.chipIcon = getIcon(R.drawable.sports_tennis_48px)
        }

        when (skill.level) {
            "Beginner" -> chip.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, R.drawable.stars_48px)
            "Intermediate" -> chip.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, R.drawable.stars_double)
            "Expert" -> chip.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, R.drawable.stars_triple)
        }

        chip.isChipIconVisible = true
        chip.isCloseIconVisible = true
        // necessary to get single selection working
        chip.isClickable = true
        chip.isCheckable = false
        chipGroup.addView(chip)

        chip.setOnCloseIconClickListener {

            var skillList = vm.user.value?.skillList!!

            skillList = skillList.filter { it.sport_name != skill.sport_name }.toMutableList()
            skillList.add(Skill(1, skill.sport_name, "none"))

            vm.user.value?.skillList = skillList

            chipGroup.removeView(chip as View)

        }
    }


    private fun setSpinners() {

        val textField = requireView().findViewById<TextInputLayout>(R.id.sportName)
        val sportArray = resources.getStringArray(R.array.sportArray)
        val adapter = ArrayAdapter(requireContext(), R.layout.sport_list_item, sportArray)
        (textField.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        val textField1 = requireView().findViewById<TextInputLayout>(R.id.sportLevel)
        val sportLevelArray = resources.getStringArray(R.array.sportLevelArray)
        val adapter1 = ArrayAdapter(requireContext(), R.layout.sport_list_item, sportLevelArray)
        (textField1.editText as? AutoCompleteTextView)?.setAdapter(adapter1)

    }

    private fun getIcon(iconCode: Int): Drawable? =
        ContextCompat.getDrawable(requireContext(), iconCode)
}