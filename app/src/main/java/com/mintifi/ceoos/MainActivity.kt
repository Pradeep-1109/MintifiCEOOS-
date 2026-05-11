package com.mintifi.ceoos
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.mintifi.ceoos.ui.screens.*
import com.mintifi.ceoos.ui.theme.*
import com.mintifi.ceoos.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MintifiTheme { AppRoot(viewModel) } }
    }
}

sealed class Screen(val label: String, val icon: ImageVector) {
    object Dashboard : Screen("Home", Icons.Default.Home)
    object Record    : Screen("Record", Icons.Default.Mic)
    object Tasks     : Screen("Tasks", Icons.Default.List)
    object Todo      : Screen("To-Do", Icons.Default.CheckBox)
    object Reminders : Screen("Remind", Icons.Default.Alarm)
    object Copilot   : Screen("Copilot", Icons.Default.Psychology)
    object Settings  : Screen("Settings", Icons.Default.Settings)
}

@Composable
fun AppRoot(viewModel: MainViewModel) {
    var current by remember { mutableStateOf<Screen>(Screen.Dashboard) }
    Scaffold(
        containerColor = MintifiColors.Background,
        bottomBar = {
            NavigationBar(containerColor = MintifiColors.Surface, tonalElevation = 0.dp) {
                listOf(Screen.Dashboard, Screen.Record, Screen.Tasks, Screen.Todo,
                       Screen.Reminders, Screen.Copilot, Screen.Settings).forEach { screen ->
                    NavigationBarItem(
                        selected = current == screen,
                        onClick = { current = screen },
                        icon = { Icon(screen.icon, screen.label, modifier = Modifier.size(20.dp)) },
                        label = { Text(screen.label, style = MaterialTheme.typography.labelSmall) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MintifiColors.Accent,
                            selectedTextColor = MintifiColors.Accent,
                            unselectedIconColor = MintifiColors.TextMuted,
                            unselectedTextColor = MintifiColors.TextMuted,
                            indicatorColor = MintifiColors.Accent.copy(alpha = 0.12f)
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (current) {
                Screen.Dashboard -> DashboardScreen(viewModel,
                    { current = Screen.Record }, { current = Screen.Copilot },
                    { current = Screen.Todo }, { current = Screen.Reminders })
                Screen.Record    -> RecordScreen(viewModel)
                Screen.Tasks     -> TaskLogScreen(viewModel)
                Screen.Todo      -> TodoScreen(viewModel)
                Screen.Reminders -> RemindersScreen(viewModel)
                Screen.Copilot   -> CopilotScreen(viewModel)
                Screen.Settings  -> SettingsScreen()
            }
        }
    }
}
