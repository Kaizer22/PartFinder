package ru.desh.partfinder.features.registration.presentation.child_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import ru.desh.partfinder.databinding.FragmentRegistrationMethodBinding
import ru.desh.partfinder.features.registration.presentation.RegistrationFragment
import ru.desh.partfinder.features.registration.presentation.RegistrationState
import javax.inject.Inject

class RegistrationMethodViewModel @Inject constructor(
    private val registrationState: MutableStateFlow<RegistrationState>
) : ViewModel() {
    fun setMethodEmail() {
        registrationState.value = RegistrationState.RegistrationMethodSelected(
            RegistrationFragment.RegistrationType.EMAIL
        )
    }

    fun setMethodPhone() {
        registrationState.value = RegistrationState.RegistrationMethodSelected(
            RegistrationFragment.RegistrationType.PHONE
        )
    }
}

class RegistrationMethodFragment : Fragment() {
    @Inject
    lateinit var viewModel: RegistrationMethodViewModel

    private lateinit var binding: FragmentRegistrationMethodBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (parentFragment as RegistrationFragment).registrationComponent
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationMethodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            registrationMethodButtonEmail.setOnClickListener {
                viewModel.setMethodEmail()
            }
            registrationMethodButtonPhone.setOnClickListener {
                viewModel.setMethodPhone()
            }
        }
    }
}