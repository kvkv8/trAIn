package com.krist.train

import android.app.Application
import androidx.room.Room
import com.krist.train.core.config.AppConfig
import com.krist.train.core.network.NetworkModule
import com.krist.train.core.security.SecureSettingsStore
import com.krist.train.core.security.SecureTokenStore
import com.krist.train.data.local.AppDatabase
import com.krist.train.data.remote.ai.GeminiAiProvider
import com.krist.train.data.repository.ActivityRepository
import com.krist.train.data.repository.AuthRepository
import com.krist.train.data.repository.GoalRepository
import com.krist.train.data.repository.TrainingPlanRepository
import com.krist.train.domain.analysis.TrainingSummaryCalculator
import com.krist.train.domain.prompt.TrainingPlanPromptBuilder
import com.krist.train.domain.usecase.GenerateTrainingPlanUseCase
import com.krist.train.domain.usecase.GetTrainingSummaryUseCase
import com.krist.train.domain.usecase.RefreshPlanUseCase
import com.krist.train.domain.usecase.SyncStravaActivitiesUseCase
import com.krist.train.sync.SyncScheduler

class TrAInApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}

class AppContainer(application: Application) {
    private val network = NetworkModule()
    private val database = Room.databaseBuilder(application, AppDatabase::class.java, AppConfig.DATABASE_NAME).build()

    val tokenStore = SecureTokenStore(application)
    val settingsStore = SecureSettingsStore(application)

    val activityRepository = ActivityRepository(database.activityDao())
    val goalRepository = GoalRepository(database.goalDao())
    val trainingPlanRepository = TrainingPlanRepository(database.trainingPlanDao(), network.json)
    val authRepository = AuthRepository(network.stravaAuthApi, tokenStore, settingsStore)
    val geminiAiProvider = GeminiAiProvider(network.geminiApi)

    val getTrainingSummary = GetTrainingSummaryUseCase(
        activityRepository = activityRepository,
        calculator = TrainingSummaryCalculator(),
    )

    val syncStravaActivities = SyncStravaActivitiesUseCase(
        authRepository = authRepository,
        stravaApi = network.stravaApi,
        activityRepository = activityRepository,
    )

    val generateTrainingPlan = GenerateTrainingPlanUseCase(
        getTrainingSummary = getTrainingSummary,
        goalRepository = goalRepository,
        planRepository = trainingPlanRepository,
        aiProvider = geminiAiProvider,
        settingsStore = settingsStore,
        promptBuilder = TrainingPlanPromptBuilder(),
        json = network.json,
    )

    val refreshPlan = RefreshPlanUseCase(
        goalRepository = goalRepository,
        generateTrainingPlan = generateTrainingPlan,
    )

    val syncScheduler = SyncScheduler(application)
}
