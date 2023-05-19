package com.example.polito_mad_01.ui

import android.os.Bundle
import android.view.*
import android.widget.*
import com.example.polito_mad_01.R
import com.example.polito_mad_01.util.UIUtils.findTextInputById
import com.example.polito_mad_01.util.UIUtils.getIcon
import com.example.polito_mad_01.viewmodel.EditProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.*
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
            findTextInputById(view,R.id.sportName)?.editText?.text.toString()
        val sportLevel =
            findTextInputById(view,R.id.sportLevel)?.editText?.text.toString()

        if(sportLevel != "Beginner" && sportLevel != "Intermediate" && sportLevel != "Expert"){
            Toast.makeText(context, "Choose a level", Toast.LENGTH_SHORT).show()
            return
        }
        val oldList = vm.user.value?.skills!!

        if(oldList.containsKey(sportName)){
            Toast.makeText(context, "There is already that skill", Toast.LENGTH_SHORT).show()
            return
        }
        else{
            vm.user.value?.skills?.put(sportName, sportLevel)
            addChipToGroup(sportName, sportLevel)
        }
    }

    private fun addChipToGroup(sportName: String, sportLevel: String) {

        val chipGroup = vm.chipGroup.value!! as ChipGroup

        val chip = Chip(context)
        chip.text = sportName

        when (sportName) {
            "Basket" -> chip.chipIcon = getIcon(R.drawable.sports_basketball_48px, requireContext())
            "Football" -> chip.chipIcon = getIcon(R.drawable.sports_soccer_48px, requireContext())
            "Volley" -> chip.chipIcon = getIcon(R.drawable.sports_volleyball_48px, requireContext())
            "Ping Pong" -> chip.chipIcon = getIcon(R.drawable.sports_tennis_48px, requireContext())
        }

        when (sportLevel) {
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
            vm.user.value?.skills?.remove(chip.text.toString())
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
}