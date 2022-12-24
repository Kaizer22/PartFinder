package ru.desh.partfinder.features.start.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.desh.partfinder.R
import ru.desh.partfinder.core.data.properties.PropertiesRepository
import ru.desh.partfinder.core.di.SingleApplicationComponent
import javax.inject.Inject

class WelcomeFragment: Fragment() {
    //TODO
    @Inject
    lateinit var propertiesRepository: PropertiesRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SingleApplicationComponent.getInstance().inject(this)
        GlobalScope.launch {
            propertiesRepository.setOnboardingFinished()
        }
    }
}