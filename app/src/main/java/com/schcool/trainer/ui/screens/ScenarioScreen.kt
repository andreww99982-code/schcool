package com.schcool.trainer.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Выбор сценария", style = MaterialTheme.typography.headlineSmall)

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
            items(state.scenarios) { scenario ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (state.selectedScenarioId == scenario.id) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface
                    ),
                    onClick = {
                        if (defaultPhoneId != null) onScenarioSelected(scenario.id, defaultPhoneId)
                    }
                ) {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(scenario.name, style = MaterialTheme.typography.titleMedium)
                        Text("Тип клиента: ${scenario.customerType}")
                        Text("Сложность: ${scenario.difficulty}")
                        Text("Возражения: ${scenario.objections}")
                    }
                }
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onStart,
            enabled = state.selectedScenarioId != null
        ) {
            Text("Начать тренировку")
        }
    }
}
