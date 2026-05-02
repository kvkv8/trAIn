package com.krist.train.ui.screen.home

import androidx.lifecycle.ViewModel
import com.krist.train.AppContainer

class HomeViewModel(container: AppContainer) : ViewModel() {
    val activities = container.activityRepository.observeActivities()
    val latestGoal = container.goalRepository.observeLatestGoal()
    val latestPlan = container.trainingPlanRepository.observeLatestPlan()
}
