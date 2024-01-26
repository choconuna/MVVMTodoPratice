package com.example.practive

import android.content.Context
import androidx.room.Database
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper

/*
* @Database 의 entities -> entity 정의
* @Database 의 version -> SQLite 버전 지정
 */
@Database(entities = [TodoModel::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao

    // DB 인스턴스를 싱글톤으로 사용하기 위해 companion object 안에 만들어줌
    companion object {
        private var INSTANCE: TodoDatabase? = null

        // getInstance(): 여러 스레드가 접근하지 못하도록 synchronized로 설정
        fun getInstance(context: Context) : TodoDatabase? {
            if(INSTANCE == null) {
                synchronized(TodoDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        TodoDatabase::class.java,
                        "tb_todo"
                    )
                        .fallbackToDestructiveMigration() // DB가 갱신될 때 기존의 테이블을 버리고 새로 사용하도록 설정
                        .build()
                }
            }
            return INSTANCE
        }
    }

    override fun clearAllTables() {
        TODO("Not yet implemented")
    }

    override fun createInvalidationTracker(): InvalidationTracker {
        TODO("Not yet implemented")
    }

    override fun createOpenHelper(config: DatabaseConfiguration): SupportSQLiteOpenHelper {
        TODO("Not yet implemented")
    }
}