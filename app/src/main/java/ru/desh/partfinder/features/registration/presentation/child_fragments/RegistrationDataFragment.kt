package ru.desh.partfinder.features.registration.presentation.child_fragments

import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.MutableStateFlow
import ru.desh.partfinder.core.domain.model.Account
import ru.desh.partfinder.core.ui.SnackbarBuilder
import ru.desh.partfinder.databinding.FragmentRegistrationDataBinding
import ru.desh.partfinder.core.domain.repository.AuthRepository
import ru.desh.partfinder.core.utils.DataOrErrorResult
import ru.desh.partfinder.features.registration.presentation.RegistrationFragment
import ru.desh.partfinder.features.registration.presentation.RegistrationState
import java.util.regex.Pattern
import javax.inject.Inject

class RegistrationDataViewModel @Inject constructor(
    private val registrationState: MutableStateFlow<RegistrationState>,
    private val authRepository: AuthRepository
): ViewModel() {
    fun notifyPhoneDataSent(phoneNumber: String) {
        registrationState.value = RegistrationState.PhoneInputFinished(phoneNumber)
    }
    fun notifyEmailDataSent() {
        registrationState.value = RegistrationState.EmailInputFinished
    }

    // TODO encode password

    fun createUserWithEmailAndPassword(email: String, password: String):
            LiveData<DataOrErrorResult<Account?, Exception?>> {
        return authRepository.createAccountWithEmailAndPassword(email, password)
    }

    fun sendVerificationEmail() {
        authRepository.sendVerificationEmail()
    }

    fun createUserWithPhoneNumber(phoneNumber: String): LiveData<DataOrErrorResult<Account?, Exception?>> {
        return authRepository.createAccountWithPhone(phoneNumber)
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
            val warningMessage = SnackbarBuilder(content, layoutInflater, Snackbar.LENGTH_LONG)
                .setType(SnackbarBuilder.Type.WARNING).setTitle("Неверный ввод")
            val dangerMessage = SnackbarBuilder(content, layoutInflater, Snackbar.LENGTH_LONG)
                .setType(SnackbarBuilder.Type.DANGER).setTitle("Ошибка")
            registrationButtonSendData.setOnClickListener {
                when(regMethod) {
                    RegistrationFragment.RegistrationType.PHONE -> {
                        val phoneNumber = registrationDataPhoneInput.editText?.text.toString()
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
                            warningMessage.setText("Пожалуйста, перепроверьте введенные значения. " +
                                    "Некорректный номер телефона").show()
                        }
                    }
                    RegistrationFragment.RegistrationType.EMAIL -> {
                        val email = registrationDataEmailInput.editText?.text.toString()
                        val password = registrationDataPasswordInput.editText?.text.toString()
                        val password2 = registrationDataPasswordRepeatInput.editText?.text.toString()
                        if (isValidEmailData(email, password, password2)) {
                            viewModel.createUserWithEmailAndPassword(email, password).observe(viewLifecycleOwner) {
                                result ->
                                if (!result.isException) {
                                    viewModel.sendVerificationEmail()
                                    viewModel.notifyEmailDataSent()
                                } else {
                                    dangerMessage.setText(result.exception.toString())
                                }
                            }
                        } else if (password != password2){
                            warningMessage.setText(
                                    "Пожалуйста, перепроверьте введенные значения. " +
                                            "Пароли не совпадают"
                                ).show()
                        } else {
                            warningMessage.setText(
                                    "Пожалуйста, перепроверьте введенные значения. " +
                                            "Email должен быть корректным, а пароль соответствовать " +
                                            "требованиям"
                                ).show()
                        }
                    }
                }
            }
        }
    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean =
        PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)

    // At least one digit, one uppercase letter, one special symbol
    private val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#\$%^&+=!])(?=\\\\S+\$).{4,}\$"
    private val pattern = Pattern.compile(passwordPattern)
    private fun isValidEmailData(email: String, password: String, password2: String): Boolean =
        email.isNotEmpty() && password.isNotEmpty() &&
        password == password2 && password.length >= 8 //&& pattern.matcher(password).matches()
                && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}