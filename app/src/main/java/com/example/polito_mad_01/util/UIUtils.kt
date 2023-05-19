package com.example.polito_mad_01.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout

object UIUtils {

    fun findTextViewById(view: View?, id: Int) : TextView? {
        return view?.findViewById(id)
    }

    fun findTextInputById(view: View?, id: Int) : TextInputLayout? {
        return view?.findViewById(id)
    }

    fun setTextView(id: Int, field: String?, view: View?) {
        field?.let { findTextViewById(view,id)?.text = field }
    }

    fun getIcon(iconCode: Int, context:Context): Drawable? =
        ContextCompat.getDrawable(context, iconCode)
}