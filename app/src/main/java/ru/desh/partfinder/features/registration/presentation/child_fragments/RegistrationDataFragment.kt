package ru.desh.partfinder.features.registration.presentation.child_fragments

import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.util.Patterns
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
import ru.desh.partfinder.databinding.FragmentRegistrationDataBinding
import ru.desh.partfinder.features.registration.presentation.RegistrationFragment
import ru.desh.partfinder.features.registration.presentation.RegistrationState
import java.util.regex.Pattern
import javax.inject.Inject

class RegistrationDataViewModel @Inject constructor(
    private val registrationState: MutableStateFlow<RegistrationState>,
    private val authRepository: AuthRepository
) : ViewModel() {

    data class RegistrationDataState(
        val isEmailUserCreated: Boolean = false,
        val isPhoneUserCreated: Boolean = false,
        val isEmailUserCreationFailed: Boolean = false,
        val isPhoneUserCreationFailed: Boolean = false,
        val error: Exception? = null
    )

    private val _registrationDataState = MutableLiveData(RegistrationDataState())
    val registrationDataState: LiveData<RegistrationDataState> = _registrationDataState

    fun notifyPhoneDataSent(phoneNumber: String) {
        registrationState.value = RegistrationState.PhoneInputFinished(phoneNumber)
    }

    fun notifyEmailDataSent() {
        registrationState.value = RegistrationState.EmailInputFinished
    }

    fun createUserWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            val res = authRepository.createAccountWithEmailAndPassword(email, password)
            if (!res.isException) {
                _registrationDataState.value = _registrationDataState.value?.copy(
                    isEmailUserCreated = true
                )
                this@RegistrationDataViewModel.sendVerificationEmail()
                this@RegistrationDataViewModel.notifyEmailDataSent()
            } else {
                _registrationDataState.value = _registrationDataState.value?.copy(
                    isEmailUserCreationFailed = true,
                    error = res.exception
                )
            }
        }
        return
    }

    fun sendVerificationEmail() {
        viewModelScope.launch {
            authRepository.sendVerificationEmail()
        }
    }

    fun createUserWithPhoneNumber(phoneNumber: String) {
        viewModelScope.launch {
            authRepository.createAccountWithPhone(phoneNumber)
        }
    }
}

class RegistrationDataFragment(
    private val regMethod: RegistrationFragment.RegistrationType
) : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: RegistrationDataViewModel

    private lateinit var binding: FragmentRegistrationDataBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (parentFragment as RegistrationFragment).registrationComponent
            .inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[RegistrationDataViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var dangerMessage: SnackbarBuilder
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            when (regMethod) {
                RegistrationFragment.RegistrationType.PHONE -> {
                    registrationDataTitle.text = getString(R.string.registration_phone_title)
                    registrationDataInfo.text = getString(R.string.registration_data_phone_info)
                    registrationDataPhoneBlock.visibility = View.VISIBLE
                }
                RegistrationFragment.RegistrationType.EMAIL -> {
                    registrationDataTitle.text = getString(R.string.registration_email_title)
                    registrationDataInfo.text = getString(R.string.registration_data_email_info)
                    registrationDataEmailBlock.visibility = View.VISIBLE
                }
            }
            val warningMessage = SnackbarBuilder(content, layoutInflater, Snackbar.LENGTH_LONG)
                .setType(SnackbarBuilder.Type.WARNING)
                .setTitle(getString(R.string.message_title_wrong_input))
            dangerMessage = SnackbarBuilder(content, layoutInflater, Snackbar.LENGTH_LONG)
                .setType(SnackbarBuilder.Type.DANGER)
                .setTitle(getString(R.string.message_title_error))

            viewModel.registrationDataState.observe(viewLifecycleOwner) { newState ->
                updateUiState(newState)
            }

            registrationDataButtonSend.setOnClickListener {
                when (regMethod) {
                    RegistrationFragment.RegistrationType.PHONE -> {
                        val phoneNumber = registrationDataPhoneInput.text.toString()
                        if (isValidPhoneNumber(phoneNumber)) {
                            viewModel.notifyPhoneDataSent(phoneNumber)
//                            viewModel.createUserWithPhoneNumber(phoneNumber).observe(viewLifecycleOwner) {
//                                result ->
//                                if (!result.isException) {
//                                    viewModel.notifyPhoneDataSent(phoneNumber)
//                                } else {
//                                    dangerMessage.setText(result.exception.toString()).show()
//                                }
//                            }
                        } else {
                            warningMessage.setText(getString(R.string.message_text_wrong_phone_number))
                                .show()
                        }
                    }
                    RegistrationFragment.RegistrationType.EMAIL -> {
                        val email = registrationDataEmailInput.text.toString()
                        val password = registrationDataPasswordInput.text.toString()
                        val password2 = registrationDataPasswordRepeatInput.text.toString()
                        if (isValidEmailData(email, password, password2)) {
                            viewModel.createUserWithEmailAndPassword(email, password)
                        } else if (password != password2) {
                            warningMessage.setText(
                                getString(R.string.warning_text_passwords_not_same)
                            ).show()
                        } else {
                            warningMessage.setText(
                                getString(
                                    R.string.message_text_registration_wrong_email_or_password
                                )
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun updateUiState(newState: RegistrationDataViewModel.RegistrationDataState) {
        if (newState.isEmailUserCreationFailed) {
            dangerMessage.setText(newState.error?.message ?: "").show()
        }
    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean =
        PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)

    // At least one digit, one uppercase letter, one special symbol
    private val passwordPattern = "^.*(?=.{8,})(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!#\$%&? \"]).*\$"
    private val pattern = Pattern.compile(passwordPattern)
    private fun isValidEmailData(email: String, password: String, password2: String): Boolean =
        email.isNotEmpty() && password.isNotEmpty() &&
                password == password2 && password.length >= 8 && pattern.matcher(password).matches()
                && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}