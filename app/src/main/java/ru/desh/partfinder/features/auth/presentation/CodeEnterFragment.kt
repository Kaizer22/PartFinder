package ru.desh.partfinder.features.auth.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.desh.partfinder.databinding.FragmentEnterSmsCodeBinding

class CodeEnterFragment: Fragment() {
    private lateinit var binding: FragmentEnterSmsCodeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEnterSmsCodeBinding.inflate(inflater, container, false)
        return binding.root
    }
}