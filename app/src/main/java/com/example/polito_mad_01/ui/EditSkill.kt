package com.example.polito_mad_01.ui


import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.*
import android.view.*
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.polito_mad_01.R
import com.example.polito_mad_01.SportTimeApplication
import com.example.polito_mad_01.db.Skill
import com.example.polito_mad_01.viewmodel.EditProfileViewModel
import com.example.polito_mad_01.viewmodel.EditProfileViewModelFactory
import com.google.android.material.chip.*


class EditSkill(val vm: EditProfileViewModel) : Fragment(R.layout.fragment_edit_skill) {

    private lateinit var mView: View

    @SuppressLint("FragmentLiveDataObserve")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(        inflater: LayoutInflater,        container: ViewGroup?,        savedInstanceState: Bundle?    ): View {
        mView = inflater.inflate(R.layout.fragment_edit_skill, container, false)

        val chipGroup = mView.findViewById<ChipGroup>(R.id.chip_group1)
        vm.chipGroup = MutableLiveData(chipGroup)

        setAllView()
        return mView
    }

    private fun showBottomSheetDialogFragment() {
        ModalBottomSheet(vm).show(parentFragmentManager, ModalBottomSheet.TAG)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setAllView() {
        vm.getUser(1).observe(viewLifecycleOwner) { userWithSkills ->
            val skills = userWithSkills.skillList
            addAllChipToGroup(skills)
        }

        val addSkillButton = mView.findViewById<Button>(R.id.addSkill)
        addSkillButton.setOnClickListener { showBottomSheetDialogFragment() }

    }

    private fun addAllChipToGroup(value: List<Skill>) {
        value.forEach { addChipToGroup(it) }
    }

    private fun getIcon(iconCode: Int): Drawable? =
        ContextCompat.getDrawable(requireContext(), iconCode)

    private fun addChipToGroup(skill: Skill) {

        val chipGroup = mView.findViewById<ChipGroup>(R.id.chip_group1)

        val chip = Chip(context)
        chip.text = skill.sport_name

        if(skill.level == "none") return

        when (skill.sport_name) {
            "Basket" -> chip.chipIcon = getIcon(R.drawable.sports_basketball_48px)
            "Football" -> chip.chipIcon = getIcon(R.drawable.sports_soccer_48px)
            "Volley" -> chip.chipIcon = getIcon(R.drawable.sports_volleyball_48px)
            "Ping Pong" -> chip.chipIcon = getIcon(R.drawable.sports_tennis_48px)
        }

        when(skill.level){
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


}