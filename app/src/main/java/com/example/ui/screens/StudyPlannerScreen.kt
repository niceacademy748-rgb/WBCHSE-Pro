package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudyRecommendation
import com.example.data.model.SubjectGoalEntity
import com.example.ui.AppNavTab
import com.example.ui.theme.*

@Composable
fun StudyPlannerScreen(
    subjectGoals: List<SubjectGoalEntity>,
    recommendations: List<StudyRecommendation>,
    onUpdateGoal: (subjectId: String, weeklyTargetHours: Int, targetScorePercent: Int, weakTopicsCsv: String, customNotes: String) -> Unit,
    onLogStudyHours: (subjectId: String, hours: Float) -> Unit,
    onNavigateTab: (AppNavTab) -> Unit,
    onSelectSubject: (subjectId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedGoalForEdit by remember { mutableStateOf<SubjectGoalEntity?>(null) }
    var selectedGoalForLogging by remember { mutableStateOf<SubjectGoalEntity?>(null) }

    val totalTargetHours = subjectGoals.sumOf { it.weeklyTargetHours }
    val totalCompletedHours = subjectGoals.sumOf { it.completedHoursThisWeek.toDouble() }.toFloat()
    val overallPlannerProgress = if (totalTargetHours > 0) (totalCompletedHours / totalTargetHours).coerceIn(0f, 1f) else 0.6f

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Header ---
        item {
            Column {
                Text(
                    text = "🎯 Personalized Study Planner",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Track subject targets, upcoming exams & AI-suggested topics",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // --- Exam Countdown Banners Bento Grid ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // WBCHSE HS Board Countdown
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(22.dp),
                    color = BentoSurfaceVariant,
                    border = BorderStroke(1.dp, BentoBorder.copy(alpha = 0.35f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "WBCHSE HS '27",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = BentoPrimary
                            )
                            Icon(Icons.Filled.Event, contentDescription = null, tint = BentoPrimary, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "184 Days",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Class 12 Board Exam",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // NEET UG Entrance Countdown
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(22.dp),
                    color = BentoNeetRedContainer,
                    border = BorderStroke(1.dp, BentoNeetRedBorder.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "NEET UG '27",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = BentoNeetRedText
                            )
                            Icon(Icons.Filled.HealthAndSafety, contentDescription = null, tint = BentoNeetRedText, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "272 Days",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = BentoNeetRedText
                        )
                        Text(
                            text = "Medical Entrance Prep",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFFFDAD6).copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        // --- Weekly Study Goal Overall Progress Card ---
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = BentoCardSecondary,
                border = BorderStroke(1.dp, BentoBorder.copy(alpha = 0.35f))
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Weekly Target Progress",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "6 Core Stream Subjects",
                                style = MaterialTheme.typography.labelSmall,
                                color = BentoPrimary
                            )
                        }

                        Surface(
                            color = BentoPrimaryContainer,
                            shape = CircleShape
                        ) {
                            Text(
                                text = "${String.format("%.1f", totalCompletedHours)} / ${totalTargetHours} hrs",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = BentoOnPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    LinearProgressIndicator(
                        progress = { overallPlannerProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = BentoPrimary,
                        trackColor = BentoBorder.copy(alpha = 0.3f)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${(overallPlannerProgress * 100).toInt()}% Weekly Goal Met",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Text(
                            text = "Target Score Avg: 87%",
                            style = MaterialTheme.typography.labelSmall,
                            color = BentoPrimary
                        )
                    }
                }
            }
        }

        // --- Smart Recommendation Engine ---
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "💡 Smart Study Recommendations",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Based on Weak Areas & Tests",
                        style = MaterialTheme.typography.labelSmall,
                        color = BentoPrimary
                    )
                }

                if (recommendations.isEmpty()) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = BentoSurface
                    ) {
                        Box(modifier = Modifier.padding(16.dp)) {
                            Text("No recommendations currently pending. Great job staying on top of your study goals!", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(recommendations) { rec ->
                            Surface(
                                modifier = Modifier
                                    .width(260.dp)
                                    .clickable {
                                        if (rec.actionType == "NEET") {
                                            onNavigateTab(AppNavTab.NEET_CORNER)
                                        } else {
                                            onSelectSubject(rec.subjectId)
                                        }
                                    },
                                shape = RoundedCornerShape(20.dp),
                                color = BentoSurface,
                                border = BorderStroke(1.dp, if (rec.priority == "HIGH") BentoPrimary else BentoBorder.copy(alpha = 0.3f))
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            color = if (rec.priority == "HIGH") BentoNeetBadge else BentoPrimaryContainer,
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = rec.reason,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }

                                        Text(
                                            text = rec.subjectName,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = BentoPrimary
                                        )
                                    }

                                    Text(
                                        text = rec.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Text(
                                        text = rec.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Text(
                                            text = "Start Now →",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = BentoPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- Subject Goals Section (6 Subjects) ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📚 Subject Target Goals",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "6 Core Subjects",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        items(subjectGoals) { goal ->
            val progressFrac = if (goal.weeklyTargetHours > 0) (goal.completedHoursThisWeek / goal.weeklyTargetHours).coerceIn(0f, 1f) else 0f
            val subjectName = when (goal.subjectId) {
                "PHYS" -> "Physics"
                "CHEM" -> "Chemistry"
                "BIO" -> "Biology"
                "MATH" -> "Mathematics"
                "BEN" -> "বাংলা (Bengali)"
                "ENG" -> "English"
                else -> goal.subjectId
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = BentoSurface,
                border = BorderStroke(1.dp, BentoBorder.copy(alpha = 0.35f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = BentoPrimaryContainer,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = goal.subjectId.take(3),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = BentoOnPrimaryContainer
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = subjectName,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Target Score: ${goal.targetScorePercent}%",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = BentoPrimary
                                )
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            // Log time button
                            IconButton(
                                onClick = { selectedGoalForLogging = goal },
                                modifier = Modifier.size(32.dp).background(BentoSurfaceVariant, CircleShape)
                            ) {
                                Icon(Icons.Filled.Add, contentDescription = "Log Hours", tint = BentoPrimary, modifier = Modifier.size(18.dp))
                            }

                            // Edit goal button
                            IconButton(
                                onClick = { selectedGoalForEdit = goal },
                                modifier = Modifier.size(32.dp).background(BentoSurfaceVariant, CircleShape)
                            ) {
                                Icon(Icons.Filled.Edit, contentDescription = "Edit Goal", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                            }
                        }
                    }

                    // Progress Bar
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Weekly Time: ${String.format("%.1f", goal.completedHoursThisWeek)} / ${goal.weeklyTargetHours} hrs",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${(progressFrac * 100).toInt()}%",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        LinearProgressIndicator(
                            progress = { progressFrac },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = BentoPrimary,
                            trackColor = BentoBorder.copy(alpha = 0.3f)
                        )
                    }

                    // Weak Topics Tags
                    if (goal.weakTopicsCsv.isNotBlank()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("Focus Areas:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                text = goal.weakTopicsCsv,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }

    // --- Edit Goal Dialog ---
    if (selectedGoalForEdit != null) {
        val goal = selectedGoalForEdit!!
        var targetHoursText by remember { mutableStateOf(goal.weeklyTargetHours.toString()) }
        var targetScoreText by remember { mutableStateOf(goal.targetScorePercent.toString()) }
        var weakTopicsText by remember { mutableStateOf(goal.weakTopicsCsv) }
        var customNotesText by remember { mutableStateOf(goal.customNotes) }

        AlertDialog(
            onDismissRequest = { selectedGoalForEdit = null },
            title = { Text("Set Goal for ${goal.subjectId}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = targetHoursText,
                        onValueChange = { targetHoursText = it },
                        label = { Text("Weekly Target Hours") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = targetScoreText,
                        onValueChange = { targetScoreText = it },
                        label = { Text("Target Score %") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = weakTopicsText,
                        onValueChange = { weakTopicsText = it },
                        label = { Text("Weak Topics / Focus Areas") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = customNotesText,
                        onValueChange = { customNotesText = it },
                        label = { Text("Strategy / Notes") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val hours = targetHoursText.toIntOrNull() ?: goal.weeklyTargetHours
                        val score = targetScoreText.toIntOrNull() ?: goal.targetScorePercent
                        onUpdateGoal(goal.subjectId, hours, score, weakTopicsText, customNotesText)
                        selectedGoalForEdit = null
                    }
                ) {
                    Text("Save Goal")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedGoalForEdit = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // --- Log Hours Quick Dialog ---
    if (selectedGoalForLogging != null) {
        val goal = selectedGoalForLogging!!
        AlertDialog(
            onDismissRequest = { selectedGoalForLogging = null },
            title = { Text("Log Study Session (${goal.subjectId})") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Log recent revision hours to update your weekly planner goal.")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                onLogStudyHours(goal.subjectId, 0.5f)
                                selectedGoalForLogging = null
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("+30 Min")
                        }
                        Button(
                            onClick = {
                                onLogStudyHours(goal.subjectId, 1.0f)
                                selectedGoalForLogging = null
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("+1 Hour")
                        }
                        Button(
                            onClick = {
                                onLogStudyHours(goal.subjectId, 2.0f)
                                selectedGoalForLogging = null
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("+2 Hours")
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { selectedGoalForLogging = null }) {
                    Text("Close")
                }
            }
        )
    }
}
