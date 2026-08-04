package com.example.data.model

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ExamCategory(val displayName: String) {
    WBCHSE_11("WBCHSE Class 11"),
    WBCHSE_12("WBCHSE Class 12"),
    NEET("NEET Entrance")
}

enum class SubjectType(val displayName: String, val code: String) {
    PHYSICS("Physics", "PHYS"),
    CHEMISTRY("Chemistry", "CHEM"),
    BIOLOGY("Biology", "BIO"),
    MATHEMATICS("Mathematics", "MATH"),
    BENGALI("বাংলা (Bengali)", "BEN"),
    ENGLISH("English", "ENG")
}

data class Subject(
    val id: String,
    val name: String,
    val code: String,
    val description: String,
    val iconName: String,
    val examCategory: ExamCategory,
    val totalChapters: Int,
    val accentColorHex: Long
)

data class Chapter(
    val id: String,
    val subjectId: String,
    val chapterNumber: Int,
    val title: String,
    val subtitle: String,
    val isNeetHighYield: Boolean = false,
    val videoLecturesCount: Int,
    val notesCount: Int,
    val quizCount: Int,
    val pyqCount: Int
)

data class VideoLecture(
    val id: String,
    val chapterId: String,
    val subjectId: String,
    val title: String,
    val durationMinutes: Int,
    val tutorName: String,
    val videoUrlMock: String,
    val summaryText: String,
    val timestampsJson: String, // e.g. "01:30 - Core Concept | 05:15 - Derivation"
    val isDownloaded: Boolean = false
)

data class ChapterNote(
    val id: String,
    val chapterId: String,
    val subjectId: String,
    val title: String,
    val overviewMarkdown: String,
    val keyFormulas: List<String>,
    val boardExamTips: String,
    val isDownloaded: Boolean = false
)

data class QuizQuestion(
    val id: String,
    val quizId: String,
    val questionText: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val formulaHint: String? = null,
    val topicTag: String
)

data class PyqItem(
    val id: String,
    val subjectId: String,
    val examName: String, // e.g., "WBCHSE HS 2024" or "NEET UG 2023"
    val year: Int,
    val questionNumber: Int,
    val questionText: String,
    val marks: Int,
    val officialAnswerText: String,
    val markingSchemeBreakdown: String,
    val isNeet: Boolean = false
)

@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey val chapterId: String,
    val subjectId: String,
    val completedLectures: Int = 0,
    val totalLectures: Int = 1,
    val isNotesRead: Boolean = false,
    val quizBestScorePercent: Int = 0,
    val lastStudiedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "downloaded_materials")
data class DownloadedMaterialEntity(
    @PrimaryKey val materialId: String,
    val title: String,
    val subjectId: String,
    val type: String, // "LECTURE", "NOTES", "PYQ", "NEET"
    val fileSizeBytes: Long,
    val downloadTimestamp: Long = System.currentTimeMillis(),
    val downloadStatus: String = "COMPLETED", // "COMPLETED", "DOWNLOADING", "PAUSED", "INTERRUPTED"
    val downloadProgressPercent: Int = 100,
    val contentPreviewText: String = ""
)

@Entity(tableName = "subject_goals")
data class SubjectGoalEntity(
    @PrimaryKey val subjectId: String,
    val weeklyTargetHours: Int = 8,
    val completedHoursThisWeek: Float = 0f,
    val targetScorePercent: Int = 85,
    val weakTopicsCsv: String = "",
    val customNotes: String = ""
)

data class StudyRecommendation(
    val id: String,
    val subjectId: String,
    val subjectName: String,
    val title: String,
    val description: String,
    val reason: String, // "Identified Weak Area", "WBCHSE Board Focus", "NEET High Yield"
    val priority: String, // "HIGH", "MEDIUM", "LOW"
    val actionType: String, // "CHAPTER", "QUIZ", "NOTES", "NEET"
    val targetChapterId: String? = null
)

@Entity(tableName = "forum_posts")
data class ForumPostEntity(
    @PrimaryKey val id: String,
    val authorName: String,
    val authorRole: String = "HS Student",
    val subjectId: String,
    val category: String, // "Doubt", "NEET Strategy", "Notes Exchange", "Exam Prep"
    val title: String,
    val content: String,
    val timestamp: Long,
    val upvotes: Int = 0,
    val replyCount: Int = 0,
    val isLiked: Boolean = false
)

@Entity(tableName = "forum_replies")
data class ForumReplyEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val postId: String,
    val authorName: String,
    val authorRole: String = "Peer / Mentor",
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isVerifiedSolution: Boolean = false
)

@Entity(tableName = "quiz_attempts")
data class QuizAttemptEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val chapterId: String,
    val subjectId: String,
    val score: Int,
    val totalQuestions: Int,
    val percentage: Int,
    val timestamp: Long = System.currentTimeMillis()
)
