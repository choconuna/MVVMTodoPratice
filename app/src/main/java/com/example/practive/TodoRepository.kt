package com.example.practive

import android.app.Application
import androidx.lifecycle.LiveData

/*
* DB 혹은 네트워크 통신을 통하여 데이터를 얻는 기능을 분리
* ViewModel 에서는 이 Repository 를 통해 데이터를 얻음
 */
class TodoRepository(application: Application) {
    private var todoDatabase: TodoDatabase = TodoDatabase.getInstance(application)!!
    private var todoDao: TodoDao = todoDatabase.todoDao()
    private var todoItems: LiveData<List<TodoModel>> = todoDao.getTodoListAll()

    fun getTodoListAll(): LiveData<List<TodoModel>> {
        return todoItems
    }

    fun insert(todoModel: TodoModel) {
        try {
            val thread =
                Thread(Runnable { // 별도 스레드를 통해 Room 데이터에 접근해야 함. 연산 시간이 오래 걸리는 작업은 메인 스레드가 아닌 별도의 스레드에서 하도록 되어있음. 그렇지 않으면 런타임 발생
                    todoDao.insert(todoModel)
                }).start()
        } catch(e: Exception) {
            e.printStackTrace()
        }
    }

    fun delete(todoModel: TodoModel) {
        try {
            val thread =
                Thread(Runnable {
                    todoDao.delete(todoModel)
                })
        } catch(e: Exception) { }
    }
}