package ru.desh.partfinder.features.ads.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.terrakok.cicerone.*
import com.github.terrakok.cicerone.androidx.AppNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.desh.partfinder.R
import ru.desh.partfinder.core.Screens
import ru.desh.partfinder.core.Screens.CreateAd_Contacts
import ru.desh.partfinder.core.Screens.CreateAd_Description
import ru.desh.partfinder.core.Screens.CreateAd_Files
import ru.desh.partfinder.core.Screens.CreateAd_Target
import ru.desh.partfinder.core.Screens.Post_CreateAd
import ru.desh.partfinder.core.di.SingleApplicationComponent
import ru.desh.partfinder.core.di.module.AppNavigation
import ru.desh.partfinder.core.di.module.CreateAdNavigation
import ru.desh.partfinder.core.domain.model.Ad
import ru.desh.partfinder.databinding.FragmentCreateAdBinding
import ru.desh.partfinder.features.BottomNavigationActivity
import ru.desh.partfinder.features.ads.di.CreateAdComponent
import ru.desh.partfinder.features.ads.presentation.child_fragments.*
import javax.inject.Inject


class CreateAdFragment: Fragment() {

    @Inject
    @AppNavigation
    lateinit var router: Router
    @Inject
    @AppNavigation
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    @CreateAdNavigation
    lateinit var innerRouter: Router
    @Inject
    @CreateAdNavigation
    lateinit var innerNavigatorHolder: NavigatorHolder

    private val navigator = object : Navigator {
        override fun applyCommands(commands: Array<out Command>) {
            for (command in commands) applyCommand(command)
        }
        private fun applyCommand(command: Command) {
            when(command){
                is Back -> {}
                is Forward -> {
                    when(command.screen.screenKey) {
                        Post_CreateAd().screenKey -> changeStage(
                            postCreateAdFragment
                        )
                        CreateAd_Target().screenKey -> changeStage(
                            createAdTargetFragment
                        )
                        CreateAd_Description().screenKey -> changeStage(
                            createAdDescriptionFragment
                        )
                        CreateAd_Files().screenKey -> changeStage(
                            createAdFilesFragment
                        )
                        CreateAd_Contacts().screenKey -> changeStage(
                            createAdContactsFragment
                        )
                    }
                }
            }
        }
        private fun changeStage(fragment: Fragment) {
            childFragmentManager.beginTransaction()
                .replace(R.id.create_ad_container, fragment).commit()
        }
    }

    private lateinit var binding: FragmentCreateAdBinding

    lateinit var createAdComponent: CreateAdComponent
        private set

    private val buildingAd = Ad()
    private val _createAdState = MutableStateFlow<CreateAdState>(CreateAdState.InitState)
    val createAdState: StateFlow<CreateAdState> = _createAdState

    private val createAdTargetFragment = CreateAdTargetFragment()
    private val createAdDescriptionFragment = CreateAdDescriptionFragment(buildingAd)
    private val createAdFilesFragment = CreateAdFilesFragment(buildingAd)
    private val createAdContactsFragment = CreateAdContactsFragment(buildingAd)
    private val postCreateAdFragment = PostCreateAdFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createAdComponent = SingleApplicationComponent.getInstance()
            .createAdComponentFactory()
            .create(_createAdState)
        createAdComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateAdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            createAdButtonBack.setOnClickListener {
                router.exit()
            }
        }
        innerRouter.navigateTo(Screens.Registration_Method())
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                createAdState.collect { createAdState ->
                    when(createAdState) {
                        is CreateAdState.InitState -> {}
                        is CreateAdState.TargetSubmitted -> {

                        }
                        is CreateAdState.DescriptionSubmitted -> {

                        }
                        is CreateAdState.FilesSubmitted -> {

                        }
                        is CreateAdState.AdPublished -> {

                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        innerNavigatorHolder.setNavigator(navigator)
        (activity as BottomNavigationActivity).hideNavigation()
    }

    override fun onPause() {
        innerNavigatorHolder.removeNavigator()
        super.onPause()
    }
}