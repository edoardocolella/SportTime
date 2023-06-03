package com.example.polito_mad_01.ui

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.fragment.app.*
import com.example.polito_mad_01.*
import com.example.polito_mad_01.model.User
import com.example.polito_mad_01.viewmodel.*
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout


class FindFriendsWithFilters : Fragment(R.layout.fragment_find_friends_with_filters){

    private lateinit var mView : View
    private lateinit var skillName: TextInputLayout
    private lateinit var skillValue: TextInputLayout
    private lateinit var location: TextInputLayout

    private val vm: FindFriendsWithFiltersViewModal by viewModels{
        FindFriendsWithFiltersViewModalFactory((activity?.application as SportTimeApplication).userRepository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mView = view

        skillName = mView.findViewById(R.id.skillName)
        skillValue = mView.findViewById(R.id.skillValue)
        location = mView.findViewById(R.id.location)

        setSelectors()

        mView.findViewById<Button>(R.id.searchButton).setOnClickListener {

            var flag = false

            vm.skillName.value = skillName.editText?.text.toString()

            if(vm.skillName.value!!.isEmpty()) {
                skillName.error = "Skill name is required"
                flag = true
            }else{skillName.error = null}

            vm.skillValue.value = skillValue.editText?.text.toString()
            if(vm.skillValue.value!!.isEmpty()) {
                skillValue.error = "Skill value is required"
                flag = true
            }else{skillValue.error = null}

            vm.location.value = location.editText?.text.toString()
            if(vm.location.value!!.isEmpty()) {
                location.error = "Location is required"
                flag = true
            }else{location.error = null}

            if(flag) return@setOnClickListener

            vm.findFriendBySkillAndLocation(
                vm.skillName.value!!,
                vm.skillValue.value!!,
                vm.location.value!!
            )
                .observe(viewLifecycleOwner) { users ->
                    if(users.isEmpty()){
                        Snackbar.make(mView, "No users found", Snackbar.LENGTH_LONG).show()
                    }
                    for (user in users) {
                        println(user)
                    }
                }
        }
    }

    private fun setSelectors() {
        val skillNameSelector = mView.findViewById<AutoCompleteTextView>(R.id.skillNameSelector)

        val skillNameArray = resources.getStringArray(R.array.sportArray)
        val skillNameAdapter = ArrayAdapter(requireContext(), R.layout.skillname_list_item, skillNameArray)
        (skillName.editText as? AutoCompleteTextView)?.setAdapter(skillNameAdapter)

        skillNameSelector.setOnItemClickListener { parent, _, position, _ ->
            vm.skillName.value = parent.getItemAtPosition(position).toString()
        }

        val skillValueArray = resources.getStringArray(R.array.sportLevelArray)
        val skillValueAdapter = ArrayAdapter(requireContext(), R.layout.skillvalue_list_item, skillValueArray)
        (skillValue.editText as? AutoCompleteTextView)?.setAdapter(skillValueAdapter)

        val skillValueSelector = mView.findViewById<AutoCompleteTextView>(R.id.skillValueSelector)
        skillValueSelector.setOnItemClickListener { parent, _, position, _ ->
            vm.skillValue.value = parent.getItemAtPosition(position).toString()
        }
    }

}