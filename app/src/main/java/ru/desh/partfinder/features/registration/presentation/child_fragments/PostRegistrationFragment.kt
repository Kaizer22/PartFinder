package ru.desh.partfinder.features.registration.presentation.child_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.desh.partfinder.databinding.FragmentPostRegistrationBinding

class PostRegistrationFragment: Fragment() {
    private lateinit var binding: FragmentPostRegistrationBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }
}