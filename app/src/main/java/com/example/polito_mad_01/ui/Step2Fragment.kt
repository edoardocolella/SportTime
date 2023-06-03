package com.example.polito_mad_01.ui

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.*
import android.text.*
import android.view.*
import android.widget.*
import android.widget.AutoCompleteTextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.polito_mad_01.R
import com.example.polito_mad_01.viewmodel.RegistrationViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class Step2Fragment: Fragment(R.layout.step2fragment) {
    private lateinit var mView: View
    private lateinit var vm : RegistrationViewModel
    private lateinit var nameInputLayout: TextInputLayout
    private lateinit var surnameInputLayout: TextInputLayout
    private lateinit var genderInputLayout: TextInputLayout
    private lateinit var birthdayInputLayout: TextInputLayout
    private lateinit var locationInputLayout: TextInputLayout
    private lateinit var nicknameInputLayout: TextInputLayout
    private lateinit var achievementsInputLayout: TextInputLayout


    @SuppressLint("FragmentLiveDataObserve")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mView = inflater.inflate(R.layout.step2fragment, container, false)
        vm = ViewModelProvider(requireActivity())[RegistrationViewModel::class.java]
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAllView()
        setBirthdateView()
        setSpinners()
    }

    private fun getAllView() {
        nameInputLayout = mView.findViewById(R.id.registrationNameInputLayout)
        surnameInputLayout = mView.findViewById(R.id.registrationSurnameInputLayout)
        genderInputLayout = mView.findViewById(R.id.registrationGenderInputLayout)
        birthdayInputLayout = mView.findViewById(R.id.registrationBirthdayInputLayout)
        locationInputLayout = mView.findViewById(R.id.registrationLocationInputLayout)
        nicknameInputLayout = mView.findViewById(R.id.registrationNicknameInputLayout)
        achievementsInputLayout = mView.findViewById(R.id.registrationAchievementsInputLayout)
    }

    private fun setSpinners() {
        val genderArray = resources.getStringArray(R.array.genderArray)
        val adapter = ArrayAdapter(requireContext(), R.layout.gender_list_item, genderArray)
        (genderInputLayout.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun setBirthdateView() {
        val materialDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select a Date").build()
        materialDatePicker.addOnPositiveButtonClickListener {
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it)
            birthdayInputLayout.editText?.setText(date)
        }

        birthdayInputLayout.editText?.setOnClickListener {
            materialDatePicker.show(childFragmentManager, "DATE_PICKER")
        }

    }

    override fun onResume() {
        super.onResume()
        setAllView()
    }

    override fun onStop() {
        super.onStop()
        vm.user.value?.name = nameInputLayout.editText?.text.toString()
        vm.user.value?.surname = surnameInputLayout.editText?.text.toString()
        vm.user.value?.gender = genderInputLayout.editText?.text.toString()
        vm.user.value?.birthdate = birthdayInputLayout.editText?.text.toString()
        vm.user.value?.location = locationInputLayout.editText?.text.toString()
        vm.user.value?.nickname = nicknameInputLayout.editText?.text.toString()
        vm.user.value?.achievements = achievementsInputLayout.editText?.text.toString()
    }

    private fun setAllView(){
        nameInputLayout.editText?.setText(vm.user.value?.name ?: "")
        surnameInputLayout.editText?.setText(vm.user.value?.surname ?: "")
        genderInputLayout.editText?.setText(vm.user.value?.gender ?: "")
        birthdayInputLayout.editText?.setText(vm.user.value?.birthdate ?: "")
        locationInputLayout.editText?.setText(vm.user.value?.location ?: "")
        nicknameInputLayout.editText?.setText(vm.user.value?.nickname ?: "")
        achievementsInputLayout.editText?.setText(vm.user.value?.achievements ?: "")
    }

}