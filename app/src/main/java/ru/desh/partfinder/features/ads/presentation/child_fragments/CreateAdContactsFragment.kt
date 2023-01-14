package ru.desh.partfinder.features.ads.presentation.child_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Router
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.MutableStateFlow
import ru.desh.partfinder.R
import ru.desh.partfinder.core.Screens
import ru.desh.partfinder.core.di.module.CreateAdNavigation
import ru.desh.partfinder.core.domain.model.Ad
import ru.desh.partfinder.core.domain.model.UserContact
import ru.desh.partfinder.core.domain.repository.AdRepository
import ru.desh.partfinder.core.ui.SnackbarBuilder
import ru.desh.partfinder.core.utils.DataOrErrorResult
import ru.desh.partfinder.databinding.FragmentCreateAdContactsBinding
import ru.desh.partfinder.databinding.FragmentCreateAdFilesBinding
import ru.desh.partfinder.databinding.FragmentPrivacyPolicyBinding
import ru.desh.partfinder.features.ads.di.BufferAd
import ru.desh.partfinder.features.ads.presentation.CreateAdFragment
import ru.desh.partfinder.features.ads.presentation.CreateAdState
import java.util.*
import javax.inject.Inject

class CreateAdContactsViewModel @Inject constructor(
    @BufferAd private val bufferAd: Ad,
    private val creationState: MutableStateFlow<CreateAdState>,
    private val adRepository: AdRepository
) : ViewModel() {

    fun createAd(): LiveData<DataOrErrorResult<
            Boolean?, Exception?>> {
        bufferAd.creationTimestamp = Date().time
        return adRepository.createAd(bufferAd)
    }

    fun setContacts(contacts: List<UserContact>) {
        bufferAd.userContacts = contacts
    }

    fun notifyAdPublished() {
        creationState.value = CreateAdState.AdPublished
    }
}

class CreateAdContactsFragment : Fragment() {
    @Inject
    lateinit var viewModel: CreateAdContactsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (parentFragment as CreateAdFragment).createAdComponent.inject(this)
    }

    private lateinit var binding: FragmentCreateAdContactsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateAdContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            val dangerMessage = SnackbarBuilder(content, layoutInflater, Snackbar.LENGTH_LONG)
                .setType(SnackbarBuilder.Type.DANGER)
                .setTitle(getString(R.string.message_title_error))
            createAdContactsButtonNext.setOnClickListener {
                viewModel.createAd().observe(viewLifecycleOwner) {
                    if (!it.isException) {
                        viewModel.notifyAdPublished()
                    } else {
                        dangerMessage.setText(it.exception?.message.toString())
                            .show()
                    }
                }
            }
        }
    }
}