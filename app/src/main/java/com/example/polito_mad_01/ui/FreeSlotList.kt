package com.example.polito_mad_01.ui

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.polito_mad_01.R
import com.example.polito_mad_01.adapters.FreeSlotAdapter
import com.example.polito_mad_01.model.*

class FreeSlotList(private val slotList: List<Slot> = listOf()) : Fragment(R.layout.fragment_free_slot_list) {
    private lateinit var freeSlotView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {}.isEnabled = false

        freeSlotView = view.findViewById(R.id.freeSlotList)
        freeSlotView.layoutManager = LinearLayoutManager(view.context)
        freeSlotView.adapter = FreeSlotAdapter(slotList, findNavController())
    }
}