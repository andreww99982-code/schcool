package com.schcool.trainer.data

import com.schcool.trainer.domain.Competency
import com.schcool.trainer.domain.Role
import com.schcool.trainer.domain.RubricFeedback

interface LlmService {
    suspend fun generateQuestion(context: String, askedQuestions: List<String>, fromRole: Role): String
    suspend fun generateHelperTip(context: String): String
}

class TemplateLlmService : LlmService {
    override suspend fun generateQuestion(context: String, askedQuestions: List<String>, fromRole: Role): String {
        val source = if (fromRole == Role.BUYER) {
            SeedData.annoyingQuestionTemplates
        } else {
            SeedData.sellerQuestionTemplates
        }
        val remaining = source.filterNot { it in askedQuestions }
        return (if (remaining.isNotEmpty()) remaining else source).random()
    }

    override suspend fun generateHelperTip(context: String): String {
        return listOf(
            "Уточни сценарий использования: фото, игры, работа или автономность.",
            "Назови 1 ограничение модели честно и 2 ключевых преимущества.",
            "Предложи выбор: 2 модели под разный бюджет и сценарий.",
            "При возражении по цене переводи в срок владения и ценность за рубль.",
            "Используй технику «Да, и...»: соглашайся, затем добавляй аргумент.",
            "Задай уточняющий вопрос: «Для вас важнее автономность или скорость?»",
            "Закрывай конкретным предложением: «Давайте оформим — сделаю скидку на чехол.»",
            "Упомяни реальный кейс использования, близкий к ситуации клиента."
        ).random()
    }
}

class RubricScorer {
    fun evaluate(answer: String): RubricFeedback {
        val text = answer.lowercase().trim()

        // ── Critical failure patterns ──────────────────────────────────────
        val rudePhrases = listOf(
            "не знаю", "без понятия", "сами смотрите", "мне всё равно",
            "не важно", "как хотите", "читайте сами", "погуглите"
        )
        if (rudePhrases.any { text.contains(it) }) {
            return RubricFeedback(
                deltaPoints = -25,
                competency = Competency.OBJECTION_HANDLING,
                explanation = "❌ Критическая ошибка: ответ разрушает доверие клиента.",
                criticalFailure = true
            )
        }

        // Rude/dismissive tone
        val rudeTone = listOf("заткнись", "отстань", "мешаете", "надоели")
        if (rudeTone.any { text.contains(it) }) {
            return RubricFeedback(
                deltaPoints = -30,
                competency = Competency.OBJECTION_HANDLING,
                explanation = "❌ Грубость: недопустимый тон общения с клиентом.",
                criticalFailure = true
            )
        }

        // ── Scoring signals ────────────────────────────────────────────────
        val picks = mutableListOf<RubricFeedback>()

        // Needs discovery — asking clarifying questions
        val needsDiscoveryKeywords = listOf(
            "?", "какие", "как вы", "что важно", "для чего", "расскажите",
            "уточните", "сценари", "цель", "задача", "привычки", "пользоват",
            "вам нужно", "что планируете", "как часто", "чем пользуетесь"
        )
        val needsDiscoveryScore = needsDiscoveryKeywords.count { text.contains(it) }
        if (needsDiscoveryScore >= 2) {
            picks += RubricFeedback(16, Competency.NEEDS_DISCOVERY, "✅ Отлично: активное выявление потребностей клиента.")
        } else if (needsDiscoveryScore == 1) {
            picks += RubricFeedback(10, Competency.NEEDS_DISCOVERY, "👍 Хорошо: уточнение потребности клиента.")
        }

        // Product presentation — explaining value
        val presentationKeywords = listOf(
            "выгода", "подходит", "преиму", "лучше", "сценар", "реш", "помо",
            "отлично", "идеально", "специально", "оптимально", "подберём",
            "характеристик", "батарея", "камер", "памят", "экран", "процессор",
            "быстр", "качество", "надёжн", "долго", "хватит"
        )
        val presentationScore = presentationKeywords.count { text.contains(it) }
        if (presentationScore >= 3) {
            picks += RubricFeedback(18, Competency.PRODUCT_PRESENTATION, "✅ Отлично: развёрнутая презентация с конкретными аргументами.")
        } else if (presentationScore >= 1) {
            picks += RubricFeedback(12, Competency.PRODUCT_PRESENTATION, "👍 Хорошо: ценность модели объяснена.")
        }

        // Objection handling — empathy + reframing
        val objectionKeywords = listOf(
            "понима", "соглас", "давайте сравним", "честно", "именно поэтому",
            "зато", "при этом", "если посмотреть", "в перспектив", "разберём",
            "попробуем", "предлагаю рассмотреть", "да, и", "рассмотрим вместе",
            "справедливо", "хороший вопрос"
        )
        val objectionScore = objectionKeywords.count { text.contains(it) }
        if (objectionScore >= 2) {
            picks += RubricFeedback(20, Competency.OBJECTION_HANDLING, "✅ Отлично: эмпатия + грамотная отработка возражения.")
        } else if (objectionScore == 1) {
            picks += RubricFeedback(14, Competency.OBJECTION_HANDLING, "👍 Хорошо: корректная реакция на возражение.")
        }

        // Closing — moving toward purchase
        val closingKeywords = listOf(
            "оформ", "берем", "предлагаю", "выбираем", "оплатим", "покупаем",
            "договорились", "сделаем", "забронируем", "подготовлю", "давайте возьмём",
            "итого", "завершим", "отличный выбор", "скидку", "в подарок"
        )
        val closingScore = closingKeywords.count { text.contains(it) }
        if (closingScore >= 2) {
            picks += RubricFeedback(22, Competency.CLOSING, "✅ Отлично: уверенное закрытие сделки с призывом к действию.")
        } else if (closingScore == 1) {
            picks += RubricFeedback(15, Competency.CLOSING, "👍 Хорошо: выполнено закрытие сделки.")
        }

        // Bonus: structured answer (uses all key competencies)
        if (picks.size >= 3) {
            val best = picks.maxBy { it.deltaPoints }
            return best.copy(
                deltaPoints = best.deltaPoints + 5,
                explanation = best.explanation + " 🌟 Бонус: структурированный ответ."
            )
        }

        if (picks.isEmpty()) {
            return RubricFeedback(-8, Competency.PRODUCT_PRESENTATION, "❌ Слабый ответ: нет структуры, аргументов и уточняющих вопросов.")
        }
        return picks.maxBy { it.deltaPoints }
    }
}
