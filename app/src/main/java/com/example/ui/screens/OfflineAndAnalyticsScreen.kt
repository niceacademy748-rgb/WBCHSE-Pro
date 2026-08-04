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
import com.example.data.model.DownloadedMaterialEntity
import com.example.data.model.QuizAttemptEntity
import com.example.data.model.UserProgressEntity
import com.example.ui.theme.*

@Composable
fun OfflineAndAnalyticsScreen(
    downloadedMaterials: List<DownloadedMaterialEntity>,
    onRemoveDownload: (String) -> Unit,
    onPauseDownload: (String) -> Unit = {},
    onResumeDownload: (String) -> Unit = {},
    onRetryDownload: (String) -> Unit = {},
    onClearAllDownloads: () -> Unit = {},
    quizAttempts: List<QuizAttemptEntity>,
    userProgressList: List<UserProgressEntity>,
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    isOfflineOnlyMode: Boolean,
    onToggleOfflineOnlyMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("ALL") } // "ALL", "LECTURE", "NOTES", "PYQ_NEET", "ACTIVE"
    var viewingOfflineItem by remember { mutableStateOf<DownloadedMaterialEntity?>(null) }
    var showClearCacheConfirmation by remember { mutableStateOf(false) }

    val totalSizeBytes = downloadedMaterials.sumOf { it.fileSizeBytes }
    val totalSizeMb = totalSizeBytes / (1024 * 1024)

    val lectureSizeBytes = downloadedMaterials.filter { it.type == "LECTURE" }.sumOf { it.fileSizeBytes }
    val notesSizeBytes = downloadedMaterials.filter { it.type == "NOTES" }.sumOf { it.fileSizeBytes }
    val pyqSizeBytes = downloadedMaterials.filter { it.type == "PYQ" || it.type == "NEET" }.sumOf { it.fileSizeBytes }

    val filteredMaterials = remember(downloadedMaterials, selectedFilter) {
        when (selectedFilter) {
            "LECTURE" -> downloadedMaterials.filter { it.type == "LECTURE" }
            "NOTES" -> downloadedMaterials.filter { it.type == "NOTES" }
            "PYQ_NEET" -> downloadedMaterials.filter { it.type == "PYQ" || it.type == "NEET" }
            "ACTIVE" -> downloadedMaterials.filter { it.downloadStatus != "COMPLETED" }
            else -> downloadedMaterials
        }
    }

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
                    text = "📥 Offline Download Manager & Storage",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Manage downloaded lectures, summaries, PYQs & storage space",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // --- Offline Study Mode & Night Mode Controls ---
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = BentoSurface,
                border = BorderStroke(1.dp, BentoBorder.copy(alpha = 0.35f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Dark Mode Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = BentoPrimaryContainer,
                                shape = CircleShape,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = if (isDarkMode) Icons.Filled.DarkMode else Icons.Filled.LightMode,
                                        contentDescription = null,
                                        tint = BentoOnPrimaryContainer,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Night Study Dark Mode",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Eye-safe dark interface for late night study",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { onToggleDarkMode() },
                            modifier = Modifier.testTag("dark_mode_switch")
                        )
                    }

                    Divider(modifier = Modifier.padding(vertical = 12.dp), color = BentoBorder.copy(alpha = 0.3f))

                    // Offline Only Switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = BentoSurfaceVariant,
                                shape = CircleShape,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Filled.WifiOff,
                                        contentDescription = null,
                                        tint = BentoPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Offline Study Mode Only",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Hide online stream; access saved offline files only",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Switch(
                            checked = isOfflineOnlyMode,
                            onCheckedChange = { onToggleOfflineOnlyMode() },
                            modifier = Modifier.testTag("offline_mode_switch")
                        )
                    }
                }
            }
        }

        // --- Storage Space Manager Card ---
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = BentoCardSecondary,
                border = BorderStroke(1.dp, BentoBorder.copy(alpha = 0.35f))
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Filled.SdCard,
                                contentDescription = null,
                                tint = BentoPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Device Storage Space",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "$totalSizeMb MB Saved",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = BentoPrimary
                            )

                            if (downloadedMaterials.isNotEmpty()) {
                                TextButton(
                                    onClick = { showClearCacheConfirmation = true },
                                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFFF8A80))
                                ) {
                                    Text("Clear All", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    // Multi-segmented Storage Progress Bar
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        LinearProgressIndicator(
                            progress = { (totalSizeMb.toFloat() / 500f).coerceAtMost(1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(5.dp)),
                            color = BentoPrimary,
                            trackColor = Color.White.copy(alpha = 0.15f)
                        )

                        // Breakdown Legend
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "📹 Videos: ${lectureSizeBytes / (1024 * 1024)}MB",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Text(
                                text = "📄 Notes: ${notesSizeBytes / (1024 * 1024)}MB",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Text(
                                text = "📑 PYQs: ${pyqSizeBytes / (1024 * 1024)}MB",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
        }

        // --- Category Filters Row ---
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val filters = listOf(
                    "ALL" to "All Saved (${downloadedMaterials.size})",
                    "LECTURE" to "Video Lectures",
                    "NOTES" to "Summaries & Notes",
                    "PYQ_NEET" to "PYQs & NEET",
                    "ACTIVE" to "Active Queue (${downloadedMaterials.count { it.downloadStatus != "COMPLETED" }})"
                )

                items(filters) { (key, label) ->
                    val isSelected = selectedFilter == key
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedFilter = key },
                        label = { Text(label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BentoPrimaryContainer,
                            selectedLabelColor = BentoOnPrimaryContainer,
                            containerColor = BentoSurface
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = BentoBorder.copy(alpha = 0.35f)
                        )
                    )
                }
            }
        }

        // --- List of Download Items ---
        if (filteredMaterials.isEmpty()) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = BentoSurface
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (downloadedMaterials.isEmpty())
                                "No offline materials downloaded yet. Tap download on lectures, chapter notes, or PYQ papers!"
                            else "No items match the selected category filter.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(filteredMaterials) { item ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = BentoSurface,
                    border = BorderStroke(1.dp, BentoBorder.copy(alpha = 0.3f))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Surface(
                                    color = when (item.type) {
                                        "LECTURE" -> BentoPrimaryContainer
                                        "NOTES" -> BentoSurfaceVariant
                                        else -> BentoNeetRedContainer
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.size(38.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = when (item.type) {
                                                "LECTURE" -> Icons.Filled.VideoFile
                                                "NOTES" -> Icons.Filled.Article
                                                else -> Icons.Filled.FileDownload
                                            },
                                            contentDescription = null,
                                            tint = when (item.type) {
                                                "LECTURE" -> BentoOnPrimaryContainer
                                                "NOTES" -> BentoPrimary
                                                else -> BentoNeetRedText
                                            },
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = item.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "${item.subjectId} • ${item.type} • ${(item.fileSizeBytes / (1024 * 1024))} MB",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            // Right Action Controls based on Download Status
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                when (item.downloadStatus) {
                                    "COMPLETED" -> {
                                        Button(
                                            onClick = { viewingOfflineItem = item },
                                            colors = ButtonDefaults.buttonColors(containerColor = BentoPrimaryContainer),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                            modifier = Modifier.height(32.dp)
                                        ) {
                                            Text("View Offline", style = MaterialTheme.typography.labelSmall, color = BentoOnPrimaryContainer)
                                        }
                                    }

                                    "DOWNLOADING" -> {
                                        IconButton(onClick = { onPauseDownload(item.materialId) }) {
                                            Icon(Icons.Filled.PauseCircle, contentDescription = "Pause", tint = BentoPrimary)
                                        }
                                    }

                                    "PAUSED" -> {
                                        IconButton(onClick = { onResumeDownload(item.materialId) }) {
                                            Icon(Icons.Filled.PlayCircle, contentDescription = "Resume", tint = BentoPrimary)
                                        }
                                    }

                                    "INTERRUPTED" -> {
                                        IconButton(onClick = { onRetryDownload(item.materialId) }) {
                                            Icon(Icons.Filled.Refresh, contentDescription = "Retry Download", tint = MaterialTheme.colorScheme.error)
                                        }
                                    }
                                }

                                IconButton(onClick = { onRemoveDownload(item.materialId) }) {
                                    Icon(Icons.Filled.DeleteOutline, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }

                        // Progress Indicator for Active Downloads
                        if (item.downloadStatus != "COMPLETED") {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = when (item.downloadStatus) {
                                            "DOWNLOADING" -> "Downloading ${item.downloadProgressPercent}%..."
                                            "PAUSED" -> "Paused at ${item.downloadProgressPercent}%"
                                            "INTERRUPTED" -> "Interrupted - Tap to retry"
                                            else -> item.downloadStatus
                                        },
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (item.downloadStatus == "INTERRUPTED") MaterialTheme.colorScheme.error else BentoPrimary
                                    )
                                }

                                LinearProgressIndicator(
                                    progress = { item.downloadProgressPercent / 100f },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(4.dp)
                                        .clip(RoundedCornerShape(2.dp)),
                                    color = if (item.downloadStatus == "INTERRUPTED") MaterialTheme.colorScheme.error else BentoPrimary
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- Quiz History Section ---
        item {
            Text(
                text = "📈 Recent Practice Quiz History",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        if (quizAttempts.isEmpty()) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = BentoSurface
                ) {
                    Box(modifier = Modifier.padding(16.dp)) {
                        Text("No practice quiz attempts logged yet. Take chapter quizzes to build retention stats!")
                    }
                }
            }
        } else {
            items(quizAttempts) { attempt ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = BentoSurface,
                    border = BorderStroke(1.dp, BentoBorder.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Chapter Quiz (${attempt.subjectId})",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Score: ${attempt.score} / ${attempt.totalQuestions}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Surface(
                            color = if (attempt.percentage >= 60) Color(0xFF2E7D32) else MaterialTheme.colorScheme.error,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "${attempt.percentage}%",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // --- Offline Content Reader Modal ---
    if (viewingOfflineItem != null) {
        val item = viewingOfflineItem!!
        AlertDialog(
            onDismissRequest = { viewingOfflineItem = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (item.type == "LECTURE") Icons.Filled.VideoFile else Icons.Filled.Article,
                        contentDescription = null,
                        tint = BentoPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Offline Content Viewer", style = MaterialTheme.typography.titleMedium)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Surface(
                        color = BentoPrimaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "⚡ OFFLINE MODE - Saved to Local Device",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = BentoOnPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Surface(
                        color = BentoSurfaceVariant,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = item.contentPreviewText.ifBlank { "Full offline content saved on local device storage." },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { viewingOfflineItem = null }) {
                    Text("Close Reader")
                }
            }
        )
    }

    // --- Clear Cache Confirmation Dialog ---
    if (showClearCacheConfirmation) {
        AlertDialog(
            onDismissRequest = { showClearCacheConfirmation = false },
            title = { Text("Clear All Offline Cache?") },
            text = { Text("This will remove all downloaded video lectures, chapter notes, and PYQ papers from local storage to free up disk space.") },
            confirmButton = {
                Button(
                    onClick = {
                        onClearAllDownloads()
                        showClearCacheConfirmation = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Clear All Space")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearCacheConfirmation = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
