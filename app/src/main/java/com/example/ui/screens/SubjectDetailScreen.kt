package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import com.example.data.model.*
import com.example.ui.components.NotesView
import com.example.ui.components.PyqView
import com.example.ui.components.QuizView
import com.example.ui.components.VideoPlayerView

@Composable
fun SubjectDetailScreen(
    subject: Subject,
    chapters: List<Chapter>,
    selectedChapterId: String,
    onSelectChapter: (String) -> Unit,
    lectures: List<VideoLecture>,
    notes: List<ChapterNote>,
    quizzes: List<QuizQuestion>,
    pyqs: List<PyqItem>,
    downloadedMaterials: List<DownloadedMaterialEntity>,
    onToggleDownload: (materialId: String, title: String, subjectId: String, type: String, sizeMb: Long) -> Unit,
    onMarkLectureCompleted: (chapterId: String, subjectId: String, total: Int) -> Unit,
    onQuizSubmitted: (chapterId: String, subjectId: String, score: Int, total: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var activeModuleTab by remember { mutableStateOf("VIDEO") } // "VIDEO", "NOTES", "QUIZ", "PYQ"

    val currentChapter = chapters.find { it.id == selectedChapterId } ?: chapters.firstOrNull()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // --- Subject Banner ---
        Surface(
            color = Color(subject.accentColorHex),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = subject.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subject.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.9f)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Horizontal Chapter Selector Carousel
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(chapters) { ch ->
                        val isSelected = ch.id == currentChapter?.id
                        Surface(
                            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.25f),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.clickable { onSelectChapter(ch.id) }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (ch.isNeetHighYield) {
                                    Icon(
                                        Icons.Filled.LocalFireDepartment,
                                        contentDescription = "NEET High Yield",
                                        tint = if (isSelected) Color(0xFFFF6D00) else Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                }
                                Text(
                                    text = "Ch ${ch.chapterNumber}: ${ch.title.take(18)}...",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color(subject.accentColorHex) else Color.White
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- Active Chapter Title & Module Tabs ---
        if (currentChapter != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "Chapter ${currentChapter.chapterNumber}: ${currentChapter.title}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Module Sub-tabs (Video / Notes / Quiz / PYQ)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                        .padding(4.dp)
                ) {
                    val modules = listOf(
                        "VIDEO" to "🎥 Videos",
                        "NOTES" to "📝 Notes",
                        "QUIZ" to "🎯 Quiz",
                        "PYQ" to "📑 PYQs"
                    )

                    modules.forEach { (tabKey, tabTitle) ->
                        val isSelected = activeModuleTab == tabKey
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent)
                                .clickable { activeModuleTab = tabKey }
                                .padding(vertical = 8.dp)
                                .testTag("chapter_tab_$tabKey"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tabTitle,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // --- Module Content ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when (activeModuleTab) {
                    "VIDEO" -> {
                        val activeLecture = lectures.firstOrNull()
                        if (activeLecture != null) {
                            val isDownloaded = downloadedMaterials.any { it.materialId == activeLecture.id }
                            VideoPlayerView(
                                lecture = activeLecture,
                                isDownloaded = isDownloaded,
                                onToggleDownload = {
                                    onToggleDownload(activeLecture.id, activeLecture.title, subject.id, "LECTURE", 45L)
                                },
                                onMarkCompleted = {
                                    onMarkLectureCompleted(currentChapter.id, subject.id, currentChapter.videoLecturesCount)
                                }
                            )
                        } else {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text("No video lectures available for this chapter yet.")
                            }
                        }
                    }

                    "NOTES" -> {
                        val activeNote = notes.firstOrNull()
                        val isDownloaded = activeNote != null && downloadedMaterials.any { it.materialId == activeNote.id }
                        NotesView(
                            notes = notes,
                            isDownloaded = isDownloaded,
                            onToggleDownload = {
                                if (activeNote != null) {
                                    onToggleDownload(activeNote.id, activeNote.title, subject.id, "NOTES", 3L)
                                }
                            }
                        )
                    }

                    "QUIZ" -> {
                        QuizView(
                            questions = quizzes,
                            onQuizSubmitted = { score, total ->
                                onQuizSubmitted(currentChapter.id, subject.id, score, total)
                            }
                        )
                    }

                    "PYQ" -> {
                        PyqView(
                            pyqs = pyqs,
                            onDownloadPyq = { pyq ->
                                onToggleDownload(pyq.id, pyq.examName + " Solution", subject.id, "PYQ", 3L)
                            }
                        )
                    }
                }
            }
        }
    }
}
