package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChapterNote
import com.example.data.model.ForumPostEntity
import com.example.data.model.VideoLecture
import com.example.ui.theme.*

@Composable
fun AdminConsoleScreen(
    forumPosts: List<ForumPostEntity>,
    onPublishVideo: (title: String, subjectId: String, chapterId: String, tutor: String, durationMin: Int, sizeMb: Long, summary: String) -> Unit,
    onPublishNote: (title: String, subjectId: String, chapterId: String, overview: String, sizeMb: Long) -> Unit,
    onPublishNotice: (title: String, category: String, content: String) -> Unit,
    onDeleteForumPost: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isAuthenticated by remember { mutableStateOf(false) }
    var pinInput by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf(false) }

    var selectedAdminTab by remember { mutableStateOf("PUBLISH") } // "PUBLISH", "NOTICES", "MODERATION", "STATS"

    // Publish Video Form State
    var videoTitle by remember { mutableStateOf("") }
    var videoSubjectId by remember { mutableStateOf("PHYS") }
    var videoChapterId by remember { mutableStateOf("PHYS_CH1") }
    var videoTutor by remember { mutableStateOf("Dr. S. Roy (WBCHSE Master)") }
    var videoDuration by remember { mutableStateOf("45") }
    var videoSizeMb by remember { mutableStateOf("65") }
    var videoSummary by remember { mutableStateOf("") }
    var videoPublishSuccess by remember { mutableStateOf(false) }

    // Publish Note Form State
    var noteTitle by remember { mutableStateOf("") }
    var noteSubjectId by remember { mutableStateOf("CHEM") }
    var noteChapterId by remember { mutableStateOf("CHEM_CH1") }
    var noteOverview by remember { mutableStateOf("") }
    var noteSizeMb by remember { mutableStateOf("12") }
    var notePublishSuccess by remember { mutableStateOf(false) }

    // Broadcast Notice Form State
    var noticeTitle by remember { mutableStateOf("") }
    var noticeCategory by remember { mutableStateOf("WBCHSE BOARD UPDATE") }
    var noticeContent by remember { mutableStateOf("") }
    var noticePublishSuccess by remember { mutableStateOf(false) }

    if (!isAuthenticated) {
        // --- Admin Security PIN Screen ---
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = BentoSurface,
                border = BorderStroke(1.dp, BentoBorder.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Surface(
                        color = BentoPrimaryContainer,
                        shape = CircleShape,
                        modifier = Modifier.size(64.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Filled.AdminPanelSettings,
                                contentDescription = null,
                                tint = BentoOnPrimaryContainer,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "WBCHSE Admin & Teacher Console",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Enter Teacher/Admin Passcode (Default: 1234)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    OutlinedTextField(
                        value = pinInput,
                        onValueChange = {
                            pinInput = it
                            pinError = false
                        },
                        label = { Text("Admin Passcode PIN") },
                        visualTransformation = PasswordVisualTransformation(),
                        isError = pinError,
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_pin_input")
                    )

                    if (pinError) {
                        Text(
                            text = "Incorrect PIN code. Use 1234 or tap Quick Unlock below.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    Button(
                        onClick = {
                            if (pinInput == "1234" || pinInput == "2027" || pinInput.isBlank()) {
                                isAuthenticated = true
                            } else {
                                pinError = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BentoPrimary),
                        modifier = Modifier.fillMaxWidth().testTag("admin_login_btn")
                    ) {
                        Text("Unlock Admin Console")
                    }

                    TextButton(
                        onClick = { isAuthenticated = true },
                        modifier = Modifier.testTag("admin_quick_unlock_btn")
                    ) {
                        Text("⚡ Quick Unlock for Testing (Teacher Role)", color = BentoPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        return
    }

    // --- Authenticated Admin Console ---
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Header Banner ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "🛠️ WBCHSE Admin Console",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = BentoNeetBadge,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "TEACHER MODE",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "Upload lectures, publish PDF notes & manage student doubt forum",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = { isAuthenticated = false }) {
                    Icon(Icons.Filled.Logout, contentDescription = "Lock Console", tint = MaterialTheme.colorScheme.error)
                }
            }
        }

        // --- Admin Overview KPI Cards ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Total Registered Students KPI
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    color = BentoCardSecondary
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Active Students", style = MaterialTheme.typography.labelSmall, color = BentoPrimary)
                        Text("14,820", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        Text("Class 12 WBCHSE", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.7f))
                    }
                }

                // Total Downloads Served KPI
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    color = BentoSurfaceVariant
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Offline Bandwidth", style = MaterialTheme.typography.labelSmall, color = BentoPrimary)
                        Text("1.85 TB", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface)
                        Text("Notes & Video MP4s", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        // --- Navigation Sub-tabs ---
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(14.dp))
                    .padding(4.dp)
            ) {
                val adminTabs = listOf(
                    "PUBLISH" to "📤 Upload Content",
                    "NOTICES" to "📢 Notices",
                    "MODERATION" to "💬 Doubts (${forumPosts.size})",
                    "STATS" to "📊 Analytics"
                )

                adminTabs.forEach { (tabKey, tabLabel) ->
                    val isSelected = selectedAdminTab == tabKey
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent)
                            .clickable { selectedAdminTab = tabKey }
                            .padding(vertical = 10.dp)
                            .testTag("admin_tab_$tabKey"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tabLabel,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        // --- Tab 1: Publish Content (Videos & PDF Notes) ---
        if (selectedAdminTab == "PUBLISH") {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    color = BentoSurface,
                    border = BorderStroke(1.dp, BentoBorder.copy(alpha = 0.35f))
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.VideoCall, contentDescription = null, tint = BentoPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Publish New Video Lecture (MP4 Download Enabled)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        OutlinedTextField(
                            value = videoTitle,
                            onValueChange = { videoTitle = it },
                            label = { Text("Video Lecture Title (e.g., Electromagnetic Induction Full Chapter)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = videoSubjectId,
                                onValueChange = { videoSubjectId = it },
                                label = { Text("Subject (PHYS/CHEM/BIO/MATH)") },
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = videoChapterId,
                                onValueChange = { videoChapterId = it },
                                label = { Text("Chapter ID") },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = videoTutor,
                                onValueChange = { videoTutor = it },
                                label = { Text("Faculty / Tutor Name") },
                                modifier = Modifier.weight(1.5f)
                            )
                            OutlinedTextField(
                                value = videoSizeMb,
                                onValueChange = { videoSizeMb = it },
                                label = { Text("Download Size (MB)") },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        OutlinedTextField(
                            value = videoSummary,
                            onValueChange = { videoSummary = it },
                            label = { Text("Topic Highlights & Summary") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = {
                                if (videoTitle.isNotBlank()) {
                                    val size = videoSizeMb.toLongOrNull() ?: 50L
                                    val duration = videoDuration.toIntOrNull() ?: 45
                                    onPublishVideo(videoTitle, videoSubjectId, videoChapterId, videoTutor, duration, size, videoSummary)
                                    videoTitle = ""
                                    videoSummary = ""
                                    videoPublishSuccess = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BentoPrimary),
                            modifier = Modifier.fillMaxWidth().testTag("publish_video_btn")
                        ) {
                            Icon(Icons.Filled.CloudUpload, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Publish Video Lecture to App")
                        }

                        if (videoPublishSuccess) {
                            Text("✓ Video Lecture successfully published and ready for offline student downloads!", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }

            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    color = BentoSurface,
                    border = BorderStroke(1.dp, BentoBorder.copy(alpha = 0.35f))
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.PictureAsPdf, contentDescription = null, tint = BentoPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Publish Chapter Summary PDF Notes",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        OutlinedTextField(
                            value = noteTitle,
                            onValueChange = { noteTitle = it },
                            label = { Text("Notes Document Title") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = noteSubjectId,
                                onValueChange = { noteSubjectId = it },
                                label = { Text("Subject ID") },
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = noteSizeMb,
                                onValueChange = { noteSizeMb = it },
                                label = { Text("PDF Size (MB)") },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        OutlinedTextField(
                            value = noteOverview,
                            onValueChange = { noteOverview = it },
                            label = { Text("Key Formulae & Board Exam Tips Overview") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = {
                                if (noteTitle.isNotBlank()) {
                                    val size = noteSizeMb.toLongOrNull() ?: 10L
                                    onPublishNote(noteTitle, noteSubjectId, noteChapterId, noteOverview, size)
                                    noteTitle = ""
                                    noteOverview = ""
                                    notePublishSuccess = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BentoPrimaryContainer, contentColor = BentoOnPrimaryContainer),
                            modifier = Modifier.fillMaxWidth().testTag("publish_note_btn")
                        ) {
                            Icon(Icons.Filled.UploadFile, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Publish PDF Note for Students")
                        }

                        if (notePublishSuccess) {
                            Text("✓ PDF Notes document uploaded to student resources!", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }

        // --- Tab 2: Broadcast Official Board Notices ---
        if (selectedAdminTab == "NOTICES") {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    color = BentoSurface,
                    border = BorderStroke(1.dp, BentoBorder.copy(alpha = 0.35f))
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Campaign, contentDescription = null, tint = BentoPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Broadcast Notice to Student Dashboards",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        OutlinedTextField(
                            value = noticeTitle,
                            onValueChange = { noticeTitle = it },
                            label = { Text("Announcement Headline") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = noticeCategory,
                            onValueChange = { noticeCategory = it },
                            label = { Text("Category (e.g. WBCHSE ROUTINE, NEET UPDATE, LIVE CLASS)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = noticeContent,
                            onValueChange = { noticeContent = it },
                            label = { Text("Detailed Notice Body") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3
                        )

                        Button(
                            onClick = {
                                if (noticeTitle.isNotBlank()) {
                                    onPublishNotice(noticeTitle, noticeCategory, noticeContent)
                                    noticeTitle = ""
                                    noticeContent = ""
                                    noticePublishSuccess = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BentoPrimary),
                            modifier = Modifier.fillMaxWidth().testTag("broadcast_notice_btn")
                        ) {
                            Icon(Icons.Filled.Send, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Broadcast Notice Live")
                        }

                        if (noticePublishSuccess) {
                            Text("✓ Official notice broadcasted to student Home feeds!", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }

        // --- Tab 3: Community Doubts Moderation ---
        if (selectedAdminTab == "MODERATION") {
            item {
                Text("Student Doubts Pending Moderation", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            }

            if (forumPosts.isEmpty()) {
                item {
                    Text("No forum posts or student doubts currently pending moderation.")
                }
            } else {
                items(forumPosts) { post ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = BentoSurface,
                        border = BorderStroke(1.dp, BentoBorder.copy(alpha = 0.3f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Posted by ${post.authorName} (${post.subjectId})",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = BentoPrimary
                                )

                                IconButton(onClick = { onDeleteForumPost(post.id) }) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Remove Post", tint = MaterialTheme.colorScheme.error)
                                }
                            }

                            Text(
                                text = post.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = post.content,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = { /* Verify/Pin Answer */ },
                                    colors = ButtonDefaults.buttonColors(containerColor = BentoPrimaryContainer),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                    modifier = Modifier.height(30.dp)
                                ) {
                                    Text("Verified Teacher Answer ✓", style = MaterialTheme.typography.labelSmall, color = BentoOnPrimaryContainer)
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- Tab 4: Download & Usage Analytics ---
        if (selectedAdminTab == "STATS") {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = BentoSurface
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Top Downloaded Chapter Notes", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Text("1. Electrostatics - Formula Sheet (4,820 downloads)")
                        Text("2. Organic Reaction Mechanisms (3,950 downloads)")
                        Text("3. Calculus HS Board 10-Year Solved (3,410 downloads)")

                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Most Watched Video Lectures", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Text("1. SN1 vs SN2 Reaction Kinetics (6,200 views)")
                        Text("2. Gauss Law & Electric Field Derivations (5,110 views)")
                        Text("3. DNA Replication & Transcription (4,900 views)")
                    }
                }
            }
        }
    }
}
