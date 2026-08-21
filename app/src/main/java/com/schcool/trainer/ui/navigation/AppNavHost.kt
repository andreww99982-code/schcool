package com.schcool.trainer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.schcool.trainer.ui.screens.ResultScreen
import com.schcool.trainer.ui.screens.RoleScreen
import com.schcool.trainer.ui.screens.ScenarioScreen
import com.schcool.trainer.ui.screens.SimulationScreen
import com.schcool.trainer.ui.screens.TrainerViewModel

@Composable
fun AppNavHost(viewModel: TrainerViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "role") {
        composable("role") {
            RoleScreen(
                state = viewModel.uiState,
                onRoleSelected = viewModel::setRole,
                onNext = { navController.navigate("scenario") }
            )
        }
        composable("scenario") {
            ScenarioScreen(
                state = viewModel.uiState,
                onScenarioSelected = viewModel::setScenario,
                onStart = {
                    viewModel.startSimulation()
                    navController.navigate("simulation")
                }
            )
        }
        composable("simulation") {
            SimulationScreen(
                state = viewModel.uiState,
                onAskHint = viewModel::requestHint,
                onSendAnswer = viewModel::submitAnswer,
                onFinish = {
                    viewModel.finishSimulation()
                    navController.navigate("result")
                }
            )
        }
        composable("result") {
            ResultScreen(
                state = viewModel.uiState,
                onRestart = {
                    viewModel.resetFlow()
                    navController.popBackStack("role", inclusive = false)
                }
            )
        }
    }
}
