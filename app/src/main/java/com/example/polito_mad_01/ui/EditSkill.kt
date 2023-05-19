package com.example.polito_mad_01.ui


import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.*
import android.view.*
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.example.polito_mad_01.R
import com.example.polito_mad_01.viewmodel.EditProfileViewModel
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
        vm.getUser(1).observe(viewLifecycleOwner) {
            val skills = it.skills
            addAllChipToGroup(skills)
        }

        val addSkillButton = mView.findViewById<Button>(R.id.addSkill)
        addSkillButton.setOnClickListener { showBottomSheetDialogFragment() }

    }

    private fun addAllChipToGroup(value: Map<String, String>) {
        value.forEach { addChipToGroup(it) }
    }

    private fun getIcon(iconCode: Int): Drawable? =
        ContextCompat.getDrawable(requireContext(), iconCode)

    private fun addChipToGroup(skill: Map.Entry<String, String>) {

        val chipGroup = mView.findViewById<ChipGroup>(R.id.chip_group1)

        val chip = Chip(context)
        chip.text = skill.key

        //if(skill.value == "none") return

        when (skill.key) {
            "Basket" -> chip.chipIcon = getIcon(R.drawable.sports_basketball_48px)
            "Football" -> chip.chipIcon = getIcon(R.drawable.sports_soccer_48px)
            "Volley" -> chip.chipIcon = getIcon(R.drawable.sports_volleyball_48px)
            "Ping Pong" -> chip.chipIcon = getIcon(R.drawable.sports_tennis_48px)
        }

        when(skill.value){
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


}