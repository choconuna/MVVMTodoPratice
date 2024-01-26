package com.example.practive

import android.content.AbstractThreadedSyncAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val TAG: String = MainActivity::class.java.name

    private lateinit var todoViewModel: TodoViewModel // TodoViewModel 인스턴스를 만들고, 이를 관창

    private lateinit var todoListAdapter: TodoListAdapter
    private var todoItems: ArrayList<TodoModel> = ArrayList()

    private val recyclerview_list: RecyclerView by lazy {
        findViewById<RecyclerView>(R.id.recyclerview_list)
    }

    private val btn_add: Button by lazy {
        findViewById<Button>(R.id.btn_add)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "== onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    /*
    * ViewModel 설정
    * View 에서 ViewModel 을 관찰하여 데이터가 변경될 때 내부적으로 자동으로 알 수 있도록 함
    * ViewModel 은 View와 ViewModel 의 분리로 액티비티가 destory 되었다가 다시 create 되어도 종료되지 않고 가지고 있음
     */
    private fun initViewModel() {
        todoViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(TodoViewModel::class.java)
        todoViewModel.getTodoListAll().observe(this, androidx.lifecycle.Observer {
            todoListAdapter.setTodoItems(it)
        })
    }

    /*
    * Recyclerview 설정
    * Recyclerview adapter 와 LinearLayoutManager 를 만들고 연결
     */
    private fun initRecyclerview() {
        todoListAdapter =
            TodoListAdapter({ todo -> deleteDialog(todo) })
        recyclerview_list.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = todoListAdapter
        }
    }

    /*
    * btn_add 설정
     */
    private fun initBtnAdd() {
        btn_add.setOnClickListener {
            showAddTodoDialog()
        }
    }

    /*
    * Todo 리스트를 추가하는 Dialog 띄우기
    *  TodoModel 을 생성하여 리스트 add 해서 리스트를 갱신
     */
    private fun showAddTodoDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add, null)
        val et_add_title: EditText by lazy {
            dialogView.findViewById<EditText>(R.id.et_add_title)
        }
        val et_add_content: EditText by lazy {
            dialogView.findViewById<EditText>(R.id.et_add_content)
        }
        var builder = AlertDialog.Builder(this)
        val dialog = builder.setTitle("Todo 아이템 추가하기").setView(dialogView)
            .setPositiveButton(
                "확인"
            ) { dialogInterface, i ->
                var id: Long? = null
                val title = et_add_title.text.toString()
                var content = et_add_content.text.toString()
                var createDate = Date().time
                val todoModel = TodoModel(
                    id,
                    todoListAdapter.getItemCount() + 1,
                    title,
                    content,
                    createDate
                )
                todoViewModel.insert(todoModel)
            }
            .setNegativeButton("취소", null)
            .create()
        dialog.show()
    }

    /*
    * Todo 리스트를 삭제하는 Dialog 띄우기
     */
    private fun deleteDialog(todoModel: TodoModel) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(todoModel.seq.toString() + "번 Todo 아이템을 삭제할까요?")
            .setNegativeButton("취소") {_, _ ->}
            .setPositiveButton("확인") {_, _ ->
                todoViewModel.delete(todoModel)
            }
            .create()
        builder.show()
    }
}