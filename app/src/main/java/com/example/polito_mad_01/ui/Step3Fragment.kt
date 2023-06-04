package com.example.polito_mad_01.ui

import android.annotation.SuppressLint
import android.os.*
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.polito_mad_01.R
import com.example.polito_mad_01.viewmodel.RegistrationViewModel
import com.google.android.material.textfield.*

class Step3Fragment: Fragment(R.layout.step3fragment) {
    private lateinit var mView: View
    private lateinit var registrationViewModel : RegistrationViewModel

    @SuppressLint("FragmentLiveDataObserve")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?    ): View {
        mView = inflater.inflate(R.layout.step3fragment, container, false)
        registrationViewModel = ViewModelProvider(requireActivity())[RegistrationViewModel::class.java]

        setupSkill(R.id.checkBoxBasket, R.id.sportLevelBasket)
        setupSkill(R.id.checkBoxFootball, R.id.sportLevelFootball)
        setupSkill(R.id.checkBoxPingPong, R.id.sportLevelPingPong)
        setupSkill(R.id.checkBoxVolleyball, R.id.sportLevelVolleyball)

        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSpinners(R.id.sportLevelBasket)
        setSpinners(R.id.sportLevelFootball)
        setSpinners(R.id.sportLevelVolleyball)
        setSpinners(R.id.sportLevelPingPong)
    }


    private fun setupSkill(checkboxId: Int, textInputId: Int){
        mView.findViewById<CheckBox>(checkboxId).let { checkbox ->
            checkbox.setOnClickListener {
                mView.findViewById<TextInputLayout>(textInputId).isEnabled = checkbox.isChecked
            }
        }
    }


    private fun setSpinners(id: Int) {
        val textField1 = requireView().findViewById<TextInputLayout>(id)
        val sportLevelArray = resources.getStringArray(R.array.sportLevelArray)
        val adapter1 = ArrayAdapter(requireContext(), R.layout.sport_list_item, sportLevelArray)
        (textField1.editText as? AutoCompleteTextView)?.setAdapter(adapter1)

    }


    override fun onStop() {
        super.onStop()
        val basketSkillView = mView.findViewById<AutoCompleteTextView>(R.id.sportLevelBasketMenu)
        val footballSkillView = mView.findViewById<AutoCompleteTextView>(R.id.sportLevelFootballMenu)
        val pingPongSkillView = mView.findViewById<AutoCompleteTextView>(R.id.sportLevelPingPongMenu)
        val volleyballSkillView = mView.findViewById<AutoCompleteTextView>(R.id.sportLevelVolleyballMenu)

        val map = mutableMapOf<String,String>()
        if(basketSkillView.text.toString().isNotEmpty())
            map["Basket"] = basketSkillView.text.toString()
        if(footballSkillView.text.toString().isNotEmpty())
            map["Football"] = footballSkillView.text.toString()
        if(pingPongSkillView.text.toString().isNotEmpty())
            map["Ping Pong"] = pingPongSkillView.text.toString()
        if(volleyballSkillView.text.toString().isNotEmpty())
            map["Volley"] = volleyballSkillView.text.toString()

        registrationViewModel.user.value?.skills =map

    }

}