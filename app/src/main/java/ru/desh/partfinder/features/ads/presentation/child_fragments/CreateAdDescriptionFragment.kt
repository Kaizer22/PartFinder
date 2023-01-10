package ru.desh.partfinder.features.ads.presentation.child_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.MutableStateFlow
import ru.desh.partfinder.core.Screens.CreateAd_Files
import ru.desh.partfinder.core.di.module.CreateAdNavigation
import ru.desh.partfinder.core.domain.model.Ad
import ru.desh.partfinder.core.domain.model.UserContact
import ru.desh.partfinder.databinding.FragmentCreateAdDescriptionBinding
import ru.desh.partfinder.features.ads.di.BufferAd
import ru.desh.partfinder.features.ads.presentation.CreateAdFragment
import ru.desh.partfinder.features.ads.presentation.CreateAdState
import javax.inject.Inject

class CreateAdDescriptionViewModel @Inject constructor(
    @BufferAd private val bufferAd: Ad,
    private val creationState: MutableStateFlow<CreateAdState>
): ViewModel() {
    fun setDescription(title: String, description: String){
        bufferAd.title = title
        bufferAd.description = description
    }
    fun notifyDescriptionSubmitted(){
        creationState.value = CreateAdState.DescriptionSubmitted
    }
}

class CreateAdDescriptionFragment: Fragment()  {

    @Inject
    lateinit var viewModel: CreateAdDescriptionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (parentFragment as CreateAdFragment).createAdComponent.inject(this)
    }

    private lateinit var binding: FragmentCreateAdDescriptionBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateAdDescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            createAdDescriptionButtonClear.setOnClickListener {
                createAdDescriptionInput.editText?.setText("")
            }
            createAdDescriptionButtonNext.setOnClickListener {
                val title = createAdDescriptionTitleInput.editText?.text.toString()
                val description = createAdDescriptionInput.editText?.text.toString()
                viewModel.setDescription(title, description)
                viewModel.notifyDescriptionSubmitted()
            }
        }
    }
}