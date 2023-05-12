package com.example.polito_mad_01.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import com.example.polito_mad_01.R
import com.example.polito_mad_01.db.Skill
import com.example.polito_mad_01.viewmodel.EditProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputLayout

class ModalBottomSheet(private val vm: EditProfileViewModel): BottomSheetDialogFragment() {

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var listSkill: List<Skill>


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
            Toast.makeText(context, "Perform add operation", Toast.LENGTH_SHORT).show()
            saveAction()
            dismiss()
        }

        setSpinners()

    }

    private fun saveAction() {
        vm.sport_skills.value = listOf(Skill(1,view?.findViewById<TextInputLayout>(R.id.sportName)?.editText?.text.toString(),view?.findViewById<TextInputLayout>(R.id.sportLevel)?.editText?.text.toString() ))
        vm.user.value?.skillList?.add(Skill(1, view?.findViewById<TextInputLayout>(R.id.sportName)?.editText?.text.toString(),view?.findViewById<TextInputLayout>(R.id.sportLevel)?.editText?.text.toString()))
        //vm.sport_skills.value = listOf(view?.findViewById<TextInputLayout>(R.id.sportName)?.editText?.text.toString(),view?.findViewById<TextInputLayout>(R.id.sportLevel)?.editText?.text.toString())
        println("USER USER USER USER ${vm.user.value}")
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
}