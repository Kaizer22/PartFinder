package ru.desh.partfinder.features.chat.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.desh.partfinder.databinding.FragmentChatsBinding
import ru.desh.partfinder.features.BottomNavigationActivity

class ChatsFragment : Fragment() {
    private lateinit var binding: FragmentChatsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as BottomNavigationActivity).showNavigation()
    }
}