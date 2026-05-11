package com.mintifi.ceoos.ui.components
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import com.mintifi.ceoos.data.model.*
import com.mintifi.ceoos.ui.theme.MintifiColors
import kotlin.math.*

@Composable
fun PriorityBadge(priority: Priority, modifier: Modifier = Modifier) {
    val (bg, fg) = when (priority) {
        Priority.P0 -> MintifiColors.P0Background to MintifiColors.P0Text
        Priority.P1 -> MintifiColors.P1Background to MintifiColors.P1Text
        Priority.P2 -> MintifiColors.P2Background to MintifiColors.P2Text
        Priority.P3 -> MintifiColors.P3Background to MintifiColors.P3Text
    }
    Box(modifier.background(bg, RoundedCornerShape(4.dp)).border(0.5.dp, fg.copy(alpha=0.5f), RoundedCornerShape(4.dp)).padding(horizontal=8.dp, vertical=3.dp)) {
        Text(priority.label, style=MaterialTheme.typography.labelSmall, color=fg, fontFamily=FontFamily.Monospace, fontWeight=FontWeight.Medium)
    }
}

@Composable
fun CategoryChip(category: Category, modifier: Modifier = Modifier) {
    Box(modifier.background(MintifiColors.Surface2, RoundedCornerShape(4.dp)).border(0.5.dp, MintifiColors.Border, RoundedCornerShape(4.dp)).padding(horizontal=7.dp, vertical=3.dp)) {
        Text(category.emoji + " " + category.label, style=MaterialTheme.typography.labelSmall, color=MintifiColors.TextSecondary)
    }
}

@Composable
fun StatusChip(status: TaskStatus, modifier: Modifier = Modifier) {
    val (bg, fg) = when (status) {
        TaskStatus.PENDING      -> MintifiColors.WarnAmber.copy(alpha=0.12f)    to MintifiColors.WarnAmber
        TaskStatus.IN_PROGRESS  -> MintifiColors.InfoBlue.copy(alpha=0.12f)     to MintifiColors.InfoBlue
        TaskStatus.COMPLETED    -> MintifiColors.SuccessGreen.copy(alpha=0.12f) to MintifiColors.SuccessGreen
        TaskStatus.WAITING      -> MintifiColors.WarnAmber.copy(alpha=0.08f)    to MintifiColors.WarnAmber
        TaskStatus.UNDER_REVIEW -> MintifiColors.Accent.copy(alpha=0.08f)       to MintifiColors.Accent
        TaskStatus.DROPPED      -> MintifiColors.Surface2                       to MintifiColors.TextMuted
        TaskStatus.ESCALATED    -> MintifiColors.DangerRed.copy(alpha=0.12f)    to MintifiColors.DangerRed
    }
    Box(modifier.background(bg, RoundedCornerShape(4.dp)).padding(horizontal=7.dp, vertical=3.dp)) {
        Text(status.label, style=MaterialTheme.typography.labelSmall, color=fg, fontFamily=FontFamily.Monospace)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCard(task: Task, onStatusChange: (TaskStatus) -> Unit, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    Card(modifier.fillMaxWidth().clickable { expanded = !expanded }, colors=CardDefaults.cardColors(containerColor=MintifiColors.Surface), shape=RoundedCornerShape(10.dp), border=BorderStroke(0.5.dp, MintifiColors.Border)) {
        Column(Modifier.padding(14.dp)) {
            Row(horizontalArrangement=Arrangement.spacedBy(10.dp), verticalAlignment=Alignment.Top) {
                PriorityBadge(task.priority)
                Column(Modifier.weight(1f)) {
                    Text(task.title, style=MaterialTheme.typography.titleMedium, color=MintifiColors.TextPrimary)
                    Spacer(Modifier.height(5.dp))
                    Row(horizontalArrangement=Arrangement.spacedBy(6.dp)) { CategoryChip(task.category); StatusChip(task.status) }
                    Spacer(Modifier.height(3.dp))
                    Row(horizontalArrangement=Arrangement.spacedBy(10.dp)) {
                        Text("by " + task.owner, style=MaterialTheme.typography.bodySmall, color=MintifiColors.TextMuted, fontFamily=FontFamily.Monospace)
                        Text(task.deadline, style=MaterialTheme.typography.bodySmall, color=MintifiColors.TextMuted, fontFamily=FontFamily.Monospace)
                    }
                }
            }
            if (expanded) {
                Spacer(Modifier.height(10.dp))
                Divider(color=MintifiColors.Border, thickness=0.5.dp)
                Spacer(Modifier.height(10.dp))
                if (task.detail.isNotBlank()) { Text(task.detail, style=MaterialTheme.typography.bodyMedium, color=MintifiColors.TextPrimary); Spacer(Modifier.height(8.dp)) }
                Text("Update Status", style=MaterialTheme.typography.labelMedium, color=MintifiColors.TextSecondary)
                Spacer(Modifier.height(6.dp))
                Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement=Arrangement.spacedBy(6.dp)) {
                    TaskStatus.values().forEach { s ->
                        FilterChip(selected=s==task.status, onClick={ onStatusChange(s) }, label={ Text(s.label, style=MaterialTheme.typography.labelSmall) },
                            colors=FilterChipDefaults.filterChipColors(selectedContainerColor=MintifiColors.Accent.copy(alpha=0.12f), selectedLabelColor=MintifiColors.Accent, containerColor=MintifiColors.Surface2, labelColor=MintifiColors.TextSecondary))
                    }
                }
            }
        }
    }
}

@Composable
fun WaveformVisualizer(amplitude: Float, isRecording: Boolean) {
    val transition = rememberInfiniteTransition(label="wave")
    val phase by transition.animateFloat(0f, (2*PI).toFloat(), infiniteRepeatable(tween(1200, easing=LinearEasing)), label="phase")
    Canvas(Modifier.fillMaxWidth().height(64.dp)) {
        val bars=40; val bw=size.width/bars/1.5f; val gap=size.width/bars
        val mxH=size.height*0.85f; val mnH=size.height*0.08f
        for (i in 0 until bars) {
            val wave=sin(phase+i*0.4f).toFloat()
            val noise=amplitude*(0.5f+0.5f*sin(phase*2+i.toFloat()))
            val h=(mnH+(mxH-mnH)*(0.15f+0.85f*abs(wave)*noise)).coerceAtLeast(mnH)
            val x=i*gap+gap/2f
            drawRoundRect(color=MintifiColors.Accent.copy(alpha=if(isRecording)0.6f+0.4f*abs(wave) else 0.15f),
                topLeft=androidx.compose.ui.geometry.Offset(x-bw/2,size.height/2-h/2),
                size=androidx.compose.ui.geometry.Size(bw,h),
                cornerRadius=androidx.compose.ui.geometry.CornerRadius(bw/2))
        }
    }
}

@Composable
fun SectionHeader(title: String, count: Int? = null) {
    Row(verticalAlignment=Alignment.CenterVertically, horizontalArrangement=Arrangement.spacedBy(8.dp), modifier=Modifier.padding(vertical=6.dp)) {
        Text(title.uppercase(), style=MaterialTheme.typography.labelSmall, color=MintifiColors.TextMuted, fontFamily=FontFamily.Monospace, letterSpacing=1.sp)
        count?.let { Box(Modifier.background(MintifiColors.Accent.copy(alpha=0.12f), RoundedCornerShape(10.dp)).padding(horizontal=7.dp, vertical=2.dp)) { Text(it.toString(), style=MaterialTheme.typography.labelSmall, color=MintifiColors.Accent, fontFamily=FontFamily.Monospace) } }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickQueryChip(text: String, onClick: () -> Unit) {
    FilterChip(selected=false, onClick=onClick, label={ Text(text, style=MaterialTheme.typography.labelSmall) },
        colors=FilterChipDefaults.filterChipColors(containerColor=MintifiColors.Surface2, labelColor=MintifiColors.TextSecondary))
}
