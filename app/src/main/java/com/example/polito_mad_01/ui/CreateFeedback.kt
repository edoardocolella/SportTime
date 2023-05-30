package com.example.polito_mad_01.ui

import android.os.*
import android.view.*
import android.widget.Button
import android.widget.RatingBar
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.polito_mad_01.R
import com.example.polito_mad_01.SportTimeApplication
import com.example.polito_mad_01.model.Review
import com.example.polito_mad_01.viewmodel.*
import com.google.android.material.textfield.TextInputEditText


class CreateFeedback : Fragment(R.layout.fragment_create_feedback) {
    private var slotId = 0
    private var playgroundId = 0
    private var userId = ""

    private val reviewVm: ReviewViewModel by viewModels {
        ReviewViewModelFactory((activity?.application as SportTimeApplication).reviewRepository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {}.isEnabled = false


        slotId = requireArguments().getInt("slotId")
        playgroundId = requireArguments().getInt("playgroundId")
        userId = requireArguments().getString("userId").orEmpty()

        val createReviewButton = view.findViewById<Button>(R.id.SubmitReviewButton)
        val ratingBar = view.findViewById<RatingBar>(R.id.createReviewRatingBar)
        val textInput = view.findViewById<TextInputEditText>(R.id.createReviewEditText)

        createReviewButton.setOnClickListener {
            reviewVm.addReview(Review(playgroundId, userId, ratingBar.rating.toInt(), textInput.editableText.toString()))
            val args = bundleOf(
                "userId" to userId,
                "playgroundId" to playgroundId,
                "slotId" to slotId
            )

            findNavController().navigate(R.id.action_createFeedback_to_showOldReservation, args)

        }


    }
}