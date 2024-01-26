package com.example.practive

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TodoDao {

    @Query("SELECT * FROM tb_todo ORDER BY SEQ ASC")
    fun getTodoListAll(): LiveData<List<TodoModel>> // getAll 함수에 LiveData 반환

    @Insert(onConflict = OnConflictStrategy.REPLACE) // @Insert의 onConflict -> 중복된 데이터는 어떻게 처리할 것인지에 대한 처리를 지정
    fun insert(todo: TodoModel)

    @Delete
    fun delete(todo: TodoModel)
}