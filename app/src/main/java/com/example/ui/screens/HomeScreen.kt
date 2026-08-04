package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ExamCategory
import com.example.data.model.Subject
import com.example.ui.AppNavTab
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    selectedCategory: ExamCategory,
    onCategorySelected: (ExamCategory) -> Unit,
    subjects: List<Subject>,
    onSelectSubject: (String) -> Unit,
    onNavigateTab: (AppNavTab) -> Unit,
    onOpenDoubtSolver: () -> Unit,
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    completedChaptersCount: Int,
    totalChaptersCount: Int,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val progressFraction = if (totalChaptersCount > 0) completedChaptersCount.toFloat() / totalChaptersCount else 0.75f
    val progressPercentage = (progressFraction * 100).toInt()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // --- Bento App Bar Header ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "WBCHSE Pro",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = BentoPrimary
                )
                Text(
                    text = "Science Stream • Class XII",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF938F99)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Streak Badge Bento Capsule
                Surface(
                    color = BentoPrimaryContainer,
                    shape = CircleShape,
                    border = BorderStroke(1.dp, BentoBorder.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.LocalFireDepartment,
                            contentDescription = "Streak",
                            tint = Color(0xFFFFB74D),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "7 Days",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = BentoOnPrimaryContainer
                        )
                    }
                }

                // Admin Console Button
                IconButton(
                    onClick = { onNavigateTab(AppNavTab.ADMIN_PANEL) },
                    modifier = Modifier
                        .size(40.dp)
                        .background(BentoPrimaryContainer, CircleShape)
                        .border(1.dp, BentoBorder.copy(alpha = 0.3f), CircleShape)
                        .testTag("admin_console_btn")
                ) {
                    Icon(
                        imageVector = Icons.Filled.AdminPanelSettings,
                        contentDescription = "Admin Console",
                        tint = BentoOnPrimaryContainer
                    )
                }

                // Dark Mode Toggle Button
                IconButton(
                    onClick = onToggleDarkMode,
                    modifier = Modifier
                        .size(40.dp)
                        .background(BentoSurfaceVariant, CircleShape)
                        .border(1.dp, BentoBorder.copy(alpha = 0.3f), CircleShape)
                        .testTag("dark_mode_toggle_btn")
                ) {
                    Icon(
                        imageVector = if (isDarkMode) Icons.Filled.DarkMode else Icons.Filled.LightMode,
                        contentDescription = "Toggle Night Study Dark Mode",
                        tint = BentoPrimary
                    )
                }
            }
        }

        // --- Target Category Selector Chips ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ExamCategory.values().forEach { category ->
                val isSelected = category == selectedCategory
                FilterChip(
                    selected = isSelected,
                    onClick = { onCategorySelected(category) },
                    label = {
                        Text(
                            text = category.displayName,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BentoPrimaryContainer,
                        selectedLabelColor = BentoOnPrimaryContainer,
                        containerColor = BentoSurface,
                        labelColor = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = CircleShape,
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = BentoBorder.copy(alpha = 0.4f),
                        selectedBorderColor = BentoPrimary
                    ),
                    modifier = Modifier.testTag("category_chip_${category.name}")
                )
            }
        }

        // ==========================================
        // BENTO GRID CARD 1: Progress Tracking Card
        // ==========================================
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            color = BentoSurfaceVariant,
            border = BorderStroke(1.dp, BentoBorder.copy(alpha = 0.35f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = "Daily Progress",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Medium,
                            color = BentoPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "$progressPercentage%",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Progress Ring Badge
                    Surface(
                        modifier = Modifier.size(52.dp),
                        shape = CircleShape,
                        color = Color.Transparent,
                        border = BorderStroke(3.dp, BentoPrimary)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "$completedChaptersCount/$totalChaptersCount",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Current: Organic Chemistry & Electromagnetism",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(
                            onClick = { onNavigateTab(AppNavTab.OFFLINE_ANALYTICS) },
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.height(24.dp)
                        ) {
                            Text("Stats →", style = MaterialTheme.typography.labelSmall, color = BentoPrimary, fontWeight = FontWeight.Bold)
                        }
                    }

                    LinearProgressIndicator(
                        progress = { progressFraction.coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = BentoPrimary,
                        trackColor = BentoBorder.copy(alpha = 0.3f)
                    )
                }
            }
        }

        // ==========================================
        // BENTO GRID CARD 2 & 3: 2-Column Split Cards
        // ==========================================
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Bento Tile 2A: AI Doubt & Video Lectures
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .height(140.dp)
                    .clickable { onOpenDoubtSolver() },
                shape = RoundedCornerShape(24.dp),
                color = BentoCardSecondary,
                border = BorderStroke(1.dp, BentoBorder.copy(alpha = 0.35f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ASK & WATCH",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = BentoPrimary
                        )
                        Icon(
                            Icons.Filled.AutoAwesome,
                            contentDescription = "AI Doubt",
                            tint = BentoPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "AI Doubt &\nLectures",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            lineHeight = 20.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "48 Solved Topics",
                            style = MaterialTheme.typography.labelSmall,
                            color = BentoPrimary.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            // Bento Tile 2B: NEET Edge Special
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .height(140.dp)
                    .clickable { onNavigateTab(AppNavTab.NEET_CORNER) },
                shape = RoundedCornerShape(24.dp),
                color = BentoNeetRedContainer,
                border = BorderStroke(1.dp, BentoNeetRedBorder.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "NEET EDGE",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = BentoNeetRedText
                        )
                        Surface(
                            color = BentoNeetBadge,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "PREP",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Column {
                        Text(
                            text = "PYQ Mocks &\nRank Predictor",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = BentoNeetRedText,
                            lineHeight = 20.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "2018-2025 Solved",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFFFDAD6).copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        // ==========================================
        // BENTO GRID CARD 4: Horizontal Subject Bento Tiles
        // ==========================================
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Stream Subjects",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            // Grid of Subject Bento Tiles
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                subjects.chunked(2).forEach { rowSubjects ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowSubjects.forEach { subject ->
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onSelectSubject(subject.id) }
                                    .testTag("subject_card_${subject.code}"),
                                shape = RoundedCornerShape(20.dp),
                                color = BentoSurface,
                                border = BorderStroke(1.dp, BentoBorder.copy(alpha = 0.35f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        color = Color(subject.accentColorHex).copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.size(42.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = when (subject.code) {
                                                    "PHYS" -> Icons.Filled.Bolt
                                                    "CHEM" -> Icons.Filled.Science
                                                    "BIO" -> Icons.Filled.Eco
                                                    "MATH" -> Icons.Filled.Calculate
                                                    "BEN" -> Icons.Filled.MenuBook
                                                    "ENG" -> Icons.Filled.Translate
                                                    else -> Icons.Filled.HealthAndSafety
                                                },
                                                contentDescription = subject.name,
                                                tint = Color(subject.accentColorHex),
                                                modifier = Modifier.size(22.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = subject.name,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "${subject.totalChapters} Ch. • ${subject.code}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                        if (rowSubjects.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        // ==========================================
        // BENTO GRID CARD 5: Interactive Quiz Challenge Tile
        // ==========================================
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateTab(AppNavTab.SUBJECTS) },
            shape = RoundedCornerShape(24.dp),
            color = BentoSurfaceVariant,
            border = BorderStroke(1.dp, BentoBorder.copy(alpha = 0.3f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        color = BentoPrimaryContainer,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Filled.Quiz,
                                contentDescription = "Daily Quiz",
                                tint = BentoOnPrimaryContainer,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Column {
                        Text(
                            text = "Daily Revision Challenge",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Test formula recall for WBCHSE & NEET",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    color = BentoPrimaryContainer,
                    shape = CircleShape
                ) {
                    Text(
                        text = "START",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = BentoOnPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                    )
                }
            }
        }

        // ==========================================
        // BENTO GRID CARD 6: Community & Peer Support Tile
        // ==========================================
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateTab(AppNavTab.COMMUNITY_FORUM) },
            shape = RoundedCornerShape(24.dp),
            color = BentoSurface,
            border = BorderStroke(1.dp, BentoBorder.copy(alpha = 0.35f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        color = BentoSecondaryContainer,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Filled.Groups,
                                contentDescription = "Chhatra Sangha",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Column {
                        Text(
                            text = "Chhatra Sangha Forum",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Peer doubts & WBCHSE top rankers discussion",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Icon(
                    Icons.Filled.ChevronRight,
                    contentDescription = "Open Forum",
                    tint = BentoPrimary
                )
            }
        }
    }
}

