package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.DownloadedMaterialEntity
import com.example.data.model.ForumPostEntity
import com.example.data.model.ForumReplyEntity
import com.example.data.model.QuizAttemptEntity
import com.example.data.model.SubjectGoalEntity
import com.example.data.model.UserProgressEntity

@Database(
    entities = [
        UserProgressEntity::class,
        DownloadedMaterialEntity::class,
        ForumPostEntity::class,
        ForumReplyEntity::class,
        QuizAttemptEntity::class,
        SubjectGoalEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun studyDao(): StudyDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "wbchse_study_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
