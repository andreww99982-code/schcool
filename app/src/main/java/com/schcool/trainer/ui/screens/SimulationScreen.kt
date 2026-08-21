package com.schcool.trainer.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SimulationScreen(
    state: TrainerUiState,
    onAskHint: () -> Unit,
    onSendAnswer: (String) -> Unit,
    onFinish: () -> Unit
) {
    var answer by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Игровая сессия продажи", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        LinearProgressIndicator(
            progress = { (state.round.toFloat() / state.maxRounds.coerceAtLeast(1)) },
            modifier = Modifier.fillMaxWidth()
        )
        Text("Очки: ${state.scoreBoard.finalScore}")

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Карточка модели", style = MaterialTheme.typography.titleMedium)
                val phone = state.phones.firstOrNull { it.id == state.selectedPhoneId }
                if (phone != null) {
                    Text("${phone.brand} ${phone.model} • ${phone.priceRub} ₽")
                    Text("${phone.memory}, ${phone.camera}, ${phone.battery}")
                    Text("Плюсы: ${phone.advantages}")
                    Text("Ограничения: ${phone.limitations}")
                }
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(state.chat) { msg ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = (if (msg.isClient) "Клиент: " else "Продавец: ") + msg.text
                    )
                }
            }
        }

        if (state.helperTip.isNotBlank()) {
            Text("Подсказка ИИ: ${state.helperTip}", color = MaterialTheme.colorScheme.primary)
        }

        OutlinedTextField(
            value = answer,
            onValueChange = { answer = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Ваш ответ клиенту") }
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            Button(modifier = Modifier.weight(1f), onClick = onAskHint) { Text("Подсказка") }
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    onSendAnswer(answer)
                    answer = ""
                }
            ) { Text("Ответить") }
        }

        Button(modifier = Modifier.fillMaxWidth(), onClick = onFinish) {
            Text("Завершить стажировку")
        }
    }
}
