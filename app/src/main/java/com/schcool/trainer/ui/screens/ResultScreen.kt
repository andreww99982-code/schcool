package com.schcool.trainer.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.schcool.trainer.domain.Competency

@Composable
fun ResultScreen(
    state: TrainerUiState,
    onRestart: () -> Unit
) {
    val passed = state.scoreBoard.passed
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Результат стажировки", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(
            if (passed) "✅ Продажа успешна: стажировка пройдена" else "❌ Продажа не закрыта: стажировка не пройдена",
            style = MaterialTheme.typography.titleMedium
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Итоговые баллы: ${state.scoreBoard.finalScore}")
                Text("Выявление потребностей: ${state.scoreBoard.competencyPoints[Competency.NEEDS_DISCOVERY] ?: 0}")
                Text("Презентация: ${state.scoreBoard.competencyPoints[Competency.PRODUCT_PRESENTATION] ?: 0}")
                Text("Возражения: ${state.scoreBoard.competencyPoints[Competency.OBJECTION_HANDLING] ?: 0}")
                Text("Закрытие сделки: ${state.scoreBoard.competencyPoints[Competency.CLOSING] ?: 0}")
            }
        }

        Text("Разбор ответов", style = MaterialTheme.typography.titleMedium)
        state.feedbackLog.takeLast(6).forEach {
            Text("• $it")
        }

        Button(modifier = Modifier.fillMaxWidth(), onClick = onRestart) {
            Text("Новая попытка")
        }
    }
}
