package ru.desh.partfinder.features.ads.presentation.child_fragments

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
import ru.desh.partfinder.core.domain.model.Ad
import ru.desh.partfinder.core.domain.model.UserContact
import ru.desh.partfinder.core.domain.repository.AdRepository
import ru.desh.partfinder.core.ui.SnackbarBuilder
import ru.desh.partfinder.databinding.FragmentCreateAdContactsBinding
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

    data class CreateAdContactsState(
        val isAdCreated: Boolean = false,
        val isCreationFailed: Boolean = false,
        val error: Exception? = null
    )

    private val _createAdContactsState = MutableLiveData(CreateAdContactsState())
    val createAdContactsState: LiveData<CreateAdContactsState> = _createAdContactsState

    fun createAd() {
        bufferAd.creationTimestamp = Date().time
        viewModelScope.launch {
            val res = adRepository.createAd(bufferAd)
            if (!res.isException) {
                _createAdContactsState.value = _createAdContactsState.value?.copy(
                    isAdCreated = true
                )
                this@CreateAdContactsViewModel.notifyAdPublished()
            } else {
                _createAdContactsState.value = _createAdContactsState.value?.copy(
                    isCreationFailed = true,
                    error = res.exception
                )
            }
        }
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
            dangerMessage = SnackbarBuilder(content, layoutInflater, Snackbar.LENGTH_LONG)
                .setType(SnackbarBuilder.Type.DANGER)
                .setTitle(getString(R.string.message_title_error))

            viewModel.createAdContactsState.observe(viewLifecycleOwner) { newState ->
                updateUiState(newState)
            }
            createAdContactsButtonNext.setOnClickListener {
                viewModel.createAd()
            }
        }
    }

    private lateinit var dangerMessage: SnackbarBuilder
    private fun updateUiState(newState: CreateAdContactsViewModel.CreateAdContactsState) {
        if (newState.isCreationFailed) {
            dangerMessage.setText(newState.error?.message ?: "")
                .show()
        }
    }
}