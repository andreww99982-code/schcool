package com.schcool.trainer.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schcool.trainer.data.PhoneEntity
import com.schcool.trainer.data.ScenarioEntity
import com.schcool.trainer.data.TrainerRepository
import com.schcool.trainer.domain.ChatMessage
import com.schcool.trainer.domain.Competency
import com.schcool.trainer.domain.Difficulty
import com.schcool.trainer.domain.Role
import com.schcool.trainer.domain.ScoreBoard
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch


data class TrainerUiState(
    val role: Role? = null,
    val phones: List<PhoneEntity> = emptyList(),
    val scenarios: List<ScenarioEntity> = emptyList(),
    val selectedScenarioId: Long? = null,
    val selectedPhoneId: Long? = null,
    val currentQuestion: String = "",
    val helperTip: String = "",
    val chat: List<ChatMessage> = emptyList(),
    val scoreBoard: ScoreBoard = ScoreBoard(),
    val feedbackLog: List<String> = emptyList(),
    val round: Int = 0,
    val maxRounds: Int = 8,
    val loading: Boolean = true
)

@HiltViewModel
class TrainerViewModel @Inject constructor(
    private val repository: TrainerRepository
) : ViewModel() {
    var uiState by mutableStateOf(TrainerUiState())
        private set

    init {
        viewModelScope.launch {
            repository.ensureSeeded()
            combine(repository.observePhones(), repository.observeScenarios()) { phones, scenarios ->
                phones to scenarios
            }.collect { (phones, scenarios) ->
                uiState = uiState.copy(
                    phones = phones,
                    scenarios = scenarios,
                    selectedPhoneId = uiState.selectedPhoneId ?: phones.firstOrNull()?.id,
                    loading = false
                )
            }
        }
    }

    fun setRole(role: Role) {
        uiState = uiState.copy(role = role)
    }

    fun setScenario(scenarioId: Long, phoneId: Long) {
        uiState = uiState.copy(selectedScenarioId = scenarioId, selectedPhoneId = phoneId)
    }

    fun startSimulation() {
        val scenario = selectedScenario() ?: return
        val opener = "Клиент (${scenario.customerType}): Добрый день. Подберите телефон и объясните, почему он мне подходит."
        uiState = uiState.copy(
            chat = listOf(ChatMessage(UUID.randomUUID().toString(), true, opener)),
            currentQuestion = opener,
            helperTip = "",
            feedbackLog = emptyList(),
            scoreBoard = ScoreBoard(),
            round = 1
        )
    }

    fun requestHint() {
        viewModelScope.launch {
            val phone = selectedPhone() ?: return@launch
            val tip = repository.helperTip("${phone.brand} ${phone.model}")
            uiState = uiState.copy(helperTip = tip)
        }
    }

    fun submitAnswer(answer: String) {
        if (answer.isBlank() || uiState.round > uiState.maxRounds) return

        val feedback = repository.evaluate(answer)
        val updatedMap = uiState.scoreBoard.competencyPoints.toMutableMap()
        updatedMap[feedback.competency] = (updatedMap[feedback.competency] ?: 0) + feedback.deltaPoints

        val updatedScore = uiState.scoreBoard.copy(
            points = uiState.scoreBoard.points + feedback.deltaPoints.coerceAtLeast(0),
            penalties = uiState.scoreBoard.penalties + (-feedback.deltaPoints).coerceAtLeast(0),
            competencyPoints = updatedMap,
            criticalFailure = uiState.scoreBoard.criticalFailure || feedback.criticalFailure
        )

        val sellerMsg = ChatMessage(UUID.randomUUID().toString(), false, answer)

        viewModelScope.launch {
            val nextQuestion = if (uiState.round < uiState.maxRounds) {
                repository.nextClientQuestion(answer)
            } else {
                "Спасибо, я подумаю. Подведите итог по предложению."
            }
            val clientMsg = ChatMessage(UUID.randomUUID().toString(), true, nextQuestion)
            uiState = uiState.copy(
                scoreBoard = updatedScore,
                chat = uiState.chat + sellerMsg + clientMsg,
                feedbackLog = uiState.feedbackLog + feedback.explanation,
                currentQuestion = nextQuestion,
                round = uiState.round + 1
            )
        }
    }

    fun finishSimulation() {
        val scenario = selectedScenario() ?: return
        val role = uiState.role ?: return
        val score = uiState.scoreBoard.finalScore
        val passed = uiState.scoreBoard.passed
        val topWeakness = uiState.scoreBoard.competencyPoints.minByOrNull { it.value }?.key ?: Competency.CLOSING
        viewModelScope.launch {
            repository.saveAttempt(
                scenarioId = scenario.id,
                role = role,
                score = score,
                passed = passed,
                summary = "Слабая зона: $topWeakness. Итог: ${if (passed) "стажировка пройдена" else "стажировка не пройдена"}."
            )
        }
    }

    fun resetFlow() {
        uiState = uiState.copy(
            selectedScenarioId = null,
            helperTip = "",
            chat = emptyList(),
            feedbackLog = emptyList(),
            scoreBoard = ScoreBoard(),
            currentQuestion = "",
            round = 0
        )
    }

    private fun selectedScenario(): ScenarioEntity? = uiState.scenarios.firstOrNull { it.id == uiState.selectedScenarioId }
    private fun selectedPhone(): PhoneEntity? = uiState.phones.firstOrNull { it.id == uiState.selectedPhoneId }
}
