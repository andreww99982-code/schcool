package com.schcool.trainer.domain

enum class Role { SELLER, BUYER }

enum class Difficulty { EASY, MEDIUM, HARD }

enum class Competency {
    NEEDS_DISCOVERY,
    PRODUCT_PRESENTATION,
    OBJECTION_HANDLING,
    CLOSING
}

data class ScoreBoard(
    val points: Int = 0,
    val penalties: Int = 0,
    val competencyPoints: Map<Competency, Int> = Competency.entries.associateWith { 0 },
    val criticalFailure: Boolean = false
) {
    val finalScore: Int get() = (points - penalties).coerceAtLeast(0)
    val passed: Boolean get() = !criticalFailure && finalScore >= 70
}

data class ChatMessage(
    val id: String,
    val isClient: Boolean,
    val text: String
)

data class RubricFeedback(
    val deltaPoints: Int,
    val competency: Competency,
    val explanation: String,
    val criticalFailure: Boolean = false
)
