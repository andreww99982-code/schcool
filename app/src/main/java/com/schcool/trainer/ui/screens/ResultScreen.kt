package com.schcool.trainer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.schcool.trainer.domain.Competency
import com.schcool.trainer.ui.theme.GoodGreen
import com.schcool.trainer.ui.theme.OnYellow
import com.schcool.trainer.ui.theme.YellowDark
import com.schcool.trainer.ui.theme.YellowPrimary

@Composable
fun ResultScreen(
    state: TrainerUiState,
    onRestart: () -> Unit
) {
    val passed = state.scoreBoard.passed
    val finalScore = state.scoreBoard.finalScore

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "🏆 Результат стажировки",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = YellowDark
        )

        // Pass/fail banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (passed) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
            )
        ) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    if (passed) "✅ Продажа успешна: стажировка пройдена!" else "❌ Продажа не закрыта: стажировка не пройдена.",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (passed) GoodGreen else MaterialTheme.colorScheme.error
                )
                Text(
                    when {
                        finalScore >= 80 -> "🌟 Превосходный результат! Вы настоящий профессионал продаж."
                        finalScore >= 50 -> "👍 Хороший результат. Есть точки роста."
                        finalScore >= 20 -> "📈 Удовлетворительно. Нужна практика по слабым зонам."
                        else -> "🔄 Низкий балл. Изучите материалы и попробуйте снова."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF3A3A00)
                )
            }
        }

        // Score summary
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("📊 Итоговые баллы: $finalScore", fontWeight = FontWeight.Bold, color = OnYellow)
                Text("Штрафы: ${state.scoreBoard.penalties}", color = MaterialTheme.colorScheme.error)

                val competencies = listOf(
                    Triple(Competency.NEEDS_DISCOVERY, "🔍 Выявление потребностей", state.scoreBoard.competencyPoints[Competency.NEEDS_DISCOVERY] ?: 0),
                    Triple(Competency.PRODUCT_PRESENTATION, "📱 Презентация", state.scoreBoard.competencyPoints[Competency.PRODUCT_PRESENTATION] ?: 0),
                    Triple(Competency.OBJECTION_HANDLING, "🛡️ Возражения", state.scoreBoard.competencyPoints[Competency.OBJECTION_HANDLING] ?: 0),
                    Triple(Competency.CLOSING, "🤝 Закрытие сделки", state.scoreBoard.competencyPoints[Competency.CLOSING] ?: 0)
                )
                competencies.forEach { (_, label, pts) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(label, color = Color(0xFF3A3A00))
                        val color = when {
                            pts >= 15 -> GoodGreen
                            pts >= 0 -> Color(0xFF8B6914)
                            else -> MaterialTheme.colorScheme.error
                        }
                        Text("$pts баллов", color = color, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        // Weakness hint
        val weakestEntry = state.scoreBoard.competencyPoints.minByOrNull { it.value }
        if (weakestEntry != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8DC))
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text("💡 Зона роста", fontWeight = FontWeight.Bold, color = YellowDark)
                    val weakLabel = when (weakestEntry.key) {
                        Competency.NEEDS_DISCOVERY -> "Выявление потребностей"
                        Competency.PRODUCT_PRESENTATION -> "Презентация"
                        Competency.OBJECTION_HANDLING -> "Отработка возражений"
                        Competency.CLOSING -> "Закрытие сделки"
                    }
                    val weakTip = when (weakestEntry.key) {
                        Competency.NEEDS_DISCOVERY -> "Больше задавайте уточняющих вопросов: «Как вы используете телефон?», «Что вам важно?»"
                        Competency.PRODUCT_PRESENTATION -> "Называйте конкретные характеристики и связывайте их с выгодой клиента."
                        Competency.OBJECTION_HANDLING -> "Признавайте возражение, затем приводите контраргумент: «Да, цена выше, зато...»"
                        Competency.CLOSING -> "Заканчивайте конкретным предложением: «Давайте оформим?» или «Беру этот?»"
                    }
                    Text(weakLabel, color = Color(0xFF7B5800), fontWeight = FontWeight.Medium)
                    Text(weakTip, color = Color(0xFF5D4037), style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        // Feedback log
        if (state.feedbackLog.isNotEmpty()) {
            Text(
                "📝 Разбор ответов",
                style = MaterialTheme.typography.titleMedium,
                color = YellowDark,
                fontWeight = FontWeight.Bold
            )
            state.feedbackLog.takeLast(8).forEach { fb ->
                val isGood = fb.startsWith("✅") || fb.startsWith("👍")
                val isBad = fb.startsWith("❌") || fb.startsWith("⚠️")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            when {
                                isGood -> Color(0xFFE8F5E9)
                                isBad -> Color(0xFFFFEBEE)
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            },
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        fb,
                        style = MaterialTheme.typography.bodySmall,
                        color = when {
                            isGood -> GoodGreen
                            isBad -> MaterialTheme.colorScheme.error
                            else -> Color(0xFF3A3A00)
                        }
                    )
                }
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onRestart,
            colors = ButtonDefaults.buttonColors(containerColor = YellowPrimary, contentColor = OnYellow)
        ) {
            Text("🔄 Новая попытка", fontWeight = FontWeight.Bold)
        }
    }
}
