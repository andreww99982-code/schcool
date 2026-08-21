package com.schcool.trainer.data

import com.schcool.trainer.domain.Role
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class TrainerRepository @Inject constructor(
    private val phoneDao: PhoneDao,
    private val scenarioDao: ScenarioDao,
    private val attemptDao: AttemptDao,
    private val llmService: LlmService,
    private val rubricScorer: RubricScorer
) {
    fun observePhones() = phoneDao.observePhones()
    fun observeScenarios() = scenarioDao.observeScenarios()
    fun observeAttempts() = attemptDao.observeAttempts()

    suspend fun ensureSeeded() {
        if (phoneDao.count() == 0) phoneDao.insertAll(SeedData.phones)
        if (scenarioDao.count() == 0) scenarioDao.insertAll(SeedData.scenarios)
    }

    suspend fun nextQuestion(context: String, askedQuestions: List<String>, fromRole: Role): String =
        llmService.generateQuestion(context, askedQuestions, fromRole)

    suspend fun helperTip(context: String): String = llmService.generateHelperTip(context)

    fun evaluate(answer: String) = rubricScorer.evaluate(answer)

    suspend fun saveAttempt(scenarioId: Long, role: Role, score: Int, passed: Boolean, summary: String) {
        attemptDao.insert(
            AttemptEntity(
                scenarioId = scenarioId,
                role = role.name,
                score = score,
                passed = passed,
                summary = summary,
                createdAt = System.currentTimeMillis()
            )
        )
    }
}
