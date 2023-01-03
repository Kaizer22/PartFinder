package ru.desh.partfinder.features.auth.presentation

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Router
import com.google.android.material.snackbar.Snackbar
import ru.desh.partfinder.core.Screens.Auth
import ru.desh.partfinder.core.di.AppNavigation
import ru.desh.partfinder.core.di.SingleApplicationComponent
import ru.desh.partfinder.core.domain.repository.AuthRepository
import ru.desh.partfinder.core.ui.SnackbarBuilder
import ru.desh.partfinder.core.utils.DataOrErrorResult
import ru.desh.partfinder.databinding.FragmentPasswordResetBinding
import javax.inject.Inject

class PasswordResetViewModel @Inject constructor(
    val authRepository: AuthRepository
): ViewModel() {
    fun sendPasswordResetEmail(email: String):
            LiveData<DataOrErrorResult<Boolean, Exception?>> {
        return authRepository.sendPasswordResetEmail(email)
    }
}

class PasswordResetFragment: Fragment() {
    @Inject
    @AppNavigation
    lateinit var router: Router

    @Inject
    lateinit var viewModel: PasswordResetViewModel

    private lateinit var binding: FragmentPasswordResetBinding

    private var isEmailSent = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SingleApplicationComponent.getInstance().inject(this)
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
                .setTitle("Ошибка")
                .setText("Проверьте корректность ввода email")
            val dangerMessage = SnackbarBuilder(content, layoutInflater, Snackbar.LENGTH_LONG)
                .setType(SnackbarBuilder.Type.DANGER)
                .setTitle("Ошибка при отправке")
            passwordResetButtonConfirm.setOnClickListener {
                val email = passwordResetEmailInput.editText?.text.toString()
                if (isValidData(email)) {
                    viewModel.sendPasswordResetEmail(email).observe(viewLifecycleOwner) { result ->
                        if (!result.isException) {
                            isEmailSent = true
                            transform()
                        } else {
                            dangerMessage.setText(result.exception.toString())
                        }
                    }
                } else {
                    warningMessage.show()
                }
            }
        }
    }

    private fun transform() {
        binding.apply {
            passwordResetEmailInput.visibility = View.GONE
            passwordResetInputHint.visibility = View.GONE

            passwordResetSentAnimation.visibility = View.VISIBLE
            passwordResetInfo.text = "Мы выслали письмо на указанный адрес. Доставка письма может " +
                    "занимать от нескольких секунд до минут. Если письмо не приходит, не забудьте " +
                    "проверить “Спам”"
            passwordResetButtonConfirm.text = "Вернуться"
            passwordResetButtonConfirm.setOnClickListener {
                 router.navigateTo(Auth())
            }
        }
    }

    private fun isValidData(email: String): Boolean = email.isNotEmpty()
            && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}