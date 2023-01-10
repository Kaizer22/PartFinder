package ru.desh.partfinder.features.auth.presentation

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.MutableStateFlow
import ru.desh.partfinder.R
import ru.desh.partfinder.core.Screens
import ru.desh.partfinder.core.Screens.BottomNavigation
import ru.desh.partfinder.core.di.SingleApplicationComponent
import ru.desh.partfinder.core.di.module.AppNavigation
import ru.desh.partfinder.core.domain.repository.AuthRepository
import ru.desh.partfinder.core.ui.SnackbarBuilder
import ru.desh.partfinder.core.utils.DataOrErrorResult
import ru.desh.partfinder.databinding.FragmentEnterSmsCodeBinding
import javax.inject.Inject


class CodeEnterViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
    fun sendVerificationCode(phoneNumber: String): LiveData<DataOrErrorResult<String, Exception?>> {
        return authRepository.sendVerificationCode(phoneNumber)
    }
    fun signInWithCode(code: String): LiveData<DataOrErrorResult<Boolean, Exception?>> {
        return authRepository.verifyCode(code)
    }
}

// TODO unify with RegistrationConfirmationFragment and reuse
class CodeEnterFragment(
    private val phoneNumber: String
): Fragment() {

    @Inject
    lateinit var viewModel: CodeEnterViewModel

    @Inject
    @AppNavigation
    lateinit var router: Router
    @Inject
    @AppNavigation
    lateinit var navigatorHolder: NavigatorHolder

    private var otp: String = ""

    private lateinit var binding: FragmentEnterSmsCodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SingleApplicationComponent.getInstance()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEnterSmsCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            val dangerMessage = SnackbarBuilder(content, layoutInflater, Snackbar.LENGTH_LONG)
                .setType(SnackbarBuilder.Type.DANGER)
                .setTitle(getString(R.string.message_title_code_doesnt_send))

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
                    dangerMessage.setText(result.exception?.message ?: "")
                        .show()
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
                viewModel.signInWithCode(otp).observe(viewLifecycleOwner) { result ->
                    if (!result.isException) {
                        router.navigateTo(BottomNavigation())
                    } else {
                        dangerMessage.setTitle(getString(R.string.message_title_wrong_code))
                            .setText(result.exception?.message ?: "").show()
                    }
                }
            }
            enterSmsCodeButtonBack.setOnClickListener {
                router.exit()
            }
            hintRegisterBlock.authButtonToRegister.setOnClickListener {
                router.navigateTo(Screens.Registration())
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
            enterSmsCodeButtonConfirmOtp.apply {
                isClickable = true
                backgroundTintList = ColorStateList.valueOf(theme.getColor(0,0))
                setTextColor(theme.getColor(1, 0))
            }
        }
        theme.recycle()
    }
}
