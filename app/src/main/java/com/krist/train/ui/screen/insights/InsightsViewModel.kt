package com.krist.train.ui.screen.insights

import androidx.lifecycle.ViewModel
import com.krist.train.AppContainer
import com.krist.train.domain.analysis.TrainingSummaryCalculator
import kotlinx.coroutines.flow.map

class InsightsViewModel(container: AppContainer) : ViewModel() {
    val summary = container.activityRepository.observeActivities()
        .map { TrainingSummaryCalculator().calculate(it) }
}
