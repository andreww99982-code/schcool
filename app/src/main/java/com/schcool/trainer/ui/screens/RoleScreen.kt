package com.schcool.trainer.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.schcool.trainer.domain.Role

@Composable
fun RoleScreen(
    state: TrainerUiState,
    onRoleSelected: (Role) -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Schcool Sales Trainer", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Выберите роль для игровой стажировки", style = MaterialTheme.typography.bodyLarge)

        Role.entries.forEach { role ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (state.role == role) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                ),
                onClick = { onRoleSelected(role) }
            ) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = if (role == Role.SELLER) "Продавец (trainee)" else "Покупатель (симулятор)"
                )
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onNext,
            enabled = state.role != null,
            contentPadding = PaddingValues(14.dp)
        ) {
            Text("Далее")
        }
    }
}
