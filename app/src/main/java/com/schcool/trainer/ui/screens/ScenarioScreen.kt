package com.schcool.trainer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.schcool.trainer.ui.theme.OnYellow
import com.schcool.trainer.ui.theme.YellowDark
import com.schcool.trainer.ui.theme.YellowPrimary

@Composable
fun ScenarioScreen(
    state: TrainerUiState,
    onScenarioSelected: (Long, Long) -> Unit,
    onStart: () -> Unit
) {
    val defaultPhoneId = state.phones.firstOrNull()?.id
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "📋 Выбор сценария",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = YellowDark
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
            items(state.scenarios) { scenario ->
                val selected = state.selectedScenarioId == scenario.id
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selected) YellowPrimary else MaterialTheme.colorScheme.surface
                    ),
                    onClick = {
                        if (defaultPhoneId != null) onScenarioSelected(scenario.id, defaultPhoneId)
                    }
                ) {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            scenario.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                            color = if (selected) OnYellow else Color(0xFF1A1A00)
                        )
                        Text(
                            "👤 Тип клиента: ${scenario.customerType}",
                            color = if (selected) OnYellow else Color(0xFF3A3A00)
                        )
                        Text(
                            "📊 Сложность: ${scenario.difficulty}",
                            color = if (selected) OnYellow else Color(0xFF5D4037)
                        )
                        Text(
                            "💬 Возражения: ${scenario.objections}",
                            color = if (selected) OnYellow else Color(0xFF8B6914)
                        )
                    }
                }
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onStart,
            enabled = state.selectedScenarioId != null,
            colors = ButtonDefaults.buttonColors(
                containerColor = YellowPrimary,
                contentColor = OnYellow,
                disabledContainerColor = Color(0xFFE0E0E0),
                disabledContentColor = Color(0xFF9E9E9E)
            )
        ) {
            Text("🚀 Начать тренировку", fontWeight = FontWeight.Bold)
        }
    }
}
