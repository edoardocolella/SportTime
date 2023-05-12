package com.example.polito_mad_01.ui

import android.Manifest
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.net.*
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.*
import android.view.*
import android.widget.*
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.*
import androidx.navigation.fragment.findNavController
import com.example.polito_mad_01.*
import com.example.polito_mad_01.db.User
import com.example.polito_mad_01.viewmodel.*
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import io.getstream.avatarview.AvatarView
import io.getstream.avatarview.coil.loadImage
import java.util.*


class EditProfile(private val vm: EditProfileViewModel) : Fragment(R.layout.fragment_edit_profile) {

    private var imageUri: Uri? = null
    private val RESULT_LOAD_IMAGE = 123
    private val IMAGE_CAPTURE_CODE = 654
    private val PERMISSION_REQUEST_CODE = 200
    private var imageUriString: String? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher
            .addCallback(this) { showExitDialog() }
            .isEnabled = true

        val imgButton = view.findViewById<ImageButton>(R.id.imageButton)
        registerForContextMenu(imgButton)
        imgButton.setOnClickListener { v -> v.showContextMenu() }
        setAllView(view)
    }


    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        requireActivity().menuInflater.inflate(R.menu.menu_picture, menu)
        super.onCreateContextMenu(menu, v, menuInfo)
    }


    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        imageUri =
            activity?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        imageUriString = imageUri.toString()
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        view?.let {
            val frame = it.findViewById<AvatarView>(R.id.profileImage_imageView)
            if (requestCode == IMAGE_CAPTURE_CODE && resultCode == AppCompatActivity.RESULT_OK)
                frame.loadImage(imageUri)
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == AppCompatActivity.RESULT_OK && data != null) {
                imageUri = data.data!!
                imageUriString = imageUri.toString()
                frame.setImageURI(imageUri)
            }
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.gallery -> gallery()
            R.id.picture -> picture()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun gallery(): Boolean {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE)
        return true
    }


    private fun picture(): Boolean {
        if (checkPermission()) openCamera()
        else showPermissionReasonAndRequest()
        return true
    }

    private fun checkPermission(): Boolean {
        if (activity?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA) }
            != PackageManager.PERMISSION_GRANTED
        ) return false
        return true
    }


    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            PERMISSION_REQUEST_CODE
        )
    }


    private fun showPermissionReasonAndRequest() {
        AlertDialog.Builder(activity)
            .setTitle("Notice")
            .setMessage(R.string.cameraPermission.toString())
            .setPositiveButton("OK") { _, _ -> requestPermission()}
    }

    private fun showExitDialog(): Boolean {
        AlertDialog.Builder(activity)
            .setTitle("Are you sure?").setMessage("All changes will be lost")
            .setPositiveButton("YES") { _, _ -> findNavController().navigate(R.id.action_editProfileContainer_to_profileFragment) }
            .setNegativeButton("NO") { _, _ -> }.show()
        return true
    }

    private fun setValue(attribute: String, newValue: String) {
        when (attribute) {
            "name" -> vm.user.value?.user?.name = newValue
            "surname" -> vm.user.value?.user?.surname = newValue
            "nickname" -> vm.user.value?.user?.nickname = newValue
            "description" -> vm.user.value?.user?.description = newValue
            "email" -> vm.user.value?.user?.email = newValue
            "phoneNumber" -> vm.user.value?.user?.phoneNumber = newValue
            "location" -> vm.user.value?.user?.location = newValue
            "birthdate" -> vm.user.value?.user?.birthdate = newValue
            "favouriteSport" -> vm.user.value?.user?.favouriteSport = newValue
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setAllView(view: View) {
        vm.getUser(1).observe(viewLifecycleOwner) { userWithSkills ->
            val user = userWithSkills.user
            setTextViews(view,user)
            //setCheckBox(user)
            setButtons(user)
            setImage(user)
            setSpinners(user)

            val skills = userWithSkills.skillList

        }

    }

    private fun setButtons(user: User) {
        setButtonAndListener(R.id.mondayButton, user.monday_availability, "monday")
        setButtonAndListener(R.id.tuesdayButton, user.tuesday_availability, "tuesday")
        setButtonAndListener(R.id.wednesdayButton, user.wednesday_availability, "wednesday")
        setButtonAndListener(R.id.thursdayButton, user.thursday_availability, "thursday")
        setButtonAndListener(R.id.fridayButton, user.friday_availability, "friday")
        setButtonAndListener(R.id.saturdayButton, user.saturday_availability, "saturday")
        setButtonAndListener(R.id.sundayButton, user.sunday_availability, "sunday")
    }

    private fun setButtonAndListener(id: Int, value: Boolean, attribute: String) {
        val button = requireView().findViewById<Button>(id)
        setButtonColor(value, button)
        button.setOnClickListener {
            val newValue = !getAvailability(attribute)
            setAvailability(attribute, newValue)
            setButtonColor(newValue, button)
        }
    }

    private fun setButtonColor(value: Boolean, button: Button) {
        val colorTrue =  getColor(requireContext(),R.color.powder_blue)
        val colorFalse = getColor(requireContext(),R.color.gray)

        if(value) button.setBackgroundColor(colorTrue)
        else button.setBackgroundColor(colorFalse)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setTextViews(view: View, user: User) {

        setEditTextViewAndListener(R.id.name, user.name, "name")
        setEditTextViewAndListener(R.id.surname, user.surname, "surname")
        setEditTextViewAndListener(R.id.nickName_value, user.nickname, "nickname")
        setEditTextViewAndListener(R.id.description_value, user.description, "description")
        setEditTextViewAndListener(R.id.mail_value, user.email, "email")
        setEditTextViewAndListener(R.id.phoneNumber_value, user.phoneNumber, "phoneNumber")
        setEditTextViewAndListener(R.id.location_value, user.location, "location")
        setBirthdateView(view,user)
    }

    private fun setBirthdateView(view:View,user: User) {
        val birthdateView = view.findViewById<TextInputLayout>(R.id.birthday)
        birthdateView.editText?.setText(user.birthdate)

        println(birthdateView)

        val materialDatePicker=
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select a Date").build()
        materialDatePicker.addOnPositiveButtonClickListener {
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it)
            birthdateView.editText?.setText(date)
            setValue("birthdate", date)
        }

        birthdateView.editText?.setOnClickListener {
            materialDatePicker.show(childFragmentManager, "DATE_PICKER")
        }

    }

    private fun setSpinners(user: User) {

        val textField = requireView().findViewById<TextInputLayout>(R.id.gender)
        val genderArray = resources.getStringArray(R.array.genderArray)
        val adapter = ArrayAdapter(requireContext(), R.layout.gender_list_item, genderArray)
        (textField.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        val sportSpinner = view?.findViewById<Spinner>(R.id.sportSpinner)
        val sportArray = resources.getStringArray(R.array.sportArray)
        sportSpinner?.setSelection(sportArray.indexOf(user.favouriteSport))
        sportSpinner?.onItemSelectedListener =
            setSpinnerListener { user.favouriteSport = sportArray[it] }
    }

    private fun setAvailability(attribute: String, checked: Boolean) {
        when (attribute) {
            "monday" -> vm.user.value?.user?.monday_availability = checked
            "tuesday" -> vm.user.value?.user?.tuesday_availability = checked
            "wednesday" -> vm.user.value?.user?.wednesday_availability = checked
            "thursday" -> vm.user.value?.user?.thursday_availability = checked
            "friday" -> vm.user.value?.user?.friday_availability = checked
            "saturday" -> vm.user.value?.user?.saturday_availability = checked
            "sunday" -> vm.user.value?.user?.sunday_availability = checked
        }
    }

    private fun getAvailability(attribute: String):Boolean{
        return when (attribute) {
            "monday" -> vm.user.value?.user?.monday_availability!!
            "tuesday" -> vm.user.value?.user?.tuesday_availability!!
            "wednesday" -> vm.user.value?.user?.wednesday_availability !!
            "thursday" -> vm.user.value?.user?.thursday_availability !!
            "friday" -> vm.user.value?.user?.friday_availability !!
            "saturday" -> vm.user.value?.user?.saturday_availability !!
            "sunday" -> vm.user.value?.user?.sunday_availability !!
            else -> false
        }
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


    private fun setEditTextViewAndListener(id: Int, field: String?, attribute: String) {
        val textName = requireView().findViewById<TextInputLayout>(id)
        textName.editText?.setText(field)
        textName.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) =
                setValue(attribute, s.toString())
        })
    }

    private fun setImage(user: User) {
    /*try {
        val uri = user.image_uri?.toUri()
        val imageView = view?.findViewById<CircleImageView>(R.id.profileImage_imageView)
        imageView?.setImageURI(uri)
    } catch (e: Exception) {
        e.printStackTrace()
    }
     */

        view?.findViewById<AvatarView>(R.id.profileImage_imageView)?.loadImage(
            data = user.image_uri,
            onError = { _,_ ->
                requireView().findViewById<AvatarView>(R.id.profileImage_imageView).avatarInitials = user.name.substring(0,1) + user.surname.substring(0,1)
            }
        )

    }
}