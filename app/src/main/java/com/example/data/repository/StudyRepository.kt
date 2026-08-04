package com.example.data.repository

import com.example.data.local.StudyDao
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

class StudyRepository(private val dao: StudyDao) {

    // --- Static Subjects & Chapters ---
    fun getSubjects(category: ExamCategory? = null): List<Subject> {
        if (category == null) return InitialDataSeed.subjects
        return InitialDataSeed.subjects.filter { it.examCategory == category || it.examCategory == ExamCategory.WBCHSE_12 }
    }

    fun getSubjectById(subjectId: String): Subject? {
        return InitialDataSeed.subjects.find { it.id == subjectId }
    }

    fun getChaptersBySubject(subjectId: String): List<Chapter> {
        return InitialDataSeed.chapters.filter { it.subjectId == subjectId }
    }

    fun getChapterById(chapterId: String): Chapter? {
        return InitialDataSeed.chapters.find { it.id == chapterId }
    }

    fun getLecturesForChapter(chapterId: String): List<VideoLecture> {
        return InitialDataSeed.videoLectures.filter { it.chapterId == chapterId }
    }

    fun getNotesForChapter(chapterId: String): List<ChapterNote> {
        return InitialDataSeed.notes.filter { it.chapterId == chapterId }
    }

    fun getQuizForChapter(chapterId: String): List<QuizQuestion> {
        val chapter = getChapterById(chapterId)
        val subjectId = chapter?.subjectId
        return InitialDataSeed.quizQuestions.filter { q ->
            q.quizId.contains(subjectId ?: "") || q.topicTag.contains(chapter?.title?.take(5) ?: "")
        }.ifEmpty {
            InitialDataSeed.quizQuestions
        }
    }

    fun getPyqsForSubject(subjectId: String): List<PyqItem> {
        return InitialDataSeed.pyqItems.filter { it.subjectId == subjectId || (subjectId == "NEET_HUB" && it.isNeet) }
    }

    fun getAllPyqs(): List<PyqItem> {
        return InitialDataSeed.pyqItems
    }

    // --- Room Database Operations ---
    val allProgress: Flow<List<UserProgressEntity>> = dao.getAllProgress()
    val allDownloads: Flow<List<DownloadedMaterialEntity>> = dao.getAllDownloads()
    val allSubjectGoals: Flow<List<SubjectGoalEntity>> = dao.getAllSubjectGoals()
    val allQuizAttempts: Flow<List<QuizAttemptEntity>> = dao.getAllQuizAttempts()
    val forumPosts: Flow<List<ForumPostEntity>> = dao.getAllForumPosts()
    val allForumReplies: Flow<List<ForumReplyEntity>> = dao.getAllReplies()

    fun getRepliesForPost(postId: String): Flow<List<ForumReplyEntity>> = dao.getRepliesForPost(postId)

    fun isDownloaded(materialId: String): Flow<Boolean> = dao.isMaterialDownloaded(materialId)

    suspend fun markProgress(progress: UserProgressEntity) {
        dao.saveProgress(progress)
    }

    suspend fun downloadMaterial(material: DownloadedMaterialEntity) {
        dao.insertDownload(material)
    }

    suspend fun updateDownloadStatus(materialId: String, status: String, progressPercent: Int) {
        dao.updateDownloadStatus(materialId, status, progressPercent)
    }

    suspend fun removeDownload(materialId: String) {
        dao.deleteDownload(materialId)
    }

    suspend fun clearAllDownloads() {
        dao.clearAllDownloads()
    }

    suspend fun saveSubjectGoal(goal: SubjectGoalEntity) {
        dao.saveSubjectGoal(goal)
    }

    suspend fun saveQuizAttempt(attempt: QuizAttemptEntity) {
        dao.recordQuizAttempt(attempt)
    }

    suspend fun createForumPost(post: ForumPostEntity) {
        dao.insertForumPost(post)
    }

    suspend fun deleteForumPost(postId: String) {
        dao.deleteForumPost(postId)
    }

    suspend fun upvotePost(postId: String) {
        dao.upvotePost(postId)
    }

    suspend fun addForumReply(reply: ForumReplyEntity) {
        dao.insertReply(reply)
    }

    suspend fun seedInitialDataIfNeeded() {
        // Seed Forum
        InitialDataSeed.forumPosts.forEach { post ->
            dao.insertForumPost(post)
        }
        InitialDataSeed.forumReplies.forEach { reply ->
            dao.insertReply(reply)
        }

        // Seed Subject Goals for 6 subjects
        val defaultGoals = listOf(
            SubjectGoalEntity("PHYS", weeklyTargetHours = 8, completedHoursThisWeek = 5.5f, targetScorePercent = 90, weakTopicsCsv = "Gauss Law Derivation, Electric Dipole Torque", customNotes = "Focus on 5-Mark WBCHSE Board Questions"),
            SubjectGoalEntity("CHEM", weeklyTargetHours = 8, completedHoursThisWeek = 6.0f, targetScorePercent = 85, weakTopicsCsv = "SN1/SN2 Kinetics, Aldol Condensation", customNotes = "Practice Organic Conversions & NCERT Reaction Mechanisms"),
            SubjectGoalEntity("BIO", weeklyTargetHours = 6, completedHoursThisWeek = 4.5f, targetScorePercent = 95, weakTopicsCsv = "Meselson & Stahl Nitrogen Isotope Experiment", customNotes = "High Yield NEET Biology Diagrams"),
            SubjectGoalEntity("MATH", weeklyTargetHours = 10, completedHoursThisWeek = 7.0f, targetScorePercent = 85, weakTopicsCsv = "Integration by Parts (ILATE), Limit Evaluation", customNotes = "WBCHSE HS Board Step Marking Formulae"),
            SubjectGoalEntity("BEN", weeklyTargetHours = 4, completedHoursThisWeek = 2.5f, targetScorePercent = 85, weakTopicsCsv = "বাংলা ব্যাকরণ, প্রবন্ধ রচনা", customNotes = "HS Board Essay Formats & Rapid Revision"),
            SubjectGoalEntity("ENG", weeklyTargetHours = 4, completedHoursThisWeek = 3.0f, targetScorePercent = 90, weakTopicsCsv = "Unseen Passage Comprehension, Formal Letters", customNotes = "Grammar Correction & Writing Practice")
        )
        defaultGoals.forEach { goal ->
            dao.saveSubjectGoal(goal)
        }

        // Seed Sample Download Materials with various statuses
        val initialDownloads = listOf(
            DownloadedMaterialEntity(
                materialId = "dl_phys_01",
                title = "Electrostatics - Coulomb's Law & Electric Field Summary",
                subjectId = "PHYS",
                type = "NOTES",
                fileSizeBytes = 12500000L,
                downloadStatus = "COMPLETED",
                downloadProgressPercent = 100,
                contentPreviewText = "Coulomb's Law: F = k*q1*q2/r^2. Vector form includes unit vector r_hat. Electric Dipole Moment p = q*2a. Axial field E_axial = 2kp/r^3. High yield for WBCHSE 5-Mark derivations."
            ),
            DownloadedMaterialEntity(
                materialId = "dl_chem_01",
                title = "Haloalkanes & Haloarenes - SN1 vs SN2 Video Lecture",
                subjectId = "CHEM",
                type = "LECTURE",
                fileSizeBytes = 45000000L,
                downloadStatus = "COMPLETED",
                downloadProgressPercent = 100,
                contentPreviewText = "Complete 45-minute video lecture covering SN1 Carbocation intermediate, polar protic solvent effects, and SN2 Walden inversion with 3D molecular animation."
            ),
            DownloadedMaterialEntity(
                materialId = "dl_pyq_neet_2023",
                title = "NEET UG 2023 Solved Question Paper & NCERT Answer Key",
                subjectId = "NEET_HUB",
                type = "NEET",
                fileSizeBytes = 28000000L,
                downloadStatus = "COMPLETED",
                downloadProgressPercent = 100,
                contentPreviewText = "Official NEET UG 2023 paper with step-by-step solutions for Physics, Chemistry, and Biology. Annotated with NCERT textbook page references."
            ),
            DownloadedMaterialEntity(
                materialId = "dl_bio_01",
                title = "Molecular Basis of Inheritance - DNA Replication Video",
                subjectId = "BIO",
                type = "LECTURE",
                fileSizeBytes = 62000000L,
                downloadStatus = "DOWNLOADING",
                downloadProgressPercent = 65,
                contentPreviewText = "Downloading 60-minute video lecture on Meselson-Stahl experiment, DNA Polymerase III mechanism, and Okazaki fragments."
            ),
            DownloadedMaterialEntity(
                materialId = "dl_math_01",
                title = "WBCHSE HS 2024 Mathematics Model Answer Key",
                subjectId = "MATH",
                type = "PYQ",
                fileSizeBytes = 18000000L,
                downloadStatus = "INTERRUPTED",
                downloadProgressPercent = 40,
                contentPreviewText = "Interrupted due to network signal drop. Step-by-step marking scheme for WBCHSE Class 12 Calculus & Vector Algebra."
            )
        )
        initialDownloads.forEach { download ->
            dao.insertDownload(download)
        }
    }
}
