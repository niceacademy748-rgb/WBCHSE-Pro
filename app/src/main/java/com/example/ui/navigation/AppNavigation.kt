package com.example.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.data.model.ExamCategory
import com.example.ui.AppNavTab
import com.example.ui.MainViewModel
import com.example.ui.components.DoubtSolverSheet
import com.example.ui.screens.*

@Composable
fun AppNavigation(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val currentTab by viewModel.currentNavTab.collectAsState()
    val selectedExamCategory by viewModel.selectedExamCategory.collectAsState()
    val selectedSubjectId by viewModel.selectedSubjectId.collectAsState()
    val selectedChapterId by viewModel.selectedChapterId.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val isOfflineOnlyMode by viewModel.isOfflineOnlyMode.collectAsState()

    val userProgressList by viewModel.userProgressList.collectAsState()
    val downloadedMaterials by viewModel.downloadedMaterials.collectAsState()
    val subjectGoals by viewModel.subjectGoals.collectAsState()
    val quizAttempts by viewModel.quizAttempts.collectAsState()
    val forumPosts by viewModel.forumPosts.collectAsState()
    val forumReplies by viewModel.forumReplies.collectAsState()

    val doubtQuery by viewModel.doubtQuery.collectAsState()
    val doubtResponse by viewModel.doubtResponse.collectAsState()
    val isSolvingDoubt by viewModel.isSolvingDoubt.collectAsState()

    var showDoubtSolverSheet by remember { mutableStateOf(false) }

    val subjects = viewModel.repository.getSubjects(selectedExamCategory)
    val selectedSubject = viewModel.repository.getSubjectById(selectedSubjectId) ?: subjects.first()
    val chapters = viewModel.repository.getChaptersBySubject(selectedSubject.id)
    val lectures = viewModel.repository.getLecturesForChapter(selectedChapterId)
    val notes = viewModel.repository.getNotesForChapter(selectedChapterId)
    val quizzes = viewModel.repository.getQuizForChapter(selectedChapterId)
    val pyqs = viewModel.repository.getPyqsForSubject(selectedSubject.id)

    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("app_bottom_navigation")
            ) {
                val navItems = listOf(
                    Triple(AppNavTab.DASHBOARD, "Home", Icons.Filled.Home to Icons.Outlined.Home),
                    Triple(AppNavTab.PLANNER, "Planner", Icons.Filled.CalendarMonth to Icons.Outlined.CalendarMonth),
                    Triple(AppNavTab.SUBJECTS, "Subjects", Icons.Filled.MenuBook to Icons.Outlined.MenuBook),
                    Triple(AppNavTab.NEET_CORNER, "NEET Hub", Icons.Filled.HealthAndSafety to Icons.Outlined.HealthAndSafety),
                    Triple(AppNavTab.COMMUNITY_FORUM, "Forum", Icons.Filled.Groups to Icons.Outlined.Groups),
                    Triple(AppNavTab.OFFLINE_ANALYTICS, "Downloads", Icons.Filled.FileDownload to Icons.Outlined.FileDownload),
                    Triple(AppNavTab.ADMIN_PANEL, "Admin", Icons.Filled.AdminPanelSettings to Icons.Outlined.AdminPanelSettings)
                )

                navItems.forEach { (tab, label, icons) ->
                    val isSelected = currentTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.navigateToTab(tab) },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) icons.first else icons.second,
                                contentDescription = label
                            )
                        },
                        label = { Text(label) },
                        modifier = Modifier.testTag("nav_tab_${tab.name}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                AppNavTab.DASHBOARD -> {
                    HomeScreen(
                        selectedCategory = selectedExamCategory,
                        onCategorySelected = { viewModel.setExamCategory(it) },
                        subjects = subjects,
                        onSelectSubject = { viewModel.selectSubject(it) },
                        onNavigateTab = { viewModel.navigateToTab(it) },
                        onOpenDoubtSolver = { showDoubtSolverSheet = true },
                        isDarkMode = isDarkMode,
                        onToggleDarkMode = { viewModel.toggleDarkMode() },
                        completedChaptersCount = userProgressList.count { it.completedLectures > 0 },
                        totalChaptersCount = 24
                    )
                }

                AppNavTab.PLANNER -> {
                    StudyPlannerScreen(
                        subjectGoals = subjectGoals,
                        recommendations = viewModel.getSmartRecommendations(),
                        onUpdateGoal = { subId, hours, score, weakTopics, notes ->
                            viewModel.updateSubjectGoal(subId, hours, score, weakTopics, notes)
                        },
                        onLogStudyHours = { subId, hours ->
                            viewModel.logStudyHours(subId, hours)
                        },
                        onNavigateTab = { viewModel.navigateToTab(it) },
                        onSelectSubject = { viewModel.selectSubject(it) }
                    )
                }

                AppNavTab.SUBJECTS -> {
                    SubjectDetailScreen(
                        subject = selectedSubject,
                        chapters = chapters,
                        selectedChapterId = selectedChapterId,
                        onSelectChapter = { viewModel.selectChapter(it) },
                        lectures = lectures,
                        notes = notes,
                        quizzes = quizzes,
                        pyqs = pyqs,
                        downloadedMaterials = downloadedMaterials,
                        onToggleDownload = { matId, title, subjId, type, sizeMb ->
                            viewModel.toggleDownloadMaterial(matId, title, subjId, type, sizeMb)
                        },
                        onMarkLectureCompleted = { chId, subjId, total ->
                            viewModel.markLectureCompleted(chId, subjId, total)
                        },
                        onQuizSubmitted = { chId, subjId, score, total ->
                            viewModel.submitQuizScore(chId, subjId, score, total)
                        }
                    )
                }

                AppNavTab.NEET_CORNER -> {
                    NeetHubScreen(
                        neetPyqs = viewModel.repository.getAllPyqs(),
                        onOpenDoubtSolver = { showDoubtSolverSheet = true }
                    )
                }

                AppNavTab.COMMUNITY_FORUM -> {
                    CommunityForumScreen(
                        posts = forumPosts,
                        onPostDoubt = { title, content, subjId, cat ->
                            viewModel.postDoubt(title, content, subjId, cat)
                        },
                        onUpvotePost = { viewModel.upvotePost(it) },
                        onAddReply = { postId, content -> viewModel.addReply(postId, content) },
                        getReplies = { postId -> forumReplies.filter { it.postId == postId } }
                    )
                }

                AppNavTab.OFFLINE_ANALYTICS -> {
                    OfflineAndAnalyticsScreen(
                        downloadedMaterials = downloadedMaterials,
                        onRemoveDownload = { viewModel.toggleDownloadMaterial(it, "", "", "", 0L) },
                        onPauseDownload = { viewModel.pauseDownload(it) },
                        onResumeDownload = { viewModel.resumeDownload(it) },
                        onRetryDownload = { viewModel.retryDownload(it) },
                        onClearAllDownloads = { viewModel.clearAllDownloads() },
                        quizAttempts = quizAttempts,
                        userProgressList = userProgressList,
                        isDarkMode = isDarkMode,
                        onToggleDarkMode = { viewModel.toggleDarkMode() },
                        isOfflineOnlyMode = isOfflineOnlyMode,
                        onToggleOfflineOnlyMode = { viewModel.toggleOfflineOnlyMode() }
                    )
                }

                AppNavTab.ADMIN_PANEL -> {
                    AdminConsoleScreen(
                        forumPosts = forumPosts,
                        onPublishVideo = { title, subjId, chId, tutor, dur, size, summary ->
                            viewModel.publishVideoLecture(title, subjId, chId, tutor, dur, size, summary)
                        },
                        onPublishNote = { title, subjId, chId, overview, size ->
                            viewModel.publishChapterNote(title, subjId, chId, overview, size)
                        },
                        onPublishNotice = { title, cat, content ->
                            viewModel.broadcastNotice(title, cat, content)
                        },
                        onDeleteForumPost = { viewModel.deleteForumPost(it) }
                    )
                }
            }
        }

        // --- AI Doubt Solver Sheet ---
        if (showDoubtSolverSheet) {
            DoubtSolverSheet(
                queryText = doubtQuery,
                onQueryChange = { viewModel.setDoubtQuery(it) },
                onSolve = { viewModel.solveDoubtWithAI(it) },
                response = doubtResponse,
                isLoading = isSolvingDoubt,
                onDismiss = { showDoubtSolverSheet = false }
            )
        }
    }
}
