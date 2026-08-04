package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.DownloadedMaterialEntity
import com.example.data.model.ForumPostEntity
import com.example.data.model.ForumReplyEntity
import com.example.data.model.QuizAttemptEntity
import com.example.data.model.SubjectGoalEntity
import com.example.data.model.UserProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyDao {

    // --- User Progress ---
    @Query("SELECT * FROM user_progress")
    fun getAllProgress(): Flow<List<UserProgressEntity>>

    @Query("SELECT * FROM user_progress WHERE chapterId = :chapterId")
    fun getProgressByChapter(chapterId: String): Flow<UserProgressEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProgress(progress: UserProgressEntity)

    // --- Downloads ---
    @Query("SELECT * FROM downloaded_materials ORDER BY downloadTimestamp DESC")
    fun getAllDownloads(): Flow<List<DownloadedMaterialEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDownload(material: DownloadedMaterialEntity)

    @Query("UPDATE downloaded_materials SET downloadStatus = :status, downloadProgressPercent = :progress WHERE materialId = :materialId")
    suspend fun updateDownloadStatus(materialId: String, status: String, progress: Int)

    @Query("DELETE FROM downloaded_materials WHERE materialId = :materialId")
    suspend fun deleteDownload(materialId: String)

    @Query("DELETE FROM downloaded_materials")
    suspend fun clearAllDownloads()

    @Query("SELECT EXISTS(SELECT 1 FROM downloaded_materials WHERE materialId = :materialId)")
    fun isMaterialDownloaded(materialId: String): Flow<Boolean>

    // --- Subject Study Goals ---
    @Query("SELECT * FROM subject_goals")
    fun getAllSubjectGoals(): Flow<List<SubjectGoalEntity>>

    @Query("SELECT * FROM subject_goals WHERE subjectId = :subjectId")
    fun getGoalForSubject(subjectId: String): Flow<SubjectGoalEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSubjectGoal(goal: SubjectGoalEntity)

    // --- Quiz Attempts ---
    @Query("SELECT * FROM quiz_attempts ORDER BY timestamp DESC")
    fun getAllQuizAttempts(): Flow<List<QuizAttemptEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun recordQuizAttempt(attempt: QuizAttemptEntity)

    // --- Forum Posts & Replies ---
    @Query("SELECT * FROM forum_posts ORDER BY timestamp DESC")
    fun getAllForumPosts(): Flow<List<ForumPostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForumPost(post: ForumPostEntity)

    @Query("DELETE FROM forum_posts WHERE id = :postId")
    suspend fun deleteForumPost(postId: String)

    @Query("UPDATE forum_posts SET upvotes = upvotes + 1, isLiked = 1 WHERE id = :postId")
    suspend fun upvotePost(postId: String)

    @Query("SELECT * FROM forum_replies WHERE postId = :postId ORDER BY timestamp ASC")
    fun getRepliesForPost(postId: String): Flow<List<ForumReplyEntity>>

    @Query("SELECT * FROM forum_replies ORDER BY timestamp ASC")
    fun getAllReplies(): Flow<List<ForumReplyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReply(reply: ForumReplyEntity)
}
