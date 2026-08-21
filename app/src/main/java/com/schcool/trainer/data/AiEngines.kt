package com.schcool.trainer.data

import com.schcool.trainer.domain.Competency
import com.schcool.trainer.domain.RubricFeedback

interface LlmService {
    suspend fun generateQuestion(context: String): String
    suspend fun generateHelperTip(context: String): String
}

class TemplateLlmService : LlmService {
    override suspend fun generateQuestion(context: String): String {
        return SeedData.annoyingQuestionTemplates.random()
    }

    override suspend fun generateHelperTip(context: String): String {
        return listOf(
            "Сначала уточни сценарий использования: фото, игры, работа, автономность.",
            "Дай честный ответ: назови 1 ограничение модели и 2 преимущества.",
            "Закрой на выбор: предложи 2 модели под разный бюджет.",
            "При возражении по цене переведи в ценность и срок владения."
        ).random()
    }
}

class RubricScorer {
    fun evaluate(answer: String): RubricFeedback {
        val text = answer.lowercase()
        val hasQuestion = text.contains("?") || text.contains("какие") || text.contains("как") || text.contains("что важно")
        val hasValue = listOf("выгода", "подходит", "преиму", "лучше", "сценар").any { text.contains(it) }
        val hasObjectionHandling = listOf("понима", "соглас", "давайте сравним", "честно").any { text.contains(it) }
        val hasClosing = listOf("оформ", "берем", "предлагаю", "выбираем").any { text.contains(it) }
        val rude = listOf("не знаю", "без понятия", "сами смотрите").any { text.contains(it) }

        if (rude) {
            return RubricFeedback(
                deltaPoints = -20,
                competency = Competency.OBJECTION_HANDLING,
                explanation = "Критичная ошибка: ответ разрушает доверие клиента.",
                criticalFailure = true
            )
        }

        val picks = mutableListOf<RubricFeedback>()
        if (hasQuestion) picks += RubricFeedback(12, Competency.NEEDS_DISCOVERY, "Хорошо: уточнение потребности клиента.")
        if (hasValue) picks += RubricFeedback(15, Competency.PRODUCT_PRESENTATION, "Хорошо: объяснена ценность модели.")
        if (hasObjectionHandling) picks += RubricFeedback(16, Competency.OBJECTION_HANDLING, "Хорошо: корректная отработка возражения.")
        if (hasClosing) picks += RubricFeedback(18, Competency.CLOSING, "Хорошо: выполнено закрытие сделки.")

        if (picks.isEmpty()) {
            return RubricFeedback(-8, Competency.PRODUCT_PRESENTATION, "Недостаточно структуры: добавьте аргументы и уточнения.")
        }
        return picks.maxBy { it.deltaPoints }
    }
}
