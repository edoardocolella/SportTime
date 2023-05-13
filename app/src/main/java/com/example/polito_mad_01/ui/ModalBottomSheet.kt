package com.example.polito_mad_01.ui

import android.os.Bundle
import android.view.*
import android.widget.*
import com.example.polito_mad_01.R
import com.example.polito_mad_01.db.Skill
import com.example.polito_mad_01.viewmodel.EditProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
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

        val newSkill = Skill(1, sportName, sportLevel)

        val oldList = vm.user.value?.skillList

        for (skill in oldList!!) {
            if(skill.sport_name == newSkill.sport_name && skill.level != "none"){
                Toast.makeText(context, "There is already that skill", Toast.LENGTH_SHORT).show()
                return
            }
        }

        for (skill in oldList) {
            if(skill.sport_name == newSkill.sport_name && skill.level == "none"){
                vm.user.value?.skillList?.remove(skill)
            }
        }


            vm.user.value?.skillList?.add(newSkill)
        vm.newSkill.value = newSkill

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

    /*
    private fun setSpinnerListener(lambda: (Int) -> Unit): AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                lambda(position)
            }
        }
    }

     */
}