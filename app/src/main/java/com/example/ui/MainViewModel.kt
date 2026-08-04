package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.*
import com.example.data.repository.StudyRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppNavTab {
    DASHBOARD,
    PLANNER,
    SUBJECTS,
    NEET_CORNER,
    COMMUNITY_FORUM,
    OFFLINE_ANALYTICS,
    ADMIN_PANEL
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    val repository = StudyRepository(db.studyDao())

    // --- App Configuration & Theme ---
    private val _isDarkMode = MutableStateFlow(true) // Default dark theme for night study sessions
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _isOfflineOnlyMode = MutableStateFlow(false)
    val isOfflineOnlyMode: StateFlow<Boolean> = _isOfflineOnlyMode.asStateFlow()

    private val _selectedExamCategory = MutableStateFlow(ExamCategory.WBCHSE_12)
    val selectedExamCategory: StateFlow<ExamCategory> = _selectedExamCategory.asStateFlow()

    private val _currentNavTab = MutableStateFlow(AppNavTab.DASHBOARD)
    val currentNavTab: StateFlow<AppNavTab> = _currentNavTab.asStateFlow()

    // --- Active Selection State ---
    private val _selectedSubjectId = MutableStateFlow("PHYS")
    val selectedSubjectId: StateFlow<String> = _selectedSubjectId.asStateFlow()

    private val _selectedChapterId = MutableStateFlow("PHYS_CH1")
    val selectedChapterId: StateFlow<String> = _selectedChapterId.asStateFlow()

    // --- Room Database Flows ---
    val userProgressList: StateFlow<List<UserProgressEntity>> = repository.allProgress
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val downloadedMaterials: StateFlow<List<DownloadedMaterialEntity>> = repository.allDownloads
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val subjectGoals: StateFlow<List<SubjectGoalEntity>> = repository.allSubjectGoals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val quizAttempts: StateFlow<List<QuizAttemptEntity>> = repository.allQuizAttempts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val forumPosts: StateFlow<List<ForumPostEntity>> = repository.forumPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val forumReplies: StateFlow<List<ForumReplyEntity>> = repository.allForumReplies
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Admin Custom Uploaded Content State ---
    private val _customLectures = MutableStateFlow<List<VideoLecture>>(emptyList())
    val customLectures: StateFlow<List<VideoLecture>> = _customLectures.asStateFlow()

    private val _customNotes = MutableStateFlow<List<ChapterNote>>(emptyList())
    val customNotes: StateFlow<List<ChapterNote>> = _customNotes.asStateFlow()

    private val _broadcastNotices = MutableStateFlow<List<Triple<String, String, String>>>(
        listOf(
            Triple("WBCHSE HS 2027 Practical Exam Schedule Out", "WBCHSE BOARD UPDATE", "Official notification from West Bengal Council of Higher Secondary Education regarding Physics, Chemistry & Biology laboratory exam dates."),
            Triple("NEET 2027 High-Yield Biology Revision Webinar", "NEET HUB", "Join Dr. S. Roy for a 2-hour live marathon on Genetics & Molecular Biology this Sunday at 7 PM.")
        )
    )
    val broadcastNotices: StateFlow<List<Triple<String, String, String>>> = _broadcastNotices.asStateFlow()

    // --- AI / Smart Doubt Solver ---
    private val _doubtQuery = MutableStateFlow("")
    val doubtQuery: StateFlow<String> = _doubtQuery.asStateFlow()

    private val _doubtResponse = MutableStateFlow<String?>(null)
    val doubtResponse: StateFlow<String?> = _doubtResponse.asStateFlow()

    private val _isSolvingDoubt = MutableStateFlow(false)
    val isSolvingDoubt: StateFlow<Boolean> = _isSolvingDoubt.asStateFlow()

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfNeeded()
        }
    }

    // --- Actions ---
    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun toggleOfflineOnlyMode() {
        _isOfflineOnlyMode.value = !_isOfflineOnlyMode.value
    }

    fun setExamCategory(category: ExamCategory) {
        _selectedExamCategory.value = category
    }

    fun navigateToTab(tab: AppNavTab) {
        _currentNavTab.value = tab
    }

    fun selectSubject(subjectId: String) {
        _selectedSubjectId.value = subjectId
        val firstChapter = repository.getChaptersBySubject(subjectId).firstOrNull()
        if (firstChapter != null) {
            _selectedChapterId.value = firstChapter.id
        }
        _currentNavTab.value = AppNavTab.SUBJECTS
    }

    fun selectChapter(chapterId: String) {
        _selectedChapterId.value = chapterId
    }

    fun markLectureCompleted(chapterId: String, subjectId: String, totalLectures: Int) {
        viewModelScope.launch {
            val existing = userProgressList.value.find { it.chapterId == chapterId }
            val completedCount = (existing?.completedLectures ?: 0) + 1
            val updated = UserProgressEntity(
                chapterId = chapterId,
                subjectId = subjectId,
                completedLectures = completedCount.coerceAtMost(totalLectures),
                totalLectures = totalLectures,
                isNotesRead = existing?.isNotesRead ?: false,
                quizBestScorePercent = existing?.quizBestScorePercent ?: 0
            )
            repository.markProgress(updated)
        }
    }

    fun toggleDownloadMaterial(materialId: String, title: String, subjectId: String, type: String, sizeMb: Long) {
        viewModelScope.launch {
            val existing = downloadedMaterials.value.find { it.materialId == materialId }
            if (existing != null) {
                repository.removeDownload(materialId)
            } else {
                repository.downloadMaterial(
                    DownloadedMaterialEntity(
                        materialId = materialId,
                        title = title,
                        subjectId = subjectId,
                        type = type,
                        fileSizeBytes = sizeMb * 1024 * 1024,
                        downloadStatus = "COMPLETED",
                        downloadProgressPercent = 100,
                        contentPreviewText = "Downloaded $title ($type) for offline WBCHSE/NEET revision."
                    )
                )
            }
        }
    }

    fun pauseDownload(materialId: String) {
        viewModelScope.launch {
            val item = downloadedMaterials.value.find { it.materialId == materialId }
            if (item != null) {
                repository.updateDownloadStatus(materialId, "PAUSED", item.downloadProgressPercent)
            }
        }
    }

    fun resumeDownload(materialId: String) {
        viewModelScope.launch {
            val item = downloadedMaterials.value.find { it.materialId == materialId }
            if (item != null) {
                repository.updateDownloadStatus(materialId, "DOWNLOADING", item.downloadProgressPercent)
                // Simulate download progress finish
                kotlinx.coroutines.delay(1000)
                repository.updateDownloadStatus(materialId, "COMPLETED", 100)
            }
        }
    }

    fun retryDownload(materialId: String) {
        viewModelScope.launch {
            repository.updateDownloadStatus(materialId, "DOWNLOADING", 10)
            kotlinx.coroutines.delay(1200)
            repository.updateDownloadStatus(materialId, "COMPLETED", 100)
        }
    }

    fun clearAllDownloads() {
        viewModelScope.launch {
            repository.clearAllDownloads()
        }
    }

    // --- Personalized Study Planner Actions ---
    fun updateSubjectGoal(
        subjectId: String,
        weeklyTargetHours: Int,
        targetScorePercent: Int,
        weakTopicsCsv: String,
        customNotes: String
    ) {
        viewModelScope.launch {
            val existing = subjectGoals.value.find { it.subjectId == subjectId }
            val updatedGoal = SubjectGoalEntity(
                subjectId = subjectId,
                weeklyTargetHours = weeklyTargetHours,
                completedHoursThisWeek = existing?.completedHoursThisWeek ?: 0f,
                targetScorePercent = targetScorePercent,
                weakTopicsCsv = weakTopicsCsv,
                customNotes = customNotes
            )
            repository.saveSubjectGoal(updatedGoal)
        }
    }

    fun logStudyHours(subjectId: String, addedHours: Float) {
        viewModelScope.launch {
            val existing = subjectGoals.value.find { it.subjectId == subjectId }
            if (existing != null) {
                val updated = existing.copy(
                    completedHoursThisWeek = existing.completedHoursThisWeek + addedHours
                )
                repository.saveSubjectGoal(updated)
            } else {
                repository.saveSubjectGoal(
                    SubjectGoalEntity(
                        subjectId = subjectId,
                        weeklyTargetHours = 8,
                        completedHoursThisWeek = addedHours,
                        targetScorePercent = 85
                    )
                )
            }
        }
    }

    fun getSmartRecommendations(): List<StudyRecommendation> {
        val list = mutableListOf<StudyRecommendation>()

        // 1. Check Weak Areas from Quiz Attempts (<60% score)
        val lowScoreAttempts = quizAttempts.value.filter { it.percentage < 60 }
        lowScoreAttempts.take(2).forEach { attempt ->
            list.add(
                StudyRecommendation(
                    id = "rec_quiz_${attempt.id}",
                    subjectId = attempt.subjectId,
                    subjectName = repository.getSubjectById(attempt.subjectId)?.name ?: attempt.subjectId,
                    title = "Re-attempt Low Score Quiz",
                    description = "Your recent score was ${attempt.percentage}%. Re-take quiz to improve formula recall.",
                    reason = "Identified Weak Area (<60% Score)",
                    priority = "HIGH",
                    actionType = "QUIZ",
                    targetChapterId = attempt.chapterId
                )
            )
        }

        // 2. Check Unstarted / Incomplete Chapters for Selected Stream
        val currentSubject = repository.getSubjectById(selectedSubjectId.value) ?: repository.getSubjects().first()
        val chapters = repository.getChaptersBySubject(currentSubject.id)
        val uncompleted = chapters.filter { ch ->
            val progress = userProgressList.value.find { it.chapterId == ch.id }
            progress == null || progress.completedLectures == 0
        }

        uncompleted.take(2).forEach { ch ->
            list.add(
                StudyRecommendation(
                    id = "rec_ch_${ch.id}",
                    subjectId = ch.subjectId,
                    subjectName = currentSubject.name,
                    title = "Cover Next Chapter: ${ch.title}",
                    description = ch.subtitle,
                    reason = if (ch.isNeetHighYield) "NEET High Yield Topic" else "WBCHSE Class 12 Syllabus Priority",
                    priority = if (ch.isNeetHighYield) "HIGH" else "MEDIUM",
                    actionType = "CHAPTER",
                    targetChapterId = ch.id
                )
            )
        }

        // 3. Add NEET High Yield Recommendation if empty or for general practice
        if (list.size < 4) {
            list.add(
                StudyRecommendation(
                    id = "rec_neet_pyq_2023",
                    subjectId = "BIO",
                    subjectName = "Biology & Chemistry",
                    title = "Solve NEET UG 2023 Solved Paper",
                    description = "Practice 45 high-yield MCQs with NCERT chapter references.",
                    reason = "NEET Entrance Exam Countdown (May 2027)",
                    priority = "HIGH",
                    actionType = "NEET",
                    targetChapterId = null
                )
            )
        }

        return list
    }

    fun submitQuizScore(chapterId: String, subjectId: String, score: Int, total: Int) {
        viewModelScope.launch {
            val percentage = (score * 100) / total
            repository.saveQuizAttempt(
                QuizAttemptEntity(
                    chapterId = chapterId,
                    subjectId = subjectId,
                    score = score,
                    totalQuestions = total,
                    percentage = percentage
                )
            )

            // Update user progress
            val existing = userProgressList.value.find { it.chapterId == chapterId }
            val best = maxOf(existing?.quizBestScorePercent ?: 0, percentage)
            repository.markProgress(
                UserProgressEntity(
                    chapterId = chapterId,
                    subjectId = subjectId,
                    completedLectures = existing?.completedLectures ?: 0,
                    totalLectures = existing?.totalLectures ?: 1,
                    isNotesRead = existing?.isNotesRead ?: true,
                    quizBestScorePercent = best
                )
            )
        }
    }

    fun postDoubt(title: String, content: String, subjectId: String, category: String) {
        viewModelScope.launch {
            val newPost = ForumPostEntity(
                id = "POST_" + System.currentTimeMillis(),
                authorName = "A. Roy (You)",
                authorRole = selectedExamCategory.value.displayName + " Student",
                subjectId = subjectId,
                category = category,
                title = title,
                content = content,
                timestamp = System.currentTimeMillis(),
                upvotes = 1,
                replyCount = 0,
                isLiked = true
            )
            repository.createForumPost(newPost)
        }
    }

    fun upvotePost(postId: String) {
        viewModelScope.launch {
            repository.upvotePost(postId)
        }
    }

    fun deleteForumPost(postId: String) {
        viewModelScope.launch {
            repository.deleteForumPost(postId)
        }
    }

    // --- Admin Publishing Actions ---
    fun publishVideoLecture(
        title: String,
        subjectId: String,
        chapterId: String,
        tutorName: String,
        durationMinutes: Int,
        sizeMb: Long,
        summary: String
    ) {
        val newLecture = VideoLecture(
            id = "custom_lec_${System.currentTimeMillis()}",
            chapterId = chapterId,
            subjectId = subjectId,
            title = title,
            durationMinutes = durationMinutes,
            tutorName = tutorName,
            videoUrlMock = "https://wbchse.org/lectures/$title.mp4",
            summaryText = summary.ifBlank { "Exclusive WBCHSE Masterclass on $title" },
            timestampsJson = "00:00 - Introduction | 10:00 - Key Derivation | 30:00 - Numerical Examples"
        )
        _customLectures.update { listOf(newLecture) + it }
        toggleDownloadMaterial(newLecture.id, title, subjectId, "LECTURE", sizeMb)
    }

    fun publishChapterNote(
        title: String,
        subjectId: String,
        chapterId: String,
        overview: String,
        sizeMb: Long
    ) {
        val newNote = ChapterNote(
            id = "custom_note_${System.currentTimeMillis()}",
            chapterId = chapterId,
            subjectId = subjectId,
            title = title,
            overviewMarkdown = overview.ifBlank { "Comprehensive study summary for WBCHSE Class 12 board preparation." },
            keyFormulas = listOf("Important Formula 1", "Important Derivation 2", "NCERT Key Concept 3"),
            boardExamTips = "High probability 5-mark question in HS Board Exam."
        )
        _customNotes.update { listOf(newNote) + it }
        toggleDownloadMaterial(newNote.id, title, subjectId, "NOTES", sizeMb)
    }

    fun broadcastNotice(title: String, category: String, content: String) {
        _broadcastNotices.update { listOf(Triple(title, category, content)) + it }
    }

    fun getLecturesForChapter(chapterId: String): List<VideoLecture> {
        val staticLecs = repository.getLecturesForChapter(chapterId)
        val dynamicLecs = customLectures.value.filter { it.chapterId == chapterId }
        return dynamicLecs + staticLecs
    }

    fun getNotesForChapter(chapterId: String): List<ChapterNote> {
        val staticNotes = repository.getNotesForChapter(chapterId)
        val dynamicNotes = customNotes.value.filter { it.chapterId == chapterId }
        return dynamicNotes + staticNotes
    }

    fun addReply(postId: String, content: String) {
        viewModelScope.launch {
            repository.addForumReply(
                ForumReplyEntity(
                    postId = postId,
                    authorName = "You",
                    authorRole = "Student Peer",
                    content = content,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    fun setDoubtQuery(query: String) {
        _doubtQuery.value = query
    }

    fun solveDoubtWithAI(query: String) {
        if (query.isBlank()) return
        _isSolvingDoubt.value = true
        _doubtResponse.value = null

        viewModelScope.launch {
            kotlinx.coroutines.delay(1200) // Simulate fast AI processing
            _doubtResponse.value = generateDoubtSolution(query)
            _isSolvingDoubt.value = false
        }
    }

    private fun generateDoubtSolution(q: String): String {
        val lower = q.lowercase()
        return when {
            "sn1" in lower || "sn2" in lower -> """
                **WBCHSE / NEET Chemistry Concept Solver:**
                
                1. **SN1 Reaction**:
                   - 2-Step Mechanism via **Carbocation** intermediate.
                   - Rate = k[R-X] (1st order kinetics).
                   - Favored by 3° alkyl halides and polar protic solvents (H₂O, EtOH).
                   - Product: **Racemic mixture** (Inversion + Retention).
                   
                2. **SN2 Reaction**:
                   - 1-Step Concerted mechanism via Transition State.
                   - Rate = k[R-X][Nu⁻] (2nd order kinetics).
                   - Favored by 1° alkyl halides and polar aprotic solvents (Acetone, DMSO).
                   - Product: **100% Walden Inversion**.
            """.trimIndent()

            "coulomb" in lower || "dipole" in lower || "gauss" in lower -> """
                **Physics Doubt Resolution:**
                
                - **Coulomb's Law**: F = [1 / (4πε₀)] * [(q₁ q₂) / r²]
                - **Electric Dipole Moment**: p = q × 2a (Directed from negative to positive charge)
                - **Axial Field**: E_axial = [1 / (4πε₀)] * [2pr / (r² - a²)²] ≈ [1 / (4πε₀)] * [2p / r³] for r >> a.
                - **WBCHSE Board Tip**: Always include vector arrows on p̂ and Ê in your derivation diagram for full 5 marks!
            """.trimIndent()

            "dna" in lower || "genetics" in lower || "mendel" in lower -> """
                **Biology / NEET NCERT Master Solution:**
                
                - **Chargaff's Rule**: In double-stranded DNA, [A] = [T] and [G] = [C].
                - **Meselson & Stahl Proof**: Proved semi-conservative DNA replication using N-15 heavy nitrogen isotope in E. coli.
                - **NEET High Yield**: Genetic code is unambiguous, degenerate (61 codons for 20 amino acids), universal, and read in 5' -> 3' direction without commas.
            """.trimIndent()

            "integration" in lower || "derivative" in lower || "limit" in lower -> """
                **Mathematics Calculus Solver:**
                
                - **Integration by Parts (ILATE Rule)**:
                  ∫ u · v dx = u ∫ v dx - ∫ [ (du/dx) ∫ v dx ] dx
                  *Priority order*: **I**nverse, **L**ogarithmic, **A**lgebraic, **T**rigonometric, **E**xponential.
                - **WBCHSE Shortcut**: ∫ e^x [f(x) + f'(x)] dx = e^x f(x) + C.
            """.trimIndent()

            else -> """
                **WBCHSE & NEET Step-by-Step Explanation:**
                
                - **Core Principle**: For your query, review the NCERT Chapter Summary & WBCHSE Model Answer Key.
                - **Key Concept**: Ensure all SI units are maintained (e.g. Charge in Coulombs, Distance in Meters, Mass in Kg).
                - **Exam Tip**: For HS Board exams, state the law/definition clearly before commencing mathematical derivations.
            """.trimIndent()
        }
    }
}
