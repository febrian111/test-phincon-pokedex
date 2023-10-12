package com.febri.pokedexphincon.captured

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.febri.pokedexphincon.databinding.FragmentCapturedBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CapturedFragment : Fragment() {

    private var _binding: FragmentCapturedBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCapturedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}