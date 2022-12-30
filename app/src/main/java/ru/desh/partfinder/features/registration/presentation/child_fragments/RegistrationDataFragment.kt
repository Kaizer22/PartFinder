package ru.desh.partfinder.features.registration.presentation.child_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.MutableStateFlow
import ru.desh.partfinder.core.ui.SnackbarManager
import ru.desh.partfinder.databinding.FragmentRegistrationBinding
import ru.desh.partfinder.databinding.FragmentRegistrationDataBinding
import ru.desh.partfinder.features.auth.data.AuthRepository
import ru.desh.partfinder.features.registration.presentation.RegistrationFragment
import ru.desh.partfinder.features.registration.presentation.RegistrationState
import javax.inject.Inject

class RegistrationDataViewModel @Inject constructor(
    private val registrationState: MutableStateFlow<RegistrationState>,
    private val authRepository: AuthRepository
): ViewModel() {
    fun notifyPhoneDataSent() {
        registrationState.value = RegistrationState.PhoneInputFinished
    }
    fun notifyEmailDataSent() {
        registrationState.value = RegistrationState.EmailInputFinished
    }

    // TODO encode password

    fun createUserWithEmailAndPassword(email: String, password: String) {
        //authRepository.createUserWithEmailAndPassword(email, password).collect()
    }
}

class RegistrationDataFragment(
    private val regMethod: RegistrationFragment.RegistrationType
): Fragment() {
    @Inject
    lateinit var viewModel: RegistrationDataViewModel

    private lateinit var binding: FragmentRegistrationDataBinding
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
        binding = FragmentRegistrationDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            when(regMethod) {
                RegistrationFragment.RegistrationType.PHONE -> {
                    // TODO
                    registrationDataTitle.text = "Регистрация по номеру\nтелефона"
                    registrationDataInfo.text = "На указанный номер будет выслано SMS с кодом подтвержения"
                    registrationDataPhoneBlock.visibility = View.VISIBLE
                }
                RegistrationFragment.RegistrationType.EMAIL -> {
                    registrationDataTitle.text = "Регистрация по Email"
                    registrationDataInfo.text = "На указанный адрес будет выслано письмо для подтверждения"
                    registrationDataEmailBlock.visibility = View.VISIBLE
                }
            }
            registrationButtonSendData.setOnClickListener {
                SnackbarManager().build(view, layoutInflater, Snackbar.LENGTH_LONG)
                    .setType(SnackbarManager.Type.PRIMARY)
                    .setTitle("Ыхыхыхыхыххы")
                    .setText("ыыыыыыыыыыыыыыыыыыыыыыыыыыыыыыыыыыыыыыыыыыыыыыыыыыыыыыы\nsssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss")
                    .show()
                when(regMethod) {
                    RegistrationFragment.RegistrationType.PHONE -> viewModel.notifyPhoneDataSent()
                    RegistrationFragment.RegistrationType.EMAIL -> viewModel.createUserWithEmailAndPassword(
                        registrationDataEmailInput.editText?.text.toString(),
                        registrationDataPasswordInput.editText?.text.toString()
                    )
                }
            }
        }
    }
}