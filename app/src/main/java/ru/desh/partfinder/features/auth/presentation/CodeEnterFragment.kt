package ru.desh.partfinder.features.auth.presentation

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
import com.github.terrakok.cicerone.Router
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import ru.desh.partfinder.R
import ru.desh.partfinder.core.Screens.BottomNavigation
import ru.desh.partfinder.core.Screens.Registration
import ru.desh.partfinder.core.di.SingleApplicationComponent
import ru.desh.partfinder.core.di.module.AppNavigation
import ru.desh.partfinder.core.domain.repository.AuthRepository
import ru.desh.partfinder.core.ui.SnackbarBuilder
import ru.desh.partfinder.core.utils.DataOrErrorResult
import ru.desh.partfinder.databinding.FragmentEnterSmsCodeBinding
import javax.inject.Inject

class CodeEnterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @AppNavigation private val router: Router
) : ViewModel() {

    data class CodeEnterState(
        val isRepeatTimerFinished: Boolean = false,
        val isSignedIn: Boolean = false,
        val isSignInFailed: Boolean = false,
        val error: Exception? = null
    )

    private val _codeEnterState = MutableLiveData(CodeEnterState())
    val codeEnterState: LiveData<CodeEnterState> = _codeEnterState

    fun back() = router.exit()

    fun toBottomNavigation() = router.navigateTo(BottomNavigation())

    fun toRegistration() = router.navigateTo(Registration())

    fun sendVerificationCode(phoneNumber: String): LiveData<DataOrErrorResult<String, Exception?>> {
        return authRepository.sendVerificationCode(phoneNumber)
    }

    fun signInWithCode(code: String) {
        viewModelScope.launch {
            val res = authRepository.verifyCode(code)
            if (!res.isException) {
                _codeEnterState.value = _codeEnterState.value?.copy(
                    isSignedIn = true
                )
            } else {
                _codeEnterState.value = _codeEnterState.value?.copy(
                    isSignInFailed = true,
                    error = res.exception
                )
            }
        }
    }
}

// TODO unify with RegistrationConfirmationFragment and reuse
class CodeEnterFragment(
    private val phoneNumber: String
) : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: CodeEnterViewModel

    private var otp: String = ""

    private lateinit var binding: FragmentEnterSmsCodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SingleApplicationComponent.getInstance().inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[CodeEnterViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentEnterSmsCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var dangerMessage: SnackbarBuilder

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            dangerMessage = SnackbarBuilder(content, layoutInflater, Snackbar.LENGTH_LONG).setType(
                SnackbarBuilder.Type.DANGER
            ).setTitle(getString(R.string.message_title_code_doesnt_send))

            viewModel.codeEnterState.observe(viewLifecycleOwner) { newState ->
                updateUiState(newState)
            }

            viewModel.sendVerificationCode(phoneNumber).observe(viewLifecycleOwner) { result ->
                if (!result.isException) {
                    result.data?.let {
                        otp = it
                        if (otp.length == enterSmsCodeOtpInput.digitsCount()) {
                            enterSmsCodeOtpInput.setText(otp)
                            activateConfirmationButton()
                        }
                    }
                } else {
                    dangerMessage.setText(result.exception?.message ?: "").show()
                }
            }
            enterSmsCodeOtpInput.setInputChangedListener { complete, text ->
                if (complete) {
                    otp = text
                    activateConfirmationButton()
                }
            }
            // TODO run timer on button
            enterSmsCodeButtonRepeatOtp.setOnClickListener {

            }
            enterSmsCodeButtonConfirmOtp.setOnClickListener {
                viewModel.signInWithCode(otp)
            }
            enterSmsCodeButtonBack.setOnClickListener {
                viewModel.back()
            }
            hintRegisterBlock.authButtonToRegister.setOnClickListener {
                viewModel.toRegistration()
            }
        }
    }

    private fun updateUiState(newState: CodeEnterViewModel.CodeEnterState) {
        if (newState.isSignInFailed) {
            dangerMessage.setTitle(getString(R.string.message_title_wrong_code))
                .setText(newState.error?.message ?: "").show()
        }
        if (newState.isSignedIn) {
            viewModel.toBottomNavigation()
        }
    }

    @SuppressLint("ResourceType")
    private fun activateConfirmationButton() {
        val theme = requireActivity().theme.obtainStyledAttributes(
            arrayOf(
                R.attr.buttonPrimaryColor, R.attr.buttonPrimaryTextColor
            ).toIntArray()
        )
        binding.apply {
            enterSmsCodeButtonConfirmOtp.apply {
                isClickable = true
                backgroundTintList = ColorStateList.valueOf(theme.getColor(0, 0))
                setTextColor(theme.getColor(1, 0))
            }
        }
        theme.recycle()
    }
}
