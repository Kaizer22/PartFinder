package ru.desh.partfinder.features.registration.presentation.child_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.desh.partfinder.R
import ru.desh.partfinder.core.domain.model.User
import ru.desh.partfinder.core.domain.repository.UserRepository
import ru.desh.partfinder.core.ui.SnackbarBuilder
import ru.desh.partfinder.databinding.FragmentRegistrationNameFormBinding
import ru.desh.partfinder.features.registration.presentation.RegistrationFragment
import ru.desh.partfinder.features.registration.presentation.RegistrationState
import java.util.*
import javax.inject.Inject

class RegistrationNameViewModel @Inject constructor(
    private val registrationState: MutableStateFlow<RegistrationState>,
    private val userRepository: UserRepository
) : ViewModel() {

    data class RegistrationNameState(
        val isUserCreated: Boolean = false,
        val isUserCreationFailed: Boolean = false,
        val error: Exception? = null
    )

    private val _registrationNameState = MutableLiveData(RegistrationNameState())
    val registrationNameState: LiveData<RegistrationNameState> = _registrationNameState

    fun createUser(name: String, surname: String, thirdName: String) {
        val user = User(
            UUID.randomUUID().toString(),
            "",
            name, surname, thirdName, "", emptyList()
        )
        viewModelScope.launch {
            val res = userRepository.createUser(user)
            if (!res.isException) {
                _registrationNameState.value = _registrationNameState.value?.copy(
                    isUserCreated = true
                )
                this@RegistrationNameViewModel.notifyUserCreated()
            } else {
                _registrationNameState.value = _registrationNameState.value?.copy(
                    isUserCreationFailed = true,
                    error = res.exception
                )
            }
        }
    }

    private fun notifyUserCreated() {
        registrationState.value = RegistrationState.RegistrationFinished
    }
}

class RegistrationNameFragment : Fragment() {
    @Inject
    lateinit var viewModel: RegistrationNameViewModel

    private lateinit var binding: FragmentRegistrationNameFormBinding

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
        binding = FragmentRegistrationNameFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var dangerMessage: SnackbarBuilder
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            val warningMessage = SnackbarBuilder(content, layoutInflater, Snackbar.LENGTH_LONG)
                .setType(SnackbarBuilder.Type.WARNING)
                .setTitle(getString(R.string.message_title_wrong_input))
            dangerMessage = SnackbarBuilder(content, layoutInflater, Snackbar.LENGTH_LONG)
                .setType(SnackbarBuilder.Type.DANGER)
                .setTitle(getString(R.string.message_title_error))

            viewModel.registrationNameState.observe(viewLifecycleOwner) { newState ->
                updateUiState(newState)
            }

            nameFormButtonSend.setOnClickListener {
                val name = nameFormNameInput.text.toString()
                val surname = nameFormSurnameInput.text.toString()
                val thirdName = nameFormThirdNameInput.text.toString()
                if (isValidInput(name, surname, thirdName)) {
                    viewModel.createUser(name, surname, thirdName)
                } else {
                    warningMessage.setText(getString(R.string.message_text_invalid_name))
                        .show()
                }
            }
        }
    }

    private fun updateUiState(newState: RegistrationNameViewModel.RegistrationNameState) {
        if (newState.isUserCreationFailed) {
            dangerMessage.setText(newState.error?.message ?: "")
            .show()
        }
    }

    private fun isValidInput(name: String, surname: String, thirdName: String): Boolean =
        name.chars().allMatch(Character::isLetter) && surname.chars().allMatch(Character::isLetter)
                && thirdName.chars().allMatch(Character::isLetter)
                && name.isNotEmpty() && surname.isNotEmpty() && thirdName.isNotEmpty()
}