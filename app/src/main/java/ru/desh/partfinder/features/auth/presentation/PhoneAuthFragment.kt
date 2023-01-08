package ru.desh.partfinder.features.auth.presentation

import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.google.android.material.snackbar.Snackbar
import ru.desh.partfinder.R
import ru.desh.partfinder.core.Screens
import ru.desh.partfinder.core.Screens.CodeEnter
import ru.desh.partfinder.core.di.SingleApplicationComponent
import ru.desh.partfinder.core.di.module.AppNavigation
import ru.desh.partfinder.core.ui.SnackbarBuilder
import ru.desh.partfinder.databinding.FragmentPhoneAuthBinding
import javax.inject.Inject

// TODO unify with RegistrationDataFragment and reuse
class PhoneAuthFragment: Fragment() {
    @Inject
    @AppNavigation
    lateinit var router: Router
    @Inject
    @AppNavigation
    lateinit var navigatorHolder: NavigatorHolder

    private lateinit var binding: FragmentPhoneAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SingleApplicationComponent.getInstance().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhoneAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            val warningMessage = SnackbarBuilder(content, layoutInflater, Snackbar.LENGTH_LONG)
                .setType(SnackbarBuilder.Type.WARNING).setTitle(getString(R.string.message_title_wrong_input))
            phoneAuthButtonSend.setOnClickListener {
                val phoneNumber = phoneAuthInput.editText?.text.toString()
                if (isValidPhoneNumber(phoneNumber)) {
                    router.navigateTo(CodeEnter(phoneNumber))
                } else {
                    warningMessage.setText(getString(R.string.message_text_wrong_phone_number))
                        .show()
                }
            }

            hintRegisterBlock.authButtonToRegister.setOnClickListener {
                router.navigateTo(Screens.Registration())
            }
        }
    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean =
        PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)
}