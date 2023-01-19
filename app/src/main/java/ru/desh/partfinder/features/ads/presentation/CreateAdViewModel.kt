package ru.desh.partfinder.features.ads.presentation

import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.desh.partfinder.core.Screens.CreateAd_Contacts
import ru.desh.partfinder.core.Screens.CreateAd_Description
import ru.desh.partfinder.core.Screens.CreateAd_Files
import ru.desh.partfinder.core.Screens.CreateAd_Target
import ru.desh.partfinder.core.Screens.Post_CreateAd
import ru.desh.partfinder.core.di.module.AppNavigation
import ru.desh.partfinder.core.di.module.CreateAdNavigation
import javax.inject.Inject

class CreateAdViewModel @Inject constructor(
    @AppNavigation private val router: Router,
    @CreateAdNavigation private val innerRouter: Router,
    @CreateAdNavigation private val innerNavigatorHolder: NavigatorHolder,
    private val _createAdState : MutableStateFlow<CreateAdState>
): ViewModel() {

    fun observeCreateAdState(): StateFlow<CreateAdState> = _createAdState

    fun initInnerNavigation(navigator: Navigator) = innerNavigatorHolder.setNavigator(navigator)

    fun clearInnerNavigation() = innerNavigatorHolder.removeNavigator()

    fun back() = router.exit()

    fun toCreateAdTarget() = innerRouter.navigateTo(CreateAd_Target())
    fun toCreateAdDescription() = innerRouter.navigateTo(CreateAd_Description())
    fun toCreateAdFiles() = innerRouter.navigateTo(CreateAd_Files())
    fun toCreateAdContacts() = innerRouter.navigateTo(CreateAd_Contacts())
    fun toPostCreateAd() = innerRouter.navigateTo(Post_CreateAd())
}