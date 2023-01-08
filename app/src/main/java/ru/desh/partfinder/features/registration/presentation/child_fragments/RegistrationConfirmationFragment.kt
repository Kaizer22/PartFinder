package ru.desh.partfinder.features.registration.presentation.child_fragments

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.MutableStateFlow
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
): ViewModel() {
    fun notifyDataConfirmed(){
        registrationState.value = RegistrationState.DataConfirmed
    }
    fun sendVerificationCode(phoneNumber: String): LiveData<DataOrErrorResult<String, Exception?>> {
        return authRepository.sendVerificationCode(phoneNumber)
    }
    fun signInWithCode(code: String): LiveData<DataOrErrorResult<Boolean, Exception?>> {
        return authRepository.verifyCode(code)
    }
}

class RegistrationConfirmationFragment(
    private val registrationType: RegistrationFragment.RegistrationType,
    private val phoneNumber: String
): Fragment() {
    @Inject
    lateinit var viewModel: RegistrationConfirmationViewModel

    private var otp: String = ""

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
            val dangerMessage = SnackbarBuilder(content, layoutInflater, Snackbar.LENGTH_LONG)
                .setType(SnackbarBuilder.Type.DANGER)
                .setTitle(getString(R.string.message_title_code_doesnt_send))
            when (registrationType) {
                RegistrationFragment.RegistrationType.PHONE -> {
                    registrationConfirmationTitle.text = getString(R.string.registration_phone_title)
                    registrationConfirmationInfo.text = getString(R.string.registration_confirmation_phone_info)
                    registrationConfirmationOtpInput.visibility = View.VISIBLE
                    registrationConfirmationButtonRepeatOtp.visibility = View.VISIBLE
                    registrationConfirmationButtonConfirmOtp.visibility = View.VISIBLE

                    viewModel.sendVerificationCode(phoneNumber).observe(viewLifecycleOwner){
                        result -> if (!result.isException) {
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
                    registrationConfirmationTitle.text = getString(R.string.registration_email_title)
                    registrationConfirmationInfo.text = getString(R.string.registration_confirmation_email_info)
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
                viewModel.signInWithCode(otp).observe(viewLifecycleOwner) {
                    result ->
                    if (!result.isException) {
                        viewModel.notifyDataConfirmed()
                    } else {
                        dangerMessage.setTitle(getString(R.string.message_title_wrong_code))
                            .setText(result.exception?.message ?: "").show()
                    }
                }
            }
        }
    }

    @SuppressLint("ResourceType")
    private fun activateConfirmationButton() {
        val theme = requireActivity().theme.obtainStyledAttributes(
            arrayOf(
                R.attr.buttonPrimaryColor,
                R.attr.buttonPrimaryTextColor).toIntArray()
        )
        binding.apply {
            registrationConfirmationButtonConfirmOtp.apply {
                isClickable = true
                backgroundTintList = ColorStateList.valueOf(theme.getColor(0,0))
                setTextColor(theme.getColor(1, 0))
            }
        }
        theme.recycle()
    }
}