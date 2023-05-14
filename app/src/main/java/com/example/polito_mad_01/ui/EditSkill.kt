package com.example.polito_mad_01.ui


import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.*
import android.view.*
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.polito_mad_01.R
import com.example.polito_mad_01.db.Skill
import com.example.polito_mad_01.viewmodel.EditProfileViewModel
import com.google.android.material.chip.*


class EditSkill(private val vm: EditProfileViewModel) : Fragment(R.layout.fragment_edit_skill) {

    private lateinit var mView: View

    @SuppressLint("FragmentLiveDataObserve")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(        inflater: LayoutInflater,        container: ViewGroup?,        savedInstanceState: Bundle?    ): View {
        mView = inflater.inflate(R.layout.fragment_edit_skill, container, false)
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

        vm.newSkill.observe(viewLifecycleOwner) {
            addChipToGroup(it)
            println("Actual state of the list: ${vm.user.value?.skillList}")
        }
    }

    private fun addAllChipToGroup(value: List<Skill>) {
        value.forEach { addChipToGroup(it) }
    }

    private fun getIcon(iconCode: Int): Drawable? =
        ContextCompat.getDrawable(requireContext(), iconCode)

    private fun addChipToGroup(skill: Skill) {
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

            val skillToAdd = Skill(1,skill.sport_name, "none")
            vm.user.value?.skillList?.remove(skill)
            vm.user.value?.skillList?.add(skillToAdd)

            view?.findViewById<ChipGroup>(R.id.chip_group1)?.removeView(chip as View)


            println("Actual state of the list: ${vm.user.value?.skillList}")

        }
    }


}