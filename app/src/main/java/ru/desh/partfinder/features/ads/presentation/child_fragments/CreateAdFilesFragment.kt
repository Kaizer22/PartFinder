package ru.desh.partfinder.features.ads.presentation.child_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.MutableStateFlow
import ru.desh.partfinder.core.Screens
import ru.desh.partfinder.core.di.module.CreateAdNavigation
import ru.desh.partfinder.core.domain.model.Ad
import ru.desh.partfinder.core.domain.model.AdFile
import ru.desh.partfinder.core.domain.model.AdMedia
import ru.desh.partfinder.databinding.FragmentCreateAdFilesBinding
import ru.desh.partfinder.databinding.FragmentCreateAdTargetBinding
import ru.desh.partfinder.features.ads.di.BufferAd
import ru.desh.partfinder.features.ads.presentation.CreateAdFragment
import ru.desh.partfinder.features.ads.presentation.CreateAdState
import javax.inject.Inject

class CreateAdFilesViewModel @Inject constructor(
    @BufferAd private val bufferAd: Ad,
    private val creationState: MutableStateFlow<CreateAdState>
): ViewModel() {
    fun setVideo(link: String, contentDescription: String){
        // TODO not yet implemented
    }

    fun uploadMedia(){
        // TODO not yet implemented
    }
    fun setMedia(media: List<AdMedia>) {
        bufferAd.media = media
    }

    fun uploadFile(){
        // TODO not yet implemented
    }
    fun setFiles(files: List<AdFile>) {
        bufferAd.adFiles = files
    }

    fun notifyFilesSubmitted(){
        creationState.value = CreateAdState.FilesSubmitted
    }
}

class CreateAdFilesFragment: Fragment() {

    @Inject
    lateinit var viewModel: CreateAdFilesViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (parentFragment as CreateAdFragment).createAdComponent.inject(this)
    }

    private lateinit var binding: FragmentCreateAdFilesBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateAdFilesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            createAdFilesButtonNext.setOnClickListener {
                viewModel.notifyFilesSubmitted()
            }
        }
    }
}