package com.example.practive

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val todoRepository = TodoRepository(application)
    private var todoItems = todoRepository.getTodoListAll() // Activity(View)에서 ViewModel의 todoItems 리스트를 observe 하고 리스트를 갱심

    /* repository 에 넘겨 viewModel 의 기능 함수 구현 */

    fun getTodoListAll(): LiveData<List<TodoModel>> {
        return todoItems
    }

    fun insert(todoModel: TodoModel) {
        todoRepository.insert(todoModel)
    }

    fun delete(todoModel: TodoModel) {
        todoRepository.delete(todoModel)
    }
}