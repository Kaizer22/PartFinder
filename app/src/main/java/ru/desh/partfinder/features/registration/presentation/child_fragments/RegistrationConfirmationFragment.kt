package ru.desh.partfinder.features.registration.presentation.child_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import ru.desh.partfinder.databinding.FragmentRegistrationConfirmationBinding
import ru.desh.partfinder.features.registration.presentation.RegistrationFragment
import ru.desh.partfinder.features.registration.presentation.RegistrationState
import javax.inject.Inject

class RegistrationConfirmationViewModel @Inject constructor(
    private val registrationState: MutableStateFlow<RegistrationState>
): ViewModel() {
    fun notifyDataConfirmed(){
        registrationState.value = RegistrationState.DataConfirmed
    }
}

class RegistrationConfirmationFragment(
    private val registrationType: RegistrationFragment.RegistrationType
): Fragment() {
    @Inject
    lateinit var viewModel: RegistrationConfirmationViewModel

    private lateinit var binding: FragmentRegistrationConfirmationBinding
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
        binding = FragmentRegistrationConfirmationBinding
            .inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            when (registrationType) {
                RegistrationFragment.RegistrationType.PHONE -> {
                    registrationConfirmationTitle.text = "Регистрация по номеру\nтелефона"
                    registrationConfirmationInfo.text = "Введите 6-значный код из SMS"
                    registrationConfirmationOtpInput.visibility = View.VISIBLE
                    registrationConfirmationButtonRepeatOtp.visibility = View.VISIBLE
                    registrationConfirmationButtonConfirmOtp.visibility = View.VISIBLE
                }
                RegistrationFragment.RegistrationType.EMAIL -> {
                    registrationConfirmationTitle.text = "Регистрация по E-mail"
                    registrationConfirmationInfo.text = "Мы выслали письмо на указанный адрес. Доставка письма может занимать от нескольких секунд до минут. Если письмо не приходит, не забудьте проверить “Спам”"
                    registrationConfirmationEmailAnimation.visibility = View.VISIBLE
                    registrationConfirmationButtonEmailNext.visibility = View.VISIBLE
                }
            }
            registrationConfirmationButtonEmailNext.setOnClickListener {
                viewModel.notifyDataConfirmed()
            }
            registrationConfirmationButtonConfirmOtp.setOnClickListener {
                viewModel.notifyDataConfirmed()
            }
        }
    }
}