package com.schcool.trainer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.schcool.trainer.ui.theme.GoodGreen
import com.schcool.trainer.ui.theme.OnYellow
import com.schcool.trainer.ui.theme.YellowDark
import com.schcool.trainer.ui.theme.YellowPrimary

@Composable
fun SimulationScreen(
    state: TrainerUiState,
    onAskHint: () -> Unit,
    onSendAnswer: (String) -> Unit,
    onFinish: () -> Unit
) {
    var answer by rememberSaveable { mutableStateOf("") }

    fun sendAnswer() {
        if (answer.isNotBlank()) {
            onSendAnswer(answer)
            answer = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "⭐ Игровая сессия",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = YellowDark,
                modifier = Modifier.weight(1f)
            )
            Text(
                "Раунд ${state.round}/${state.maxRounds}",
                style = MaterialTheme.typography.labelLarge,
                color = YellowDark
            )
        }

        LinearProgressIndicator(
            progress = { (state.round.toFloat() / state.maxRounds.coerceAtLeast(1)) },
            modifier = Modifier.fillMaxWidth(),
            color = YellowPrimary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("💰 Очки: ${state.scoreBoard.finalScore}", fontWeight = FontWeight.Bold, color = OnYellow)
            if (state.scoreBoard.criticalFailure) {
                Text("⚠️ Критическая ошибка!", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("📱 Карточка модели", style = MaterialTheme.typography.titleMedium, color = YellowDark, fontWeight = FontWeight.Bold)
                val phone = state.phones.firstOrNull { it.id == state.selectedPhoneId }
                if (phone != null) {
                    Text("${phone.brand} ${phone.model} • ${phone.priceRub} ₽", fontWeight = FontWeight.SemiBold, color = Color(0xFF1A1A00))
                    Text("${phone.memory}, ${phone.camera}, ${phone.battery}", color = Color(0xFF3A3A00))
                    Text("✅ Плюсы: ${phone.advantages}", color = GoodGreen)
                    Text("⚠️ Ограничения: ${phone.limitations}", color = Color(0xFF8B6914))
                }
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(state.chat) { msg ->
                val isClient = msg.isClient
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isClient) Arrangement.Start else Arrangement.End
                ) {
                    Card(
                        modifier = Modifier.widthIn(max = 300.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isClient) MaterialTheme.colorScheme.primaryContainer else Color(0xFFE8F5E9)
                        ),
                        shape = RoundedCornerShape(
                            topStart = if (isClient) 4.dp else 12.dp,
                            topEnd = if (isClient) 12.dp else 4.dp,
                            bottomStart = 12.dp,
                            bottomEnd = 12.dp
                        )
                    ) {
                        Text(
                            modifier = Modifier.padding(10.dp),
                            text = (if (isClient) "🧑 Клиент: " else "🎓 Продавец: ") + msg.text,
                            color = Color(0xFF1A1A00)
                        )
                    }
                }
            }
        }

        if (state.helperTip.isNotBlank()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8DC))
            ) {
                Row(Modifier.padding(10.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("💡", style = MaterialTheme.typography.bodyMedium)
                    Text(state.helperTip, color = Color(0xFF7B5800), style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        // Input row: text field + send icon button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = answer,
                onValueChange = { answer = it },
                modifier = Modifier
                    .weight(1f)
                    .onKeyEvent { event ->
                        if (event.key == Key.Enter && event.type == KeyEventType.KeyUp) {
                            sendAnswer()
                            true
                        } else false
                    },
                label = { Text("Ваш ответ клиенту", color = Color(0xFF7B5800)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedBorderColor = YellowPrimary,
                    unfocusedBorderColor = YellowDark,
                    cursorColor = YellowDark,
                    focusedLabelColor = YellowDark
                ),
                singleLine = false,
                maxLines = 4
            )
            IconButton(
                onClick = { sendAnswer() },
                modifier = Modifier
                    .background(YellowPrimary, RoundedCornerShape(50))
                    .padding(4.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Отправить", tint = OnYellow)
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = onAskHint,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = OnYellow)
            ) { Text("💡 Подсказка") }
            Button(
                modifier = Modifier.weight(1f),
                onClick = { sendAnswer() },
                colors = ButtonDefaults.buttonColors(containerColor = YellowPrimary, contentColor = OnYellow)
            ) { Text("✅ Ответить") }
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onFinish,
            colors = ButtonDefaults.buttonColors(containerColor = YellowDark, contentColor = Color.White)
        ) {
            Text("🏁 Завершить стажировку")
        }
    }
}
