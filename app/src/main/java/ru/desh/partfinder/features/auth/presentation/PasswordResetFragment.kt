package ru.desh.partfinder.features.auth.presentation

import android.os.Bundle
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
import com.github.terrakok.cicerone.Router
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import ru.desh.partfinder.R
import ru.desh.partfinder.core.Screens.Auth
import ru.desh.partfinder.core.di.SingleApplicationComponent
import ru.desh.partfinder.core.di.module.AppNavigation
import ru.desh.partfinder.core.domain.repository.AuthRepository
import ru.desh.partfinder.core.ui.SnackbarBuilder
import ru.desh.partfinder.databinding.FragmentPasswordResetBinding
import javax.inject.Inject

class PasswordResetViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @AppNavigation private val router: Router
) : ViewModel() {

    data class PasswordResetState(
        val isResetSuccess: Boolean = false,
        val isResetFailed: Boolean = false,
        val error: Exception? = null
    )

    private val _passwordResetState = MutableLiveData(PasswordResetState())
    val passwordResetState: LiveData<PasswordResetState> = _passwordResetState

    fun back() = router.exit()
    fun toAuth() = router.navigateTo(Auth())

    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            val res = authRepository.sendPasswordResetEmail(email)
            if (!res.isException) {
                _passwordResetState.value = _passwordResetState.value?.copy(
                    isResetSuccess = true
                )
            } else {
                _passwordResetState.value = _passwordResetState.value?.copy(
                    isResetFailed = true,
                    error = res.exception
                )
            }
        }
    }
}

class PasswordResetFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: PasswordResetViewModel

    private lateinit var binding: FragmentPasswordResetBinding

    private var isEmailSent = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SingleApplicationComponent.getInstance().inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[PasswordResetViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPasswordResetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            val warningMessage = SnackbarBuilder(content, layoutInflater, Snackbar.LENGTH_LONG)
                .setType(SnackbarBuilder.Type.WARNING)
                .setTitle(getString(R.string.message_title_error))
                .setText(getString(R.string.message_text_pasword_reset_incorrect_email))

            viewModel.passwordResetState.observe(viewLifecycleOwner) { newState ->
                updateUiState(newState)
            }
            passwordResetButtonConfirm.setOnClickListener {
                val email = passwordResetEmailInput.editText?.text.toString()
                if (isValidData(email)) {
                    viewModel.sendPasswordResetEmail(email)
                } else {
                    warningMessage.show()
                }
            }
            passwordResetButtonBack.setOnClickListener {
                viewModel.back()
            }
        }
    }

    private fun updateUiState(newState: PasswordResetViewModel.PasswordResetState) {
        val dangerMessage = SnackbarBuilder(binding.content, layoutInflater, Snackbar.LENGTH_LONG)
            .setType(SnackbarBuilder.Type.DANGER)
            .setTitle(getString(R.string.message_title_sending_error))

        if (newState.isResetFailed) {
            dangerMessage.setText(newState.error?.message ?: "")
        }

        if (newState.isResetSuccess) {
            isEmailSent = true
            transform()
        }
    }

    private fun transform() {
        binding.apply {
            passwordResetEmailInput.visibility = View.GONE
            passwordResetInputHint.visibility = View.GONE

            passwordResetSentAnimation.visibility = View.VISIBLE
            passwordResetInfo.text = getString(R.string.password_reset_sent_letter_info)
            passwordResetButtonConfirm.text = getString(R.string.button_go_back)
            passwordResetButtonConfirm.setOnClickListener {
                viewModel.toAuth()
            }
        }
    }

    private fun isValidData(email: String): Boolean = email.isNotEmpty()
            && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}