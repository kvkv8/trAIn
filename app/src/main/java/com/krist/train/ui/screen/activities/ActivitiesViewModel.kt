package com.krist.train.ui.screen.activities

import androidx.lifecycle.ViewModel
import com.krist.train.AppContainer

class ActivitiesViewModel(container: AppContainer) : ViewModel() {
    val activities = container.activityRepository.observeActivities()
}
