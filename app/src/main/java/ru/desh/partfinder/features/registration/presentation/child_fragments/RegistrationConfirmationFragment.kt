package ru.desh.partfinder.features.registration.presentation.child_fragments

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.desh.partfinder.R
import ru.desh.partfinder.core.domain.repository.AuthRepository
import ru.desh.partfinder.core.ui.SnackbarBuilder
import ru.desh.partfinder.core.utils.DataOrErrorResult
import ru.desh.partfinder.databinding.FragmentRegistrationConfirmationBinding
import ru.desh.partfinder.features.registration.presentation.RegistrationFragment
import ru.desh.partfinder.features.registration.presentation.RegistrationState
import javax.inject.Inject

class RegistrationConfirmationViewModel @Inject constructor(
    private val registrationState: MutableStateFlow<RegistrationState>,
    private val authRepository: AuthRepository
) : ViewModel() {

    data class RegistrationConfirmationState(
        val isOtpConfirmed: Boolean = false,
        val isOtpConfirmationFailed: Boolean = false,
        val error: Exception? = null
    )

    private val _registrationConfirmationState = MutableLiveData(RegistrationConfirmationState())
    val registrationConfirmationState: LiveData<RegistrationConfirmationState> =
        _registrationConfirmationState

    fun notifyDataConfirmed() {
        registrationState.value = RegistrationState.DataConfirmed
    }

    fun sendVerificationCode(phoneNumber: String): LiveData<DataOrErrorResult<String, Exception?>> {
        return authRepository.sendVerificationCode(phoneNumber)
    }

    fun signInWithCode(code: String) {
        viewModelScope.launch {
            val res = authRepository.verifyCode(code)
            if (!res.isException) {
                _registrationConfirmationState.value = _registrationConfirmationState.value?.copy(
                    isOtpConfirmed = true
                )
                this@RegistrationConfirmationViewModel.notifyDataConfirmed()
            } else {
                _registrationConfirmationState.value = _registrationConfirmationState.value?.copy(
                    isOtpConfirmationFailed = true,
                    error = res.exception
                )
            }
        }
    }
}

class RegistrationConfirmationFragment(
    private val registrationType: RegistrationFragment.RegistrationType,
) : Fragment() {
    companion object {
        const val PHONE_NUMBER_ARGUMENT = "phone_number"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: RegistrationConfirmationViewModel

    private var otp: String = ""

    private lateinit var binding: FragmentRegistrationConfirmationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (parentFragment as RegistrationFragment).registrationComponent
            .inject(this)
        viewModel =
            ViewModelProvider(this, viewModelFactory)[RegistrationConfirmationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationConfirmationBinding
            .inflate(inflater, container, false)

        return binding.root
    }

    private lateinit var dangerMessage: SnackbarBuilder
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            dangerMessage = SnackbarBuilder(content, layoutInflater, Snackbar.LENGTH_LONG)
                .setType(SnackbarBuilder.Type.DANGER)
                .setTitle(getString(R.string.message_title_code_doesnt_send))

            viewModel.registrationConfirmationState.observe(viewLifecycleOwner) { newState ->
                updateUiState(newState)
            }
            when (registrationType) {
                RegistrationFragment.RegistrationType.PHONE -> {
                    val phoneNumber = arguments?.getString(PHONE_NUMBER_ARGUMENT) ?: ""

                    registrationConfirmationTitle.text =
                        getString(R.string.registration_phone_title)
                    registrationConfirmationInfo.text =
                        getString(R.string.registration_confirmation_phone_info)
                    registrationConfirmationOtpInput.visibility = View.VISIBLE
                    registrationConfirmationButtonRepeatOtp.visibility = View.VISIBLE
                    registrationConfirmationButtonConfirmOtp.visibility = View.VISIBLE

                    registrationConfirmationOtpInput.setInputChangedListener { complete, text ->
                        if (complete) {
                            otp = text
                            activateConfirmationButton()
                        }
                    }
                    viewModel.sendVerificationCode(phoneNumber)
                        .observe(viewLifecycleOwner) { result ->
                            if (!result.isException) {
                                result.data?.let {
                                    otp = it
                                    if (otp.length == registrationConfirmationOtpInput.digitsCount()) {
                                        registrationConfirmationOtpInput.setText(otp)
                                        activateConfirmationButton()
                                    }
                                }
                            } else {
                                dangerMessage.setText(result.exception?.message ?: "")
                                    .show()
                            }
                        }

                }
                RegistrationFragment.RegistrationType.EMAIL -> {
                    registrationConfirmationTitle.text =
                        getString(R.string.registration_email_title)
                    registrationConfirmationInfo.text =
                        getString(R.string.registration_confirmation_email_info)
                    registrationConfirmationEmailAnimation.visibility = View.VISIBLE
                    registrationConfirmationButtonEmailNext.visibility = View.VISIBLE
                }
            }
            registrationConfirmationButtonEmailNext.setOnClickListener {
                viewModel.notifyDataConfirmed()
            }
            // TODO run timer on button
            registrationConfirmationButtonRepeatOtp.setOnClickListener {

            }
            registrationConfirmationButtonConfirmOtp.setOnClickListener {
                viewModel.signInWithCode(otp)
            }
        }
    }

    private fun updateUiState(newState: RegistrationConfirmationViewModel.RegistrationConfirmationState) {
        if (newState.isOtpConfirmationFailed) {
            dangerMessage.setTitle(getString(R.string.message_title_wrong_code))
                .setText(newState.error?.message ?: "").show()
        }
    }

    @SuppressLint("ResourceType")
    private fun activateConfirmationButton() {
        val theme = requireActivity().theme.obtainStyledAttributes(
            arrayOf(
                R.attr.buttonPrimaryColor,
                R.attr.buttonPrimaryTextColor
            ).toIntArray()
        )
        binding.apply {
            registrationConfirmationButtonConfirmOtp.apply {
                isClickable = true
                backgroundTintList = ColorStateList.valueOf(theme.getColor(0, 0))
                setTextColor(theme.getColor(1, 0))
            }
        }
        theme.recycle()
    }
}