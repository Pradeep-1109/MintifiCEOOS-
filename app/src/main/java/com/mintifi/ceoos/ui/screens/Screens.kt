package com.mintifi.ceoos.ui.screens

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import com.google.accompanist.permissions.*
import com.mintifi.ceoos.R
import com.mintifi.ceoos.data.model.*
import com.mintifi.ceoos.ui.components.*
import com.mintifi.ceoos.ui.theme.MintifiColors
import com.mintifi.ceoos.viewmodel.MainViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.launch

// ── Dashboard ─────────────────────────────────────────────────────────────────

@Composable
fun DashboardScreen(viewModel: MainViewModel, onNavigateToRecord: () -> Unit, onNavigateToCopilot: () -> Unit, onNavigateToTodo: () -> Unit, onNavigateToReminders: () -> Unit) {
    val stats           by viewModel.dashboardStats.collectAsState()
    val allTasks        by viewModel.allTasks.collectAsState()
    val allSessions     by viewModel.allSessions.collectAsState()
    val todayTodos      by viewModel.todayTodos.collectAsState()
    val activeReminders by viewModel.activeRemindersCount.collectAsState()
    val dailyBriefing   by viewModel.dailyBriefing.collectAsState()
    val lastSummary     by viewModel.lastSummary.collectAsState()
    val offlineCount    by viewModel.offlineQueueCount.collectAsState()
    val isOnline        by viewModel.isOnline.collectAsState()
    var showBriefing    by remember { mutableStateOf(false) }
    var showSummary     by remember { mutableStateOf(false) }
    var taskToDelete    by remember { mutableStateOf<Task?>(null) }
    val fmt             = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault())

    LaunchedEffect(lastSummary) { if (lastSummary != null) showSummary = true }

    // Delete task confirmation dialog
    taskToDelete?.let { task ->
        AlertDialog(
            onDismissRequest = { taskToDelete = null },
            title = { Text("Delete Task?") },
            text = { Text("\"" + task.title + "\" will be permanently deleted.") },
            confirmButton = { TextButton(onClick = { viewModel.deleteTask(task); taskToDelete = null }, colors = ButtonDefaults.textButtonColors(contentColor = MintifiColors.DangerRed)) { Text("Delete") } },
            dismissButton = { TextButton(onClick = { taskToDelete = null }) { Text("Cancel") } }
        )
    }

    Column(Modifier.fillMaxSize().background(MintifiColors.Background).verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {

        // Header with logo
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = R.drawable.mintifi_logo), contentDescription = "Mintifi", modifier = Modifier.height(28.dp))
            Spacer(Modifier.weight(1f))
            val h = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            val greet = when { h < 12 -> "morning"; h < 17 -> "afternoon"; else -> "evening" }
            Text("Good $greet, Pradeep", style = MaterialTheme.typography.bodyMedium, color = MintifiColors.TextSecondary)
        }

        // Offline banner
        if (!isOnline || offlineCount > 0) {
            Card(colors = CardDefaults.cardColors(containerColor = if (!isOnline) MintifiColors.DangerRed.copy(alpha = 0.1f) else MintifiColors.WarnAmber.copy(alpha = 0.1f)), border = BorderStroke(0.5.dp, if (!isOnline) MintifiColors.DangerRed.copy(alpha = 0.5f) else MintifiColors.WarnAmber.copy(alpha = 0.5f)), shape = RoundedCornerShape(10.dp)) {
                Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(if (!isOnline) Icons.Default.WifiOff else Icons.Default.CloudUpload, null, tint = if (!isOnline) MintifiColors.DangerRed else MintifiColors.WarnAmber, modifier = Modifier.size(18.dp))
                    Column(Modifier.weight(1f)) {
                        Text(if (!isOnline) "No internet connection" else "$offlineCount recording(s) waiting to process", style = MaterialTheme.typography.titleSmall, color = if (!isOnline) MintifiColors.DangerRed else MintifiColors.WarnAmber)
                        if (!isOnline) Text("Recordings will be queued and processed when connected", style = MaterialTheme.typography.bodySmall, color = MintifiColors.TextSecondary)
                    }
                    if (offlineCount > 0 && isOnline) {
                        TextButton(onClick = { viewModel.processOfflineQueue() }, colors = ButtonDefaults.textButtonColors(contentColor = MintifiColors.WarnAmber)) { Text("Process") }
                    }
                }
            }
        }

        // Summary card
        if (showSummary && lastSummary != null) {
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)), shape = RoundedCornerShape(12.dp), border = BorderStroke(0.5.dp, MintifiColors.Accent.copy(alpha = 0.4f))) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Summarize, null, tint = MintifiColors.Accent, modifier = Modifier.size(16.dp)); Spacer(Modifier.width(6.dp))
                        Text("Meeting Summary", style = MaterialTheme.typography.titleSmall, color = MintifiColors.Accent); Spacer(Modifier.weight(1f))
                        IconButton(onClick = { showSummary = false }, modifier = Modifier.size(24.dp)) { Icon(Icons.Default.Close, null, tint = MintifiColors.TextMuted, modifier = Modifier.size(16.dp)) }
                    }
                    Text(lastSummary!!, style = MaterialTheme.typography.bodySmall, color = MintifiColors.TextPrimary)
                }
            }
        }

        // Title + briefing
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("CEO OS", style = MaterialTheme.typography.headlineMedium, color = MintifiColors.TextPrimary)
                Text("Mintifi Finserv  |  Chief of Staff", style = MaterialTheme.typography.bodySmall, color = MintifiColors.TextSecondary)
            }
            OutlinedButton(onClick = { viewModel.generateBriefing(); showBriefing = true }, border = BorderStroke(1.dp, MintifiColors.Accent.copy(alpha = 0.6f)), colors = ButtonDefaults.outlinedButtonColors(contentColor = MintifiColors.Accent), shape = RoundedCornerShape(10.dp)) {
                Icon(Icons.Default.WbSunny, null, Modifier.size(14.dp)); Spacer(Modifier.width(4.dp)); Text("Briefing")
            }
        }

        if (showBriefing) {
            Card(colors = CardDefaults.cardColors(containerColor = MintifiColors.Surface), shape = RoundedCornerShape(12.dp), border = BorderStroke(0.5.dp, MintifiColors.Accent.copy(alpha = 0.3f))) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.WbSunny, null, tint = MintifiColors.Accent, modifier = Modifier.size(16.dp)); Spacer(Modifier.width(8.dp))
                        Text("Daily Briefing", style = MaterialTheme.typography.titleMedium, color = MintifiColors.Accent); Spacer(Modifier.weight(1f))
                        IconButton(onClick = { showBriefing = false }, modifier = Modifier.size(24.dp)) { Icon(Icons.Default.Close, null, tint = MintifiColors.TextMuted, modifier = Modifier.size(16.dp)) }
                    }
                    dailyBriefing?.let { Text(it, style = MaterialTheme.typography.bodyMedium, color = MintifiColors.TextPrimary) }
                        ?: CircularProgressIndicator(Modifier.size(16.dp), color = MintifiColors.Accent, strokeWidth = 2.dp)
                }
            }
        }

        // Stats
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            DStatCard("P0 Critical", stats.p0Count.toString(), MintifiColors.DangerRed, Modifier.weight(1f))
            DStatCard("P1 High", stats.p1Count.toString(), MintifiColors.WarnAmber, Modifier.weight(1f))
            DStatCard("Total", stats.total.toString(), MintifiColors.Accent, Modifier.weight(1f))
        }

        // Quick actions
        Text("QUICK ACTIONS", style = MaterialTheme.typography.labelSmall, color = MintifiColors.TextMuted, fontFamily = FontFamily.Monospace, letterSpacing = 1.sp)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DActionBtn("Record CEO Call", MintifiColors.Accent, Color.White, Modifier.weight(1f), onNavigateToRecord)
            DActionBtn("AI Copilot", MintifiColors.Surface, MintifiColors.Accent, Modifier.weight(1f), onNavigateToCopilot)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DActionBtn("Today Tasks", MintifiColors.Surface, MintifiColors.InfoBlue, Modifier.weight(1f), onNavigateToTodo)
            DActionBtn("Reminders ($activeReminders)", MintifiColors.Surface, MintifiColors.AccentBlue, Modifier.weight(1f), onNavigateToReminders)
        }

        // Critical tasks with delete
        val critical = allTasks.filter { it.priority == Priority.P0 || it.priority == Priority.P1 }.take(5)
        if (critical.isNotEmpty()) {
            Text("CRITICAL CEO ASKS", style = MaterialTheme.typography.labelSmall, color = MintifiColors.TextMuted, fontFamily = FontFamily.Monospace, letterSpacing = 1.sp)
            critical.forEach { task ->
                Card(colors = CardDefaults.cardColors(containerColor = MintifiColors.Surface), shape = RoundedCornerShape(10.dp), border = BorderStroke(0.5.dp, MintifiColors.Border), modifier = Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(8.dp).background(if (task.priority == Priority.P0) MintifiColors.DangerRed else MintifiColors.WarnAmber, CircleShape))
                        Column(Modifier.weight(1f)) {
                            Text(task.title, style = MaterialTheme.typography.bodyMedium, color = MintifiColors.TextPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(task.deadline, style = MaterialTheme.typography.labelSmall, color = MintifiColors.WarnAmber, fontFamily = FontFamily.Monospace)
                                Text("·", color = MintifiColors.TextMuted)
                                Text(SimpleDateFormat("dd MMM HH:mm", Locale.getDefault()).format(Date(task.createdAt)), style = MaterialTheme.typography.labelSmall, color = MintifiColors.TextMuted, fontFamily = FontFamily.Monospace)
                            }
                        }
                        IconButton(onClick = { taskToDelete = task }, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.Delete, "Delete", tint = MintifiColors.DangerRed.copy(alpha = 0.6f), modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        // Recent sessions with hide/delete
        if (allSessions.isNotEmpty()) {
            Text("RECENT SESSIONS", style = MaterialTheme.typography.labelSmall, color = MintifiColors.TextMuted, fontFamily = FontFamily.Monospace, letterSpacing = 1.sp)
            allSessions.take(5).forEach { s ->
                Card(colors = CardDefaults.cardColors(containerColor = MintifiColors.Surface), shape = RoundedCornerShape(10.dp), border = BorderStroke(0.5.dp, MintifiColors.Border), modifier = Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(if (s.status == SessionStatus.DONE) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked, null, tint = if (s.status == SessionStatus.DONE) MintifiColors.SuccessGreen else MintifiColors.TextMuted, modifier = Modifier.size(18.dp))
                        Column(Modifier.weight(1f)) {
                            Text(s.title.ifBlank { "Untitled" }, style = MaterialTheme.typography.titleSmall, color = MintifiColors.TextPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(fmt.format(Date(s.date)), style = MaterialTheme.typography.labelSmall, color = MintifiColors.TextMuted, fontFamily = FontFamily.Monospace)
                                if (s.taskCount > 0) Text(s.taskCount.toString() + " tasks", style = MaterialTheme.typography.labelSmall, color = MintifiColors.Accent, fontFamily = FontFamily.Monospace)
                            }
                        }
                        IconButton(onClick = { viewModel.hideSession(s.id) }, modifier = Modifier.size(28.dp)) { Icon(Icons.Default.VisibilityOff, "Hide", tint = MintifiColors.TextMuted, modifier = Modifier.size(14.dp)) }
                        IconButton(onClick = { viewModel.deleteSession(s) }, modifier = Modifier.size(28.dp)) { Icon(Icons.Default.Delete, "Delete", tint = MintifiColors.DangerRed.copy(alpha = 0.7f), modifier = Modifier.size(14.dp)) }
                    }
                }
            }
        }
        Spacer(Modifier.height(80.dp))
    }
}

@Composable private fun DStatCard(label: String, value: String, color: Color, modifier: Modifier) { Card(modifier, colors=CardDefaults.cardColors(containerColor=MintifiColors.Surface), shape=RoundedCornerShape(10.dp), border=BorderStroke(0.5.dp,MintifiColors.Border)){ Column(Modifier.padding(12.dp)){ Text(label,style=MaterialTheme.typography.labelSmall,color=MintifiColors.TextMuted,fontFamily=FontFamily.Monospace); Spacer(Modifier.height(4.dp)); Text(value,style=MaterialTheme.typography.headlineMedium,color=color) } } }
@Composable private fun DActionBtn(label: String, bg: Color, fg: Color, modifier: Modifier, onClick: () -> Unit) { Card(modifier.clickable{onClick()},colors=CardDefaults.cardColors(containerColor=bg),shape=RoundedCornerShape(10.dp),border=BorderStroke(0.5.dp,MintifiColors.Border)){ Box(Modifier.fillMaxWidth().padding(14.dp),contentAlignment=Alignment.Center){ Text(label,style=MaterialTheme.typography.titleSmall,color=fg) } } }

// ── Record Screen ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RecordScreen(viewModel: MainViewModel) {
    val context     = LocalContext.current
    val isRecording by viewModel.isRecording.collectAsState()
    val isPaused    by viewModel.isPaused.collectAsState()
    val duration    by viewModel.recordingDur.collectAsState()
    val amplitude   by viewModel.amplitude.collectAsState()
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val uiMessage   by viewModel.uiMessage.collectAsState()
    val lastSummary by viewModel.lastSummary.collectAsState()
    val isOnline    by viewModel.isOnline.collectAsState()
    val offlineCount by viewModel.offlineQueueCount.collectAsState()
    var contextNote by remember { mutableStateOf("") }
    var showContext by remember { mutableStateOf(false) }
    var showText    by remember { mutableStateOf(false) }
    var manualText  by remember { mutableStateOf("") }
    val micPerm = rememberPermissionState(Manifest.permission.RECORD_AUDIO)
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { context.contentResolver.openInputStream(it)?.use { inp -> val tmp = File(context.cacheDir,"up_"+System.currentTimeMillis()+".m4a"); tmp.outputStream().use{out->inp.copyTo(out)}; viewModel.processAudioFile(tmp, contextNote) } }
    }

    Column(Modifier.fillMaxSize().background(MintifiColors.Background).verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Mic, null, tint = MintifiColors.Accent, modifier = Modifier.size(20.dp)); Spacer(Modifier.width(8.dp))
            Column {
                Text("CEO Call Capture", style = MaterialTheme.typography.headlineSmall, color = MintifiColors.TextPrimary)
                Text("Hindi + English supported  •  Offline queue enabled", style = MaterialTheme.typography.bodySmall, color = MintifiColors.TextSecondary)
            }
        }

        // Offline indicator
        if (!isOnline) {
            Card(colors = CardDefaults.cardColors(containerColor = MintifiColors.WarnAmber.copy(alpha = 0.1f)), border = BorderStroke(0.5.dp, MintifiColors.WarnAmber.copy(alpha = 0.5f)), shape = RoundedCornerShape(8.dp)) {
                Row(Modifier.padding(10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.WifiOff, null, tint = MintifiColors.WarnAmber, modifier = Modifier.size(16.dp))
                    Text("Offline mode — recordings will be queued and processed when connected", style = MaterialTheme.typography.bodySmall, color = MintifiColors.WarnAmber)
                }
            }
        }

        if (offlineCount > 0 && isOnline) {
            Card(colors = CardDefaults.cardColors(containerColor = MintifiColors.SuccessGreen.copy(alpha = 0.1f)), border = BorderStroke(0.5.dp, MintifiColors.SuccessGreen.copy(alpha = 0.5f)), shape = RoundedCornerShape(8.dp)) {
                Row(Modifier.padding(10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CloudUpload, null, tint = MintifiColors.SuccessGreen, modifier = Modifier.size(16.dp))
                    Text("$offlineCount queued recording(s) ready to process", style = MaterialTheme.typography.bodySmall, color = MintifiColors.SuccessGreen, modifier = Modifier.weight(1f))
                    TextButton(onClick = { viewModel.processOfflineQueue() }, colors = ButtonDefaults.textButtonColors(contentColor = MintifiColors.SuccessGreen)) { Text("Process Now") }
                }
            }
        }

        uiMessage?.let { msg ->
            Card(colors=CardDefaults.cardColors(containerColor=MintifiColors.Accent.copy(alpha=0.1f)), border=BorderStroke(0.5.dp,MintifiColors.Accent.copy(alpha=0.4f)), shape=RoundedCornerShape(8.dp)) {
                Row(Modifier.padding(12.dp), horizontalArrangement=Arrangement.spacedBy(10.dp), verticalAlignment=Alignment.CenterVertically) {
                    if(isAnalyzing) CircularProgressIndicator(Modifier.size(14.dp),color=MintifiColors.Accent,strokeWidth=2.dp)
                    else Icon(Icons.Default.CheckCircle,null,tint=MintifiColors.Accent,modifier=Modifier.size(16.dp))
                    Text(msg, style=MaterialTheme.typography.bodyMedium, color=MintifiColors.Accent)
                }
            }
        }

        if (lastSummary != null && !isAnalyzing) {
            Card(colors=CardDefaults.cardColors(containerColor=Color(0xFFEFF6FF)), border=BorderStroke(0.5.dp,MintifiColors.Accent.copy(alpha=0.4f)), shape=RoundedCornerShape(10.dp)) {
                Column(Modifier.padding(14.dp), verticalArrangement=Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment=Alignment.CenterVertically) { Icon(Icons.Default.Summarize,null,tint=MintifiColors.Accent,modifier=Modifier.size(16.dp)); Spacer(Modifier.width(6.dp)); Text("Meeting Summary",style=MaterialTheme.typography.titleSmall,color=MintifiColors.Accent) }
                    Text(lastSummary!!, style=MaterialTheme.typography.bodySmall, color=MintifiColors.TextPrimary)
                }
            }
        }

        // Record card
        Card(colors=CardDefaults.cardColors(containerColor=MintifiColors.Surface), shape=RoundedCornerShape(12.dp), border=BorderStroke(0.5.dp,if(isRecording)MintifiColors.Accent.copy(alpha=0.4f) else MintifiColors.Border)) {
            Column(Modifier.padding(16.dp), horizontalAlignment=Alignment.CenterHorizontally) {
                Row(horizontalArrangement=Arrangement.spacedBy(8.dp), verticalAlignment=Alignment.CenterVertically) {
                    Box(Modifier.size(8.dp).background(when{isRecording&&!isPaused->MintifiColors.DangerRed;isPaused->MintifiColors.WarnAmber;else->MintifiColors.TextMuted},CircleShape))
                    Text(if(isPaused)"PAUSED" else if(isRecording)"REC" else "READY", style=MaterialTheme.typography.labelMedium, color=if(isRecording&&!isPaused)MintifiColors.DangerRed else MintifiColors.TextMuted)
                    Spacer(Modifier.weight(1f))
                    val m=TimeUnit.MILLISECONDS.toMinutes(duration)%60; val s=TimeUnit.MILLISECONDS.toSeconds(duration)%60
                    Text("%02d:%02d".format(m,s), style=MaterialTheme.typography.headlineSmall, color=MintifiColors.TextPrimary)
                }
                Spacer(Modifier.height(12.dp))
                WaveformVisualizer(amplitude=amplitude, isRecording=isRecording&&!isPaused)
                Spacer(Modifier.height(20.dp))

                // Buttons — PAUSE BUTTON IS NOW 64dp (was 52dp)
                Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.spacedBy(16.dp,Alignment.CenterHorizontally), verticalAlignment=Alignment.CenterVertically) {
                    when {
                        !isRecording -> {
                            Button(onClick={if(micPerm.status.isGranted)viewModel.startRecording() else micPerm.launchPermissionRequest()}, modifier=Modifier.size(80.dp), shape=CircleShape, colors=ButtonDefaults.buttonColors(containerColor=MintifiColors.Accent)) { Icon(Icons.Default.Mic,null,tint=Color.White,modifier=Modifier.size(32.dp)) }
                            OutlinedButton(onClick={picker.launch("audio/*")}, shape=RoundedCornerShape(12.dp), border=BorderStroke(1.dp,MintifiColors.Border), modifier=Modifier.height(52.dp)) { Icon(Icons.Default.UploadFile,null,Modifier.size(20.dp)); Spacer(Modifier.width(6.dp)); Text("Upload Audio") }
                        }
                        isPaused -> {
                            // Resume — 64dp
                            OutlinedButton(onClick={viewModel.resumeRecording()}, shape=CircleShape, modifier=Modifier.size(64.dp), border=BorderStroke(2.dp,MintifiColors.AccentBlue)) { Icon(Icons.Default.PlayArrow,null,tint=MintifiColors.AccentBlue,modifier=Modifier.size(28.dp)) }
                            // Stop — 80dp
                            Button(onClick={viewModel.stopAndProcess(contextNote)}, shape=CircleShape, modifier=Modifier.size(80.dp), colors=ButtonDefaults.buttonColors(containerColor=MintifiColors.DangerRed)) { Icon(Icons.Default.Stop,null,tint=Color.White,modifier=Modifier.size(32.dp)) }
                        }
                        else -> {
                            // PAUSE — now 64dp (was 52dp) and border is 2dp (was 0.5dp) — much more visible
                            OutlinedButton(onClick={viewModel.pauseRecording()}, shape=CircleShape, modifier=Modifier.size(64.dp), border=BorderStroke(2.dp,MintifiColors.WarnAmber)) { Icon(Icons.Default.Pause,null,tint=MintifiColors.WarnAmber,modifier=Modifier.size(28.dp)) }
                            // Stop — 80dp
                            Button(onClick={viewModel.stopAndProcess(contextNote)}, shape=CircleShape, modifier=Modifier.size(80.dp), colors=ButtonDefaults.buttonColors(containerColor=MintifiColors.DangerRed)) { Icon(Icons.Default.Stop,null,tint=Color.White,modifier=Modifier.size(32.dp)) }
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                // Button labels
                if (isRecording) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)) {
                        Text(if(isPaused)"Resume" else "Pause", style=MaterialTheme.typography.labelSmall, color=MintifiColors.TextMuted, modifier=Modifier.width(64.dp), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                        Text("Stop & Analyze", style=MaterialTheme.typography.labelSmall, color=MintifiColors.TextMuted, modifier=Modifier.width(80.dp), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    }
                }
            }
        }

        Row(Modifier.clickable{showContext=!showContext}, verticalAlignment=Alignment.CenterVertically) { Icon(if(showContext)Icons.Default.ExpandLess else Icons.Default.ExpandMore,null,tint=MintifiColors.TextSecondary,modifier=Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Add context note (optional)",style=MaterialTheme.typography.bodyMedium,color=MintifiColors.TextSecondary) }
        AnimatedVisibility(showContext) { OutlinedTextField(value=contextNote,onValueChange={contextNote=it},placeholder={Text("Topic, speakers, projects...",color=MintifiColors.TextMuted)},modifier=Modifier.fillMaxWidth().height(80.dp),colors=OutlinedTextFieldDefaults.colors(focusedBorderColor=MintifiColors.Accent,unfocusedBorderColor=MintifiColors.Border,focusedTextColor=MintifiColors.TextPrimary,unfocusedTextColor=MintifiColors.TextPrimary,cursorColor=MintifiColors.Accent,focusedContainerColor=MintifiColors.Surface,unfocusedContainerColor=MintifiColors.Surface),shape=RoundedCornerShape(10.dp)) }
        Divider(color=MintifiColors.Border,thickness=0.5.dp)
        Row(Modifier.clickable{showText=!showText},verticalAlignment=Alignment.CenterVertically){Icon(Icons.Default.Keyboard,null,tint=MintifiColors.TextMuted,modifier=Modifier.size(16.dp));Spacer(Modifier.width(6.dp));Text("Type or paste discussion (Hindi/English)",style=MaterialTheme.typography.bodySmall,color=MintifiColors.TextMuted)}
        AnimatedVisibility(showText) {
            Column(verticalArrangement=Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(value=manualText,onValueChange={manualText=it},placeholder={Text("Type in Hindi, English, or both...",color=MintifiColors.TextMuted)},modifier=Modifier.fillMaxWidth().height(140.dp),colors=OutlinedTextFieldDefaults.colors(focusedBorderColor=MintifiColors.Accent,unfocusedBorderColor=MintifiColors.Border,focusedTextColor=MintifiColors.TextPrimary,unfocusedTextColor=MintifiColors.TextPrimary,cursorColor=MintifiColors.Accent,focusedContainerColor=MintifiColors.Surface,unfocusedContainerColor=MintifiColors.Surface),shape=RoundedCornerShape(10.dp))
                Button(onClick={if(manualText.isNotBlank()){viewModel.analyzeTextOnly(manualText,contextNote);manualText="";showText=false}},enabled=manualText.isNotBlank()&&!isAnalyzing,colors=ButtonDefaults.buttonColors(containerColor=MintifiColors.Accent),shape=RoundedCornerShape(8.dp)){Text("Analyze Text",color=Color.White,fontWeight=FontWeight.SemiBold)}
            }
        }
    }
}

// ── Task Log with Edit + Delete ───────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskLogScreen(viewModel: MainViewModel) {
    val tasks  by viewModel.filteredTasks.collectAsState()
    val filter by viewModel.taskFilter.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var taskToEdit   by remember { mutableStateOf<Task?>(null) }
    var taskToDelete by remember { mutableStateOf<Task?>(null) }
    val fmt = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault())

    // Delete confirmation
    taskToDelete?.let { task ->
        AlertDialog(
            onDismissRequest = { taskToDelete = null },
            title = { Text("Delete Task?") },
            text = { Text("\"" + task.title + "\" will be permanently deleted.") },
            confirmButton = { TextButton(onClick = { viewModel.deleteTask(task); taskToDelete = null }, colors = ButtonDefaults.textButtonColors(contentColor = MintifiColors.DangerRed)) { Text("Delete") } },
            dismissButton = { TextButton(onClick = { taskToDelete = null }) { Text("Cancel") } }
        )
    }

    Column(Modifier.fillMaxSize()) {
        Column(Modifier.background(MintifiColors.Surface).padding(16.dp), verticalArrangement=Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment=Alignment.CenterVertically) {
                Text("Master Ask Log", style=MaterialTheme.typography.headlineSmall, color=MintifiColors.TextPrimary)
                Spacer(Modifier.weight(1f))
                if (filter.priorities.isNotEmpty() || filter.statuses.isNotEmpty()) {
                    TextButton(onClick={viewModel.clearFilters()}, colors=ButtonDefaults.textButtonColors(contentColor=MintifiColors.DangerRed)) { Text("Clear") }
                }
            }
            OutlinedTextField(value=searchQuery, onValueChange={searchQuery=it;viewModel.setFilter(filter.copy(query=it))}, placeholder={Text("Search tasks, projects...",color=MintifiColors.TextMuted)}, leadingIcon={Icon(Icons.Default.Search,null,tint=MintifiColors.TextMuted,modifier=Modifier.size(18.dp))}, modifier=Modifier.fillMaxWidth().height(50.dp), colors=OutlinedTextFieldDefaults.colors(focusedBorderColor=MintifiColors.Accent,unfocusedBorderColor=MintifiColors.Border,focusedTextColor=MintifiColors.TextPrimary,unfocusedTextColor=MintifiColors.TextPrimary,cursorColor=MintifiColors.Accent,focusedContainerColor=MintifiColors.Surface2,unfocusedContainerColor=MintifiColors.Surface2), shape=RoundedCornerShape(8.dp), singleLine=true)
            Text("Priority:", style=MaterialTheme.typography.labelSmall, color=MintifiColors.TextMuted)
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement=Arrangement.spacedBy(6.dp)) {
                Priority.values().forEach { p ->
                    val sel = p in filter.priorities
                    FilterChip(selected=sel, onClick={viewModel.togglePriority(p)}, label={Text(p.label,style=MaterialTheme.typography.labelSmall,fontWeight=if(sel)FontWeight.Bold else FontWeight.Normal)}, colors=FilterChipDefaults.filterChipColors(selectedContainerColor=when(p){Priority.P0->MintifiColors.DangerRed.copy(alpha=0.2f);Priority.P1->MintifiColors.WarnAmber.copy(alpha=0.2f);Priority.P2->MintifiColors.AccentBlue.copy(alpha=0.2f);else->MintifiColors.SuccessGreen.copy(alpha=0.2f)},selectedLabelColor=when(p){Priority.P0->MintifiColors.DangerRed;Priority.P1->MintifiColors.WarnAmber;Priority.P2->MintifiColors.AccentBlue;else->MintifiColors.SuccessGreen},containerColor=MintifiColors.Surface2,labelColor=MintifiColors.TextSecondary))
                }
            }
            Text("Status:", style=MaterialTheme.typography.labelSmall, color=MintifiColors.TextMuted)
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement=Arrangement.spacedBy(6.dp)) {
                listOf(TaskStatus.PENDING,TaskStatus.IN_PROGRESS,TaskStatus.COMPLETED,TaskStatus.WAITING,TaskStatus.ESCALATED).forEach { s ->
                    val sel = s in filter.statuses
                    FilterChip(selected=sel, onClick={viewModel.toggleStatus(s)}, label={Text(s.label,style=MaterialTheme.typography.labelSmall)}, colors=FilterChipDefaults.filterChipColors(selectedContainerColor=MintifiColors.Accent.copy(alpha=0.15f),selectedLabelColor=MintifiColors.Accent,containerColor=MintifiColors.Surface2,labelColor=MintifiColors.TextSecondary))
                }
            }
            if (filter.priorities.isNotEmpty() || filter.statuses.isNotEmpty()) {
                val f = buildString {
                    if(filter.priorities.isNotEmpty()) append(filter.priorities.joinToString("+"){it.label})
                    if(filter.statuses.isNotEmpty()){if(isNotEmpty())append(" + ");append(filter.statuses.joinToString("+"){it.label})}
                    append(" — ${tasks.size} tasks")
                }
                Text(f, style=MaterialTheme.typography.labelSmall, color=MintifiColors.Accent, fontFamily=FontFamily.Monospace)
            }
        }
        Divider(color=MintifiColors.Border, thickness=0.5.dp)

        if (tasks.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment=Alignment.Center) { Column(horizontalAlignment=Alignment.CenterHorizontally) { Text("No tasks match filters",style=MaterialTheme.typography.titleSmall,color=MintifiColors.TextSecondary); Text("Adjust filters or record a CEO call",style=MaterialTheme.typography.bodySmall,color=MintifiColors.TextMuted) } }
        } else {
            val grouped = tasks.groupBy { it.projectGroup }
            LazyColumn(contentPadding=PaddingValues(16.dp), verticalArrangement=Arrangement.spacedBy(8.dp)) {
                grouped.forEach { (project, projectTasks) ->
                    item {
                        Row(Modifier.fillMaxWidth().padding(top=8.dp,bottom=4.dp), verticalAlignment=Alignment.CenterVertically, horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.FolderOpen,null,tint=MintifiColors.Accent,modifier=Modifier.size(16.dp))
                            Text(project,style=MaterialTheme.typography.titleSmall,color=MintifiColors.Accent)
                            Box(Modifier.background(MintifiColors.Accent.copy(alpha=0.1f),RoundedCornerShape(10.dp)).padding(horizontal=8.dp,vertical=2.dp)){Text(projectTasks.size.toString()+" tasks",style=MaterialTheme.typography.labelSmall,color=MintifiColors.Accent,fontFamily=FontFamily.Monospace)}
                            Spacer(Modifier.weight(1f))
                            projectTasks.firstOrNull()?.let { t -> Text("Session #"+t.sessionId,style=MaterialTheme.typography.labelSmall,color=MintifiColors.TextMuted,fontFamily=FontFamily.Monospace) }
                        }
                    }
                    items(projectTasks, key={it.id}) { task ->
                        EditableTaskCard(task=task, onStatusChange={viewModel.updateTaskStatus(task.id,it)}, onEdit={taskToEdit=task}, onDelete={taskToDelete=task}, fmt=fmt)
                    }
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }

    // Edit task bottom sheet
    taskToEdit?.let { task ->
        EditTaskSheet(task=task, onSave={ updated -> viewModel.updateTask(updated); taskToEdit=null }, onDismiss={ taskToEdit=null })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditableTaskCard(task: Task, onStatusChange: (TaskStatus)->Unit, onEdit: ()->Unit, onDelete: ()->Unit, fmt: SimpleDateFormat) {
    var expanded by remember { mutableStateOf(false) }
    Card(Modifier.fillMaxWidth().clickable{expanded=!expanded}, colors=CardDefaults.cardColors(containerColor=MintifiColors.Surface), shape=RoundedCornerShape(10.dp), border=BorderStroke(0.5.dp,MintifiColors.Border)) {
        Column(Modifier.padding(12.dp), verticalArrangement=Arrangement.spacedBy(6.dp)) {
            Row(horizontalArrangement=Arrangement.spacedBy(8.dp), verticalAlignment=Alignment.Top) {
                PriorityBadge(task.priority)
                Column(Modifier.weight(1f)) {
                    Text(task.title,style=MaterialTheme.typography.titleSmall,color=MintifiColors.TextPrimary)
                    Row(horizontalArrangement=Arrangement.spacedBy(6.dp)){CategoryChip(task.category);StatusChip(task.status)}
                }
                // Edit button
                IconButton(onClick=onEdit, modifier=Modifier.size(28.dp)) { Icon(Icons.Default.Edit,"Edit",tint=MintifiColors.Accent,modifier=Modifier.size(14.dp)) }
                // Delete button
                IconButton(onClick=onDelete, modifier=Modifier.size(28.dp)) { Icon(Icons.Default.Delete,"Delete",tint=MintifiColors.DangerRed.copy(alpha=0.7f),modifier=Modifier.size(14.dp)) }
            }
            Row(horizontalArrangement=Arrangement.spacedBy(8.dp), verticalAlignment=Alignment.CenterVertically) {
                Icon(Icons.Default.AccessTime,null,tint=MintifiColors.TextMuted,modifier=Modifier.size(11.dp))
                Text(fmt.format(Date(task.createdAt)),style=MaterialTheme.typography.labelSmall,color=MintifiColors.TextMuted,fontFamily=FontFamily.Monospace)
                Text("·",color=MintifiColors.TextMuted)
                Text(task.owner,style=MaterialTheme.typography.labelSmall,color=MintifiColors.TextMuted,fontFamily=FontFamily.Monospace)
                Text("·",color=MintifiColors.TextMuted)
                Text(task.deadline,style=MaterialTheme.typography.labelSmall,color=MintifiColors.WarnAmber,fontFamily=FontFamily.Monospace)
            }
            if (expanded) {
                Divider(color=MintifiColors.Border,thickness=0.5.dp)
                if(task.detail.isNotBlank()) Text(task.detail,style=MaterialTheme.typography.bodySmall,color=MintifiColors.TextPrimary)
                Text("Update Status:",style=MaterialTheme.typography.labelSmall,color=MintifiColors.TextSecondary)
                Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(6.dp)){
                    TaskStatus.values().forEach{s->FilterChip(selected=s==task.status,onClick={onStatusChange(s)},label={Text(s.label,style=MaterialTheme.typography.labelSmall)},colors=FilterChipDefaults.filterChipColors(selectedContainerColor=MintifiColors.Accent.copy(alpha=0.12f),selectedLabelColor=MintifiColors.Accent,containerColor=MintifiColors.Surface2,labelColor=MintifiColors.TextSecondary))}
                }
            }
        }
    }
}

// ── Edit Task Sheet ───────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditTaskSheet(task: Task, onSave: (Task)->Unit, onDismiss: ()->Unit) {
    var title    by remember { mutableStateOf(task.title) }
    var detail   by remember { mutableStateOf(task.detail) }
    var priority by remember { mutableStateOf(task.priority) }
    var status   by remember { mutableStateOf(task.status) }
    var deadline by remember { mutableStateOf(task.deadline) }
    var owner    by remember { mutableStateOf(task.owner) }
    var category by remember { mutableStateOf(task.category) }
    var project  by remember { mutableStateOf(task.projectGroup) }

    ModalBottomSheet(onDismissRequest=onDismiss, containerColor=MintifiColors.Surface, dragHandle={Box(Modifier.padding(8.dp).width(40.dp).height(4.dp).background(MintifiColors.Border,RoundedCornerShape(2.dp)))}) {
        Column(Modifier.fillMaxWidth().padding(horizontal=20.dp).padding(bottom=32.dp).verticalScroll(rememberScrollState()), verticalArrangement=Arrangement.spacedBy(14.dp)) {
            Text("Edit Task", style=MaterialTheme.typography.headlineSmall, color=MintifiColors.TextPrimary)

            OutlinedTextField(value=title, onValueChange={title=it}, label={Text("Task Title")}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(10.dp), colors=OutlinedTextFieldDefaults.colors(focusedBorderColor=MintifiColors.Accent,unfocusedBorderColor=MintifiColors.Border,focusedTextColor=MintifiColors.TextPrimary,unfocusedTextColor=MintifiColors.TextPrimary,cursorColor=MintifiColors.Accent,focusedContainerColor=MintifiColors.Surface,unfocusedContainerColor=MintifiColors.Surface))
            OutlinedTextField(value=detail, onValueChange={detail=it}, label={Text("Details")}, modifier=Modifier.fillMaxWidth().height(80.dp), shape=RoundedCornerShape(10.dp), colors=OutlinedTextFieldDefaults.colors(focusedBorderColor=MintifiColors.Accent,unfocusedBorderColor=MintifiColors.Border,focusedTextColor=MintifiColors.TextPrimary,unfocusedTextColor=MintifiColors.TextPrimary,cursorColor=MintifiColors.Accent,focusedContainerColor=MintifiColors.Surface,unfocusedContainerColor=MintifiColors.Surface))

            Row(horizontalArrangement=Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(value=deadline, onValueChange={deadline=it}, label={Text("Deadline")}, modifier=Modifier.weight(1f), shape=RoundedCornerShape(10.dp), singleLine=true, colors=OutlinedTextFieldDefaults.colors(focusedBorderColor=MintifiColors.Accent,unfocusedBorderColor=MintifiColors.Border,focusedTextColor=MintifiColors.TextPrimary,unfocusedTextColor=MintifiColors.TextPrimary,cursorColor=MintifiColors.Accent,focusedContainerColor=MintifiColors.Surface,unfocusedContainerColor=MintifiColors.Surface))
                OutlinedTextField(value=owner, onValueChange={owner=it}, label={Text("Owner")}, modifier=Modifier.weight(1f), shape=RoundedCornerShape(10.dp), singleLine=true, colors=OutlinedTextFieldDefaults.colors(focusedBorderColor=MintifiColors.Accent,unfocusedBorderColor=MintifiColors.Border,focusedTextColor=MintifiColors.TextPrimary,unfocusedTextColor=MintifiColors.TextPrimary,cursorColor=MintifiColors.Accent,focusedContainerColor=MintifiColors.Surface,unfocusedContainerColor=MintifiColors.Surface))
            }

            OutlinedTextField(value=project, onValueChange={project=it}, label={Text("Project Group")}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(10.dp), singleLine=true, colors=OutlinedTextFieldDefaults.colors(focusedBorderColor=MintifiColors.Accent,unfocusedBorderColor=MintifiColors.Border,focusedTextColor=MintifiColors.TextPrimary,unfocusedTextColor=MintifiColors.TextPrimary,cursorColor=MintifiColors.Accent,focusedContainerColor=MintifiColors.Surface,unfocusedContainerColor=MintifiColors.Surface))

            Text("Priority:", style=MaterialTheme.typography.labelMedium, color=MintifiColors.TextSecondary)
            Row(horizontalArrangement=Arrangement.spacedBy(6.dp)) {
                Priority.values().forEach { p -> FilterChip(selected=priority==p, onClick={priority=p}, label={Text(p.label,style=MaterialTheme.typography.labelSmall)}, colors=FilterChipDefaults.filterChipColors(selectedContainerColor=MintifiColors.Accent.copy(alpha=0.15f),selectedLabelColor=MintifiColors.Accent,containerColor=MintifiColors.Surface2,labelColor=MintifiColors.TextSecondary)) }
            }

            Text("Status:", style=MaterialTheme.typography.labelMedium, color=MintifiColors.TextSecondary)
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement=Arrangement.spacedBy(6.dp)) {
                TaskStatus.values().forEach { s -> FilterChip(selected=status==s, onClick={status=s}, label={Text(s.label,style=MaterialTheme.typography.labelSmall)}, colors=FilterChipDefaults.filterChipColors(selectedContainerColor=MintifiColors.Accent.copy(alpha=0.15f),selectedLabelColor=MintifiColors.Accent,containerColor=MintifiColors.Surface2,labelColor=MintifiColors.TextSecondary)) }
            }

            Text("Category:", style=MaterialTheme.typography.labelMedium, color=MintifiColors.TextSecondary)
            LazyRow(horizontalArrangement=Arrangement.spacedBy(6.dp)) {
                items(Category.values()) { c -> FilterChip(selected=category==c, onClick={category=c}, label={Text(c.emoji+" "+c.label,style=MaterialTheme.typography.labelSmall)}, colors=FilterChipDefaults.filterChipColors(selectedContainerColor=MintifiColors.Accent.copy(alpha=0.15f),selectedLabelColor=MintifiColors.Accent,containerColor=MintifiColors.Surface2,labelColor=MintifiColors.TextSecondary)) }
            }

            Button(onClick={ onSave(task.copy(title=title,detail=detail,priority=priority,status=status,deadline=deadline,owner=owner,category=category,projectGroup=project)) }, modifier=Modifier.fillMaxWidth(), enabled=title.isNotBlank(), colors=ButtonDefaults.buttonColors(containerColor=MintifiColors.Accent), shape=RoundedCornerShape(10.dp)) {
                Icon(Icons.Default.Save, null, tint=Color.White); Spacer(Modifier.width(8.dp)); Text("Save Changes", color=Color.White, fontWeight=FontWeight.SemiBold)
            }
        }
    }
}

// ── Copilot ───────────────────────────────────────────────────────────────────

@Composable
fun CopilotScreen(viewModel: MainViewModel) {
    val messages  by viewModel.chatMessages.collectAsState()
    val isLoading by viewModel.isCopilotLoading.collectAsState()
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val scope     = rememberCoroutineScope()
    LaunchedEffect(messages.size) { if (messages.isNotEmpty()) listState.animateScrollToItem(messages.size-1) }
    val quickQ = listOf("Pending P0 tasks?","DDR status?","Collections escalations?","Draft EOD summary","AI projects update","Weekly MOM points")
    Column(Modifier.fillMaxSize()) {
        Column(Modifier.background(MintifiColors.Surface).padding(horizontal=16.dp,vertical=12.dp)) {
            Row(verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(10.dp)) {
                Image(painter=painterResource(id=R.drawable.mintifi_logo),contentDescription="Mintifi",modifier=Modifier.height(22.dp))
                Column{Text("AI Copilot",style=MaterialTheme.typography.titleMedium,color=MintifiColors.TextPrimary);Text("Hindi + English supported",style=MaterialTheme.typography.bodySmall,color=MintifiColors.TextSecondary)}
            }
            Spacer(Modifier.height(8.dp))
            Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(6.dp)){quickQ.forEach{q->QuickQueryChip(text=q){viewModel.sendCopilotMessage(q)}}}
        }
        Divider(color=MintifiColors.Border,thickness=0.5.dp)
        LazyColumn(state=listState,modifier=Modifier.weight(1f),contentPadding=PaddingValues(16.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){
            if(messages.isEmpty())item{Card(colors=CardDefaults.cardColors(containerColor=MintifiColors.Surface),shape=RoundedCornerShape(12.dp),border=BorderStroke(0.5.dp,MintifiColors.Accent.copy(alpha=0.2f))){Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(8.dp)){Text("CEO OS COPILOT",style=MaterialTheme.typography.labelMedium,color=MintifiColors.Accent,fontFamily=FontFamily.Monospace);Text("Ask me about tasks, projects, or meetings. I understand Hindi and English both.",style=MaterialTheme.typography.bodyMedium,color=MintifiColors.TextPrimary)}}}
            items(messages){msg->
                val isUser=msg.role=="user"
                Row(Modifier.fillMaxWidth(),horizontalArrangement=if(isUser)Arrangement.End else Arrangement.Start){
                    Column(Modifier.widthIn(max=300.dp),horizontalAlignment=if(isUser)Alignment.End else Alignment.Start){
                        Card(colors=CardDefaults.cardColors(containerColor=if(isUser)MintifiColors.Surface2 else MintifiColors.Surface),shape=RoundedCornerShape(topStart=if(isUser)12.dp else 4.dp,topEnd=if(isUser)4.dp else 12.dp,bottomStart=12.dp,bottomEnd=12.dp),border=BorderStroke(0.5.dp,if(isUser)MintifiColors.Border else MintifiColors.Accent.copy(alpha=0.2f))){Text(msg.content,style=MaterialTheme.typography.bodyMedium,color=MintifiColors.TextPrimary,modifier=Modifier.padding(12.dp))}
                        Text(SimpleDateFormat("HH:mm",Locale.getDefault()).format(Date(msg.timestamp)),style=MaterialTheme.typography.labelSmall,color=MintifiColors.TextMuted,modifier=Modifier.padding(horizontal=4.dp,vertical=2.dp))
                    }
                }
            }
            if(isLoading)item{
                val inf=rememberInfiniteTransition(label="t");val a by inf.animateFloat(0.3f,1f,infiniteRepeatable(tween(600),RepeatMode.Reverse),label="a")
                Row(horizontalArrangement=Arrangement.spacedBy(4.dp),verticalAlignment=Alignment.CenterVertically){repeat(3){i->Box(Modifier.size(8.dp).background(MintifiColors.Accent.copy(alpha=if(i==0)a else 0.4f),CircleShape))}}
            }
        }
        Divider(color=MintifiColors.Border,thickness=0.5.dp)
        Row(Modifier.background(MintifiColors.Surface).padding(horizontal=12.dp,vertical=10.dp),horizontalArrangement=Arrangement.spacedBy(8.dp),verticalAlignment=Alignment.Bottom){
            OutlinedTextField(value=inputText,onValueChange={inputText=it},placeholder={Text("Ask anything in Hindi or English...",color=MintifiColors.TextMuted)},modifier=Modifier.weight(1f),colors=OutlinedTextFieldDefaults.colors(focusedBorderColor=MintifiColors.Accent,unfocusedBorderColor=MintifiColors.Border,focusedTextColor=MintifiColors.TextPrimary,unfocusedTextColor=MintifiColors.TextPrimary,cursorColor=MintifiColors.Accent,focusedContainerColor=MintifiColors.Surface2,unfocusedContainerColor=MintifiColors.Surface2),shape=RoundedCornerShape(10.dp),maxLines=4)
            FloatingActionButton(onClick={val t=inputText.trim();if(t.isNotBlank()&&!isLoading){viewModel.sendCopilotMessage(t);inputText="";scope.launch{listState.animateScrollToItem(messages.size)}}},containerColor=if(inputText.isNotBlank()&&!isLoading)MintifiColors.Accent else MintifiColors.Surface2,contentColor=if(inputText.isNotBlank()&&!isLoading)Color.White else MintifiColors.TextMuted,modifier=Modifier.size(48.dp),shape=RoundedCornerShape(12.dp),elevation=FloatingActionButtonDefaults.elevation(0.dp)){Icon(Icons.Default.Send,"Send",modifier=Modifier.size(20.dp))}
        }
    }
}

// ── Reminders ─────────────────────────────────────────────────────────────────

@Composable
fun RemindersScreen(viewModel: MainViewModel) {
    val reminders by viewModel.allReminders.collectAsState()
    var showAdd by remember { mutableStateOf(false) }
    var editTarget by remember { mutableStateOf<Reminder?>(null) }
    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            Column(Modifier.background(MintifiColors.Surface).padding(16.dp)){Text("Smart Reminders",style=MaterialTheme.typography.headlineSmall,color=MintifiColors.TextPrimary);Text(reminders.count{it.isActive}.toString()+" active",style=MaterialTheme.typography.bodySmall,color=MintifiColors.TextSecondary)}
            Divider(color=MintifiColors.Border,thickness=0.5.dp)
            LazyColumn(contentPadding=PaddingValues(16.dp),verticalArrangement=Arrangement.spacedBy(8.dp)){
                item{Card(colors=CardDefaults.cardColors(containerColor=MintifiColors.Accent.copy(alpha=0.08f)),border=BorderStroke(0.5.dp,MintifiColors.Accent.copy(alpha=0.3f)),shape=RoundedCornerShape(10.dp)){Row(Modifier.padding(12.dp),horizontalArrangement=Arrangement.spacedBy(10.dp),verticalAlignment=Alignment.CenterVertically){Column(Modifier.weight(1f)){Text("Load Preset Reminders",style=MaterialTheme.typography.titleSmall,color=MintifiColors.Accent);Text("CEO prep, DDR daily, MBA study, EOD wrap",style=MaterialTheme.typography.bodySmall,color=MintifiColors.TextSecondary)};TextButton(onClick={viewModel.loadPresetReminders()},colors=ButtonDefaults.textButtonColors(contentColor=MintifiColors.Accent)){Text("Load")}}}}
                if(reminders.isEmpty())item{Box(Modifier.fillMaxWidth().padding(40.dp),contentAlignment=Alignment.Center){Text("No reminders yet",style=MaterialTheme.typography.titleSmall,color=MintifiColors.TextSecondary)}}
                items(reminders,key={it.id}){rem->
                    Card(colors=CardDefaults.cardColors(containerColor=if(rem.isActive)MintifiColors.Surface else MintifiColors.Surface2),shape=RoundedCornerShape(12.dp),border=BorderStroke(0.5.dp,MintifiColors.Border),modifier=Modifier.fillMaxWidth()){
                        Row(Modifier.padding(14.dp),horizontalArrangement=Arrangement.spacedBy(12.dp),verticalAlignment=Alignment.CenterVertically){
                            Text(rem.category.emoji,fontSize=22.sp)
                            Column(Modifier.weight(1f)){
                                Text(rem.title,style=MaterialTheme.typography.titleSmall,color=if(rem.isActive)MintifiColors.TextPrimary else MintifiColors.TextMuted)
                                val h=if(rem.timeHour%12==0)12 else rem.timeHour%12; val ampm=if(rem.timeHour<12)"AM" else "PM"
                                Text("%02d:%02d %s  %s".format(h,rem.timeMinute,ampm,rem.repeatType.label),style=MaterialTheme.typography.bodySmall,color=MintifiColors.TextSecondary,fontFamily=FontFamily.Monospace)
                            }
                            Column(horizontalAlignment=Alignment.End){
                                Switch(checked=rem.isActive,onCheckedChange={viewModel.toggleReminder(rem.id,!rem.isActive)},colors=SwitchDefaults.colors(checkedThumbColor=Color.White,checkedTrackColor=MintifiColors.Accent,uncheckedTrackColor=MintifiColors.Border),modifier=Modifier.height(24.dp))
                                Row{IconButton(onClick={editTarget=rem;showAdd=true},modifier=Modifier.size(28.dp)){Icon(Icons.Default.Edit,null,tint=MintifiColors.TextMuted,modifier=Modifier.size(14.dp))};IconButton(onClick={viewModel.deleteReminder(rem)},modifier=Modifier.size(28.dp)){Icon(Icons.Default.Delete,null,tint=MintifiColors.DangerRed.copy(alpha=0.7f),modifier=Modifier.size(14.dp))}}
                            }
                        }
                    }
                }
                item{Spacer(Modifier.height(80.dp))}
            }
        }
        FloatingActionButton(onClick={editTarget=null;showAdd=true},modifier=Modifier.align(Alignment.BottomEnd).padding(20.dp),containerColor=MintifiColors.Accent,contentColor=Color.White,shape=RoundedCornerShape(14.dp),elevation=FloatingActionButtonDefaults.elevation(2.dp)){Icon(Icons.Default.Add,"Add")}
    }
    if(showAdd){AddReminderSheet(existing=editTarget,onSave={if(editTarget!=null)viewModel.updateReminder(it) else viewModel.addReminder(it);showAdd=false},onDismiss={showAdd=false})}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddReminderSheet(existing: Reminder?, onSave: (Reminder)->Unit, onDismiss: ()->Unit) {
    var title by remember{mutableStateOf(existing?.title?:"")}; var category by remember{mutableStateOf(existing?.category?:ReminderCategory.GENERAL)}; var repeat by remember{mutableStateOf(existing?.repeatType?:RepeatType.WEEKDAYS)}
    ModalBottomSheet(onDismissRequest=onDismiss,containerColor=MintifiColors.Surface,dragHandle={Box(Modifier.padding(8.dp).width(40.dp).height(4.dp).background(MintifiColors.Border,RoundedCornerShape(2.dp)))}){
        Column(Modifier.fillMaxWidth().padding(horizontal=20.dp).padding(bottom=32.dp),verticalArrangement=Arrangement.spacedBy(14.dp)){
            Text(if(existing!=null)"Edit Reminder" else "New Reminder",style=MaterialTheme.typography.headlineSmall,color=MintifiColors.TextPrimary)
            OutlinedTextField(value=title,onValueChange={title=it},label={Text("Title")},modifier=Modifier.fillMaxWidth(),shape=RoundedCornerShape(10.dp),colors=OutlinedTextFieldDefaults.colors(focusedBorderColor=MintifiColors.Accent,unfocusedBorderColor=MintifiColors.Border,focusedTextColor=MintifiColors.TextPrimary,unfocusedTextColor=MintifiColors.TextPrimary,cursorColor=MintifiColors.Accent,focusedContainerColor=MintifiColors.Surface,unfocusedContainerColor=MintifiColors.Surface))
            Text("Category",style=MaterialTheme.typography.labelMedium,color=MintifiColors.TextSecondary)
            LazyRow(horizontalArrangement=Arrangement.spacedBy(6.dp)){items(ReminderCategory.values()){cat->FilterChip(selected=category==cat,onClick={category=cat},label={Text(cat.emoji+" "+cat.label,style=MaterialTheme.typography.labelSmall)},colors=FilterChipDefaults.filterChipColors(selectedContainerColor=MintifiColors.Accent.copy(alpha=0.15f),selectedLabelColor=MintifiColors.Accent,containerColor=MintifiColors.Surface2,labelColor=MintifiColors.TextSecondary))}}
            Text("Repeat",style=MaterialTheme.typography.labelMedium,color=MintifiColors.TextSecondary)
            Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(6.dp)){RepeatType.values().forEach{rt->FilterChip(selected=repeat==rt,onClick={repeat=rt},label={Text(rt.label,style=MaterialTheme.typography.labelSmall)},colors=FilterChipDefaults.filterChipColors(selectedContainerColor=MintifiColors.Accent.copy(alpha=0.15f),selectedLabelColor=MintifiColors.Accent,containerColor=MintifiColors.Surface2,labelColor=MintifiColors.TextSecondary))}}
            Button(onClick={if(title.isNotBlank())onSave(Reminder(id=existing?.id?:0,title=title,category=category,repeatType=repeat,timeHour=existing?.timeHour?:9,timeMinute=existing?.timeMinute?:0,notificationId=(System.currentTimeMillis()%Int.MAX_VALUE).toInt()))},modifier=Modifier.fillMaxWidth(),enabled=title.isNotBlank(),colors=ButtonDefaults.buttonColors(containerColor=MintifiColors.Accent),shape=RoundedCornerShape(10.dp)){Text(if(existing!=null)"Update" else "Save",color=Color.White)}
        }
    }
}

// ── Todo with delete ──────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(viewModel: MainViewModel) {
    val allTodos      by viewModel.allTodos.collectAsState()
    var selectedList  by remember{mutableStateOf(TodoListType.TODAY)}
    var showAdd       by remember{mutableStateOf(false)}
    var showTemplates by remember{mutableStateOf(false)}
    var newTitle      by remember{mutableStateOf("")}
    var newPriority   by remember{mutableStateOf(TodoPriority.NORMAL)}
    var todoToDelete  by remember{mutableStateOf<TodoItem?>(null)}
    val filtered=allTodos.filter{it.listType==selectedList}; val pending=filtered.filter{!it.isCompleted}; val completed=filtered.filter{it.isCompleted}

    // Delete confirmation
    todoToDelete?.let { todo ->
        AlertDialog(
            onDismissRequest = { todoToDelete = null },
            title = { Text("Delete Task?") },
            text = { Text("\"" + todo.title + "\" will be deleted.") },
            confirmButton = { TextButton(onClick = { viewModel.deleteTodo(todo); todoToDelete = null }, colors = ButtonDefaults.textButtonColors(contentColor = MintifiColors.DangerRed)) { Text("Delete") } },
            dismissButton = { TextButton(onClick = { todoToDelete = null }) { Text("Cancel") } }
        )
    }

    Box(Modifier.fillMaxSize()){
        Column(Modifier.fillMaxSize()){
            Column(Modifier.background(MintifiColors.Surface).padding(16.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){
                Row(verticalAlignment=Alignment.CenterVertically){Column(Modifier.weight(1f)){Text("To-Do Lists",style=MaterialTheme.typography.headlineSmall,color=MintifiColors.TextPrimary);Text(pending.size.toString()+" pending",style=MaterialTheme.typography.bodySmall,color=MintifiColors.TextSecondary)};IconButton(onClick={showTemplates=true}){Icon(Icons.Default.GridView,"Templates",tint=MintifiColors.Accent)}}
                LazyRow(horizontalArrangement=Arrangement.spacedBy(6.dp)){items(TodoListType.values()){lt->val cnt=allTodos.count{it.listType==lt&&!it.isCompleted};FilterChip(selected=selectedList==lt,onClick={selectedList=lt},label={Text(lt.emoji+" "+lt.label+(if(cnt>0)" ($cnt)" else ""),style=MaterialTheme.typography.labelSmall)},colors=FilterChipDefaults.filterChipColors(selectedContainerColor=MintifiColors.Accent.copy(alpha=0.15f),selectedLabelColor=MintifiColors.Accent,containerColor=MintifiColors.Surface2,labelColor=MintifiColors.TextSecondary))}}
            }
            Divider(color=MintifiColors.Border,thickness=0.5.dp)
            if(showAdd){Card(Modifier.fillMaxWidth().padding(16.dp),colors=CardDefaults.cardColors(containerColor=MintifiColors.Surface),border=BorderStroke(0.5.dp,MintifiColors.Accent.copy(alpha=0.4f)),shape=RoundedCornerShape(12.dp)){Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){OutlinedTextField(value=newTitle,onValueChange={newTitle=it},placeholder={Text("Task title...",color=MintifiColors.TextMuted)},modifier=Modifier.fillMaxWidth(),shape=RoundedCornerShape(8.dp),colors=OutlinedTextFieldDefaults.colors(focusedBorderColor=MintifiColors.Accent,unfocusedBorderColor=MintifiColors.Border,focusedTextColor=MintifiColors.TextPrimary,unfocusedTextColor=MintifiColors.TextPrimary,cursorColor=MintifiColors.Accent,focusedContainerColor=MintifiColors.Surface,unfocusedContainerColor=MintifiColors.Surface),singleLine=true);Row(horizontalArrangement=Arrangement.spacedBy(6.dp)){TodoPriority.values().forEach{p->FilterChip(selected=newPriority==p,onClick={newPriority=p},label={Text(p.label,style=MaterialTheme.typography.labelSmall)},colors=FilterChipDefaults.filterChipColors(selectedContainerColor=MintifiColors.Accent.copy(alpha=0.15f),selectedLabelColor=MintifiColors.Accent,containerColor=MintifiColors.Surface2,labelColor=MintifiColors.TextSecondary))}};Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){Button(onClick={if(newTitle.isNotBlank()){viewModel.addTodo(TodoItem(title=newTitle,listType=selectedList,priority=newPriority));newTitle="";showAdd=false}},colors=ButtonDefaults.buttonColors(containerColor=MintifiColors.Accent),shape=RoundedCornerShape(8.dp),modifier=Modifier.weight(1f)){Text("Add Task",color=Color.White)};OutlinedButton(onClick={showAdd=false;newTitle=""},border=BorderStroke(0.5.dp,MintifiColors.Border),shape=RoundedCornerShape(8.dp)){Text("Cancel")}}}}}
            LazyColumn(contentPadding=PaddingValues(16.dp),verticalArrangement=Arrangement.spacedBy(6.dp)){
                if(pending.isEmpty()&&completed.isEmpty())item{Box(Modifier.fillMaxWidth().padding(40.dp),contentAlignment=Alignment.Center){Column(horizontalAlignment=Alignment.CenterHorizontally){Text(selectedList.emoji,fontSize=36.sp);Spacer(Modifier.height(8.dp));Text(selectedList.label+" is empty",style=MaterialTheme.typography.titleSmall,color=MintifiColors.TextSecondary)}}}
                items(pending,key={it.id}){todo->
                    TodoCard(todo=todo, onCheck={viewModel.toggleTodo(todo.id,true)}, onDelete={todoToDelete=todo})
                }
                if(completed.isNotEmpty()){
                    item{Row(Modifier.fillMaxWidth().padding(top=8.dp),horizontalArrangement=Arrangement.SpaceBetween,verticalAlignment=Alignment.CenterVertically){Text("Completed ("+completed.size+")",style=MaterialTheme.typography.labelMedium,color=MintifiColors.TextMuted);TextButton(onClick={viewModel.clearCompleted(selectedList)},colors=ButtonDefaults.textButtonColors(contentColor=MintifiColors.DangerRed)){Text("Clear All")}}}
                    items(completed,key={it.id}){todo->TodoCard(todo=todo,onCheck={viewModel.toggleTodo(todo.id,false)},onDelete={todoToDelete=todo},done=true)}
                }
                item{Spacer(Modifier.height(80.dp))}
            }
        }
        FloatingActionButton(onClick={showAdd=!showAdd},modifier=Modifier.align(Alignment.BottomEnd).padding(20.dp),containerColor=MintifiColors.Accent,contentColor=Color.White,shape=RoundedCornerShape(14.dp),elevation=FloatingActionButtonDefaults.elevation(2.dp)){Icon(if(showAdd)Icons.Default.Close else Icons.Default.Add,"Add")}
    }
    if(showTemplates){TemplatesSheet(onSelect={viewModel.loadTemplate(it,selectedList);showTemplates=false},onDismiss={showTemplates=false})}
}

@Composable
private fun TodoCard(todo: TodoItem, onCheck: ()->Unit, onDelete: ()->Unit, done: Boolean=false) {
    Card(colors=CardDefaults.cardColors(containerColor=if(done)MintifiColors.Surface2 else MintifiColors.Surface),shape=RoundedCornerShape(10.dp),border=BorderStroke(0.5.dp,MintifiColors.Border),modifier=Modifier.fillMaxWidth()){
        Row(Modifier.padding(12.dp),horizontalArrangement=Arrangement.spacedBy(10.dp),verticalAlignment=Alignment.CenterVertically){
            Checkbox(checked=done,onCheckedChange={onCheck()},colors=CheckboxDefaults.colors(checkedColor=MintifiColors.SuccessGreen,uncheckedColor=MintifiColors.Border,checkmarkColor=Color.White))
            Text(todo.title,style=MaterialTheme.typography.bodyMedium,color=if(done)MintifiColors.TextMuted else MintifiColors.TextPrimary,textDecoration=if(done)TextDecoration.LineThrough else null,modifier=Modifier.weight(1f))
            // Delete button always visible
            IconButton(onClick=onDelete, modifier=Modifier.size(30.dp)) {
                Icon(Icons.Default.Delete,"Delete",tint=MintifiColors.DangerRed.copy(alpha=if(done)0.4f else 0.7f),modifier=Modifier.size(16.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TemplatesSheet(onSelect: (WorkTemplate)->Unit, onDismiss: ()->Unit) {
    ModalBottomSheet(onDismissRequest=onDismiss,containerColor=MintifiColors.Surface,dragHandle={Box(Modifier.padding(8.dp).width(40.dp).height(4.dp).background(MintifiColors.Border,RoundedCornerShape(2.dp)))}){
        Column(Modifier.fillMaxWidth().padding(horizontal=20.dp).padding(bottom=32.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){
            Text("Work Templates",style=MaterialTheme.typography.headlineSmall,color=MintifiColors.TextPrimary)
            PRADEEP_WORK_TEMPLATES.forEach{t->Card(colors=CardDefaults.cardColors(containerColor=MintifiColors.Surface2),border=BorderStroke(0.5.dp,MintifiColors.Border),shape=RoundedCornerShape(10.dp),modifier=Modifier.fillMaxWidth().clickable{onSelect(t)}){Row(Modifier.padding(14.dp),horizontalArrangement=Arrangement.spacedBy(12.dp),verticalAlignment=Alignment.CenterVertically){Text(t.emoji,fontSize=22.sp);Column(Modifier.weight(1f)){Text(t.title,style=MaterialTheme.typography.titleSmall,color=MintifiColors.TextPrimary);Text(t.items.size.toString()+" items",style=MaterialTheme.typography.bodySmall,color=MintifiColors.TextSecondary)};Icon(Icons.Default.ChevronRight,null,tint=MintifiColors.TextMuted,modifier=Modifier.size(18.dp))}}}
        }
    }
}

// ── Settings ──────────────────────────────────────────────────────────────────

@Composable
fun SettingsScreen() {
    val context=LocalContext.current; val scope=rememberCoroutineScope()
    val prefs=remember{context.getSharedPreferences("ceo_os_prefs",Context.MODE_PRIVATE)}
    var geminiKey    by remember{mutableStateOf(prefs.getString("gemini_key","") ?: "")}
    var groqKey      by remember{mutableStateOf(prefs.getString("groq_key","") ?: "")}
    var anthropicKey by remember{mutableStateOf(prefs.getString("anthropic_key","") ?: "")}
    var deepgramKey  by remember{mutableStateOf(prefs.getString("deepgram_key","") ?: "")}
    var showKeys     by remember{mutableStateOf(false)}
    var saved        by remember{mutableStateOf(false)}

    Column(Modifier.fillMaxSize().background(MintifiColors.Background).verticalScroll(rememberScrollState()).padding(20.dp),verticalArrangement=Arrangement.spacedBy(20.dp)){
        Row(verticalAlignment=Alignment.CenterVertically){Image(painter=painterResource(id=R.drawable.mintifi_logo),contentDescription="Mintifi",modifier=Modifier.height(32.dp))}
        Text("CEO OS Settings",style=MaterialTheme.typography.headlineSmall,color=MintifiColors.TextPrimary)
        SSection("AI Keys — Free keys available"){
            Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween,verticalAlignment=Alignment.CenterVertically){Text("Show keys",color=MintifiColors.TextSecondary);Switch(checked=showKeys,onCheckedChange={showKeys=it},colors=SwitchDefaults.colors(checkedThumbColor=Color.White,checkedTrackColor=MintifiColors.Accent))}
            SKeyField("Gemini Key — FREE (ai.google.dev)",geminiKey,"AIzaSy...",showKeys){geminiKey=it}
            SKeyField("Groq Key — FREE (console.groq.com)",groqKey,"gsk_...",showKeys){groqKey=it}
            SKeyField("Anthropic Key — PAID optional",anthropicKey,"sk-ant-...",showKeys){anthropicKey=it}
            SKeyField("Deepgram Key — PAID (Indian English)",deepgramKey,"deepgram...",showKeys){deepgramKey=it}
        }
        SSection("Features Active"){
            SInfoRow("Hindi + English","Auto-detected in all recordings")
            SInfoRow("Offline Mode","Queues recordings when no internet")
            SInfoRow("Task Edit","Edit any task after AI creates it")
            SInfoRow("Task Delete","Delete from Home, Tasks, To-Do")
            SInfoRow("Project Groups","Tasks grouped by discussion topic")
            SInfoRow("Meeting Summary","Auto-generated after recording")
        }
        Button(onClick={scope.launch{prefs.edit().putString("gemini_key",geminiKey).putString("groq_key",groqKey).putString("anthropic_key",anthropicKey).putString("deepgram_key",deepgramKey).apply();saved=true}},modifier=Modifier.fillMaxWidth(),colors=ButtonDefaults.buttonColors(containerColor=MintifiColors.Accent),shape=RoundedCornerShape(10.dp)){Icon(if(saved)Icons.Default.CheckCircle else Icons.Default.Save,null,tint=Color.White);Spacer(Modifier.width(8.dp));Text(if(saved)"Saved!" else "Save Settings",color=Color.White)}
        Spacer(Modifier.height(80.dp))
    }
}

@Composable private fun SSection(title: String, content: @Composable ColumnScope.()->Unit){Column(verticalArrangement=Arrangement.spacedBy(8.dp)){Text(title,style=MaterialTheme.typography.labelSmall,color=MintifiColors.TextMuted,fontFamily=FontFamily.Monospace);Card(colors=CardDefaults.cardColors(containerColor=MintifiColors.Surface),shape=RoundedCornerShape(12.dp),border=BorderStroke(0.5.dp,MintifiColors.Border)){Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){content()}}}}
@Composable private fun SKeyField(label: String, value: String, hint: String, visible: Boolean, onChange: (String)->Unit){Column(verticalArrangement=Arrangement.spacedBy(3.dp)){Text(label,style=MaterialTheme.typography.bodySmall,color=MintifiColors.TextSecondary);OutlinedTextField(value=value,onValueChange=onChange,placeholder={Text(hint,color=MintifiColors.TextMuted)},visualTransformation=if(visible)VisualTransformation.None else PasswordVisualTransformation(),modifier=Modifier.fillMaxWidth(),shape=RoundedCornerShape(8.dp),singleLine=true,colors=OutlinedTextFieldDefaults.colors(focusedBorderColor=MintifiColors.Accent,unfocusedBorderColor=MintifiColors.Border,focusedTextColor=MintifiColors.TextPrimary,unfocusedTextColor=MintifiColors.TextPrimary,cursorColor=MintifiColors.Accent,focusedContainerColor=MintifiColors.Surface,unfocusedContainerColor=MintifiColors.Surface))}}
@Composable private fun SInfoRow(label: String, value: String){Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){Text(label,style=MaterialTheme.typography.bodySmall,color=MintifiColors.TextSecondary);Text(value,style=MaterialTheme.typography.bodySmall,color=MintifiColors.TextPrimary)}}
