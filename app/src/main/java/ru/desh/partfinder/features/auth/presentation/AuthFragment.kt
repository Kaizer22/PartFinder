package ru.desh.partfinder.features.auth.presentation

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.desh.partfinder.R
import ru.desh.partfinder.core.di.SingleApplicationComponent
import ru.desh.partfinder.core.ui.SnackbarBuilder
import ru.desh.partfinder.databinding.FragmentAuthBinding
import javax.inject.Inject

class AuthFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: AuthViewModel

    private lateinit var binding: FragmentAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SingleApplicationComponent.getInstance().inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[AuthViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            val infoMessage = SnackbarBuilder(content, layoutInflater, Snackbar.LENGTH_LONG)
                .setType(SnackbarBuilder.Type.SECONDARY)
                .setTitle(getString(R.string.message_title_todo))
                .setText(getString(R.string.message_text_todo))
            val warningMessage = SnackbarBuilder(content, layoutInflater, Snackbar.LENGTH_LONG)
                .setType(SnackbarBuilder.Type.WARNING)
                .setTitle(getString(R.string.message_title_error))
                .setText(getString(R.string.message_text_auth_incorrect_email_or_password))

            viewModel.authState.observe(viewLifecycleOwner) { newState ->
                updateUiState(newState)
            }

            authButtonSignIn.setOnClickListener {
                val email = authEmailInput.text.toString()
                val password = authPasswordInput.text.toString()
                hideInput()
                if (isValidInput(email, password)) {
                    //TODO show loading
                    viewModel.authWithEmailAndPassword(email, password)
                } else {
                    warningMessage.show()
                }
            }
            textButtonForgotPassword.setOnClickListener {
                viewModel.toPasswordReset()
            }
            authButtonPhone.setOnClickListener {
                viewModel.toPhoneAuth()
            }
            authButtonGoogle.setOnClickListener {
                infoMessage.show()
            }
            authButtonFacebook.setOnClickListener {
                infoMessage.show()
            }
            hintRegisterBlock.authButtonToRegister.setOnClickListener {
                viewModel.toRegistration()
            }
        }
    }

    private fun updateUiState(newState: AuthViewModel.AuthState) {
        binding.apply {
            val successMessage = SnackbarBuilder(content, layoutInflater, Snackbar.LENGTH_LONG)
                .setType(SnackbarBuilder.Type.PRIMARY)
                .setTitle(getString(R.string.message_title_sign_in_success))
                .setText(getString(R.string.message_text_sign_in_success))

            val dangerMessage = SnackbarBuilder(content, layoutInflater, Snackbar.LENGTH_LONG)
                .setType(SnackbarBuilder.Type.DANGER)
                .setTitle(getString(R.string.message_title_auth_error))

//            if (newState.isLoading) {
//                showLoading()
//            } else {
//                hideLoading()
//            }

            if (newState.signedIn) {
                successMessage.show()
                viewModel.toBottomNavigation()
            }

            if (newState.signInFailed) {
                dangerMessage
                    .setText(newState.error?.message ?: "")
                    .show()
            }
        }
    }

    private fun hideLoading() {
        TODO("Not yet implemented")
    }

    private fun showLoading() {
        TODO("Not yet implemented")
    }

    private fun isValidInput(email: String, password: String): Boolean =
        email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && password.length >= 8

    private fun hideInput() {
        this@AuthFragment.activity?.currentFocus?.let {
            val imm =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view?.windowToken, 0)
        }
    }
}