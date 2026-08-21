package com.schcool.trainer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.schcool.trainer.domain.Role
import com.schcool.trainer.ui.theme.OnYellow
import com.schcool.trainer.ui.theme.YellowDark
import com.schcool.trainer.ui.theme.YellowPrimary

@Composable
fun RoleScreen(
    state: TrainerUiState,
    onRoleSelected: (Role) -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "⭐ Schcool Sales Trainer",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = YellowDark
        )
        Text(
            "Выберите роль для игровой стажировки",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF5D4037)
        )

        Role.entries.forEach { role ->
            val selected = state.role == role
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (selected) YellowPrimary else MaterialTheme.colorScheme.surface
                ),
                onClick = { onRoleSelected(role) }
            ) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = if (role == Role.SELLER) "🎓 Продавец (trainee)" else "🛒 Покупатель (симулятор)",
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                    color = if (selected) OnYellow else Color(0xFF3A3A00)
                )
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onNext,
            enabled = state.role != null,
            contentPadding = PaddingValues(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = YellowPrimary,
                contentColor = OnYellow,
                disabledContainerColor = Color(0xFFE0E0E0),
                disabledContentColor = Color(0xFF9E9E9E)
            )
        ) {
            Text("Далее →", fontWeight = FontWeight.Bold)
        }
    }
}
