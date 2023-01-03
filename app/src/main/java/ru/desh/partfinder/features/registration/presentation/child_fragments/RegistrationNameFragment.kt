package ru.desh.partfinder.features.registration.presentation.child_fragments

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
import ru.desh.partfinder.core.di.RegistrationNavigation
import ru.desh.partfinder.core.domain.model.User
import ru.desh.partfinder.core.domain.repository.UserRepository
import ru.desh.partfinder.core.ui.SnackbarBuilder
import ru.desh.partfinder.core.utils.DataOrErrorResult
import ru.desh.partfinder.databinding.FragmentRegistrationNameFormBinding
import ru.desh.partfinder.features.registration.presentation.RegistrationFragment
import ru.desh.partfinder.features.registration.presentation.RegistrationState
import java.util.UUID
import javax.inject.Inject

class RegistrationNameViewModel @Inject constructor(
    private val registrationState: MutableStateFlow<RegistrationState>,
    private val userRepository: UserRepository
): ViewModel() {
    fun createUser(name: String, surname: String, thirdName: String)
    : LiveData<DataOrErrorResult<Boolean,Exception>>{
        val user = User(UUID.randomUUID().toString(),
        "",
        name, surname, thirdName, "")
        return userRepository.createUser(user)
    }
    fun notifyUserCreated() {
        registrationState.value = RegistrationState.RegistrationFinished
    }
}

class RegistrationNameFragment: Fragment(){
    @Inject
    @RegistrationNavigation
    lateinit var router: Router
    @Inject
    @RegistrationNavigation
    lateinit var navigatorHolder: NavigatorHolder
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            val warningMessage = SnackbarBuilder(content, layoutInflater, Snackbar.LENGTH_LONG)
                .setType(SnackbarBuilder.Type.WARNING)
                .setTitle("Неверный ввод")
            val dangerMessage = SnackbarBuilder(content, layoutInflater, Snackbar.LENGTH_LONG)
                .setType(SnackbarBuilder.Type.DANGER)
                .setTitle("Ошибка")
            nameFormButtonSend.setOnClickListener {
                val name = nameFormNameInput.editText?.text.toString()
                val surname = nameFormSurnameInput.editText?.text.toString()
                val thirdName = nameFormThirdNameInput.editText?.text.toString()
                if (isValidInput(name, surname, thirdName)) {
                    viewModel.createUser(name, surname, thirdName).observe(viewLifecycleOwner) { result ->
                        if (!result.isException) {
                            viewModel.notifyUserCreated()
                        } else {
                            dangerMessage.setText(result.exception.toString())
                                .show()
                        }
                    }
                } else {
                    warningMessage.setText("Имя и фамилия не должны быть пустыми " +
                            "или содержать цифры или спец. знаки")
                        .show()
                }
            }
        }
    }

    private fun isValidInput(name: String, surname: String, thirdName: String): Boolean =
        name.chars().allMatch(Character::isLetter) && surname.chars().allMatch(Character::isLetter)
                && thirdName.chars().allMatch(Character::isLetter)
                && name.isNotEmpty() && surname.isNotEmpty() && thirdName.isNotEmpty()
}