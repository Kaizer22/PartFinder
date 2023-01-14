package ru.desh.partfinder.features.ads.presentation.child_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import ru.desh.partfinder.R
import ru.desh.partfinder.core.domain.model.Ad
import ru.desh.partfinder.core.domain.model.AdType
import ru.desh.partfinder.databinding.FragmentCreateAdTargetBinding
import ru.desh.partfinder.features.ads.di.BufferAd
import ru.desh.partfinder.features.ads.presentation.CreateAdFragment
import ru.desh.partfinder.features.ads.presentation.CreateAdState
import javax.inject.Inject

class CreateAdTargetViewModel @Inject constructor(
    @BufferAd private val bufferAd: Ad,
    private val creationState: MutableStateFlow<CreateAdState>
) : ViewModel() {

    fun setType(adType: String) {
        bufferAd.type = adType
    }

    fun setTarget(adTarget: String) {
        bufferAd.target = adTarget
    }

    fun notifyTargetSubmitted() {
        creationState.value = CreateAdState.TargetSubmitted
    }
}

class CreateAdTargetFragment : Fragment() {

    @Inject
    lateinit var viewModel: CreateAdTargetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (parentFragment as CreateAdFragment).createAdComponent.inject(this)
    }

    private lateinit var binding: FragmentCreateAdTargetBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateAdTargetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            createAdTargetButtonClear.setOnClickListener {
                createAdTargetInput.editText?.setText("")
            }
            createAdTargetButtonNext.setOnClickListener {
                val target = createAdTargetInput.editText?.text.toString()
                viewModel.setTarget(target)
                viewModel.notifyTargetSubmitted()
            }
            createAdTargetRadioGroup.setOnCheckedChangeListener { radioGroup, _ ->
                when (radioGroup.checkedRadioButtonId) {
                    R.id.create_ad_target_suggest_investments -> viewModel
                        .setType(AdType.SUGGEST_INVESTMENTS.value)
                    R.id.create_ad_target_suggest_services -> viewModel
                        .setType(AdType.SUGGEST_SERVICES.value)
                    R.id.create_ad_target_look_business_partner -> viewModel
                        .setType(AdType.LOOK_BUSINESS_PARTNER.value)
                    R.id.create_ad_target_look_investments -> viewModel
                        .setType(AdType.LOOK_INVESTMENTS.value)
                    R.id.create_ad_target_look_supplier -> viewModel
                        .setType(AdType.LOOK_SUPPLIER.value)
                }
            }

        }
    }

}