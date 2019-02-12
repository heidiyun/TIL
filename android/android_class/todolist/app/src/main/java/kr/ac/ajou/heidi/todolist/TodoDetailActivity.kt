package kr.ac.ajou.heidi.todolist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_todo.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TodoDetailActivity : AppCompatActivity() {

    companion object {
        const val TODO_ID = "kr.ac.ajou.heidi.todo_id"
        fun newInstance(packageContext: Context, todoId: String): Intent {
            val intent = Intent(packageContext, TodoDetailActivity::class.java)
            intent.putExtra(TODO_ID, todoId)
            return intent
        }
    }

    val todoLab = TodoLab.get()
    lateinit var todoId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)

        todoId = intent.getSerializableExtra(TODO_ID) as String

        val todo = todoLab.getTodo(todoId)

        todo?.let {
            todoTitle.setText(it.subject)
            todoSolved.isChecked = it.solved
        }

        todoUploadButton.setOnClickListener {
            uploadTodo()
        }


    }

    private fun createBody(): JsonObject {
        val jsonObject = JsonObject()
        jsonObject.addProperty("subject", todoTitle.text.toString())
        jsonObject.addProperty("solved", todoSolved.isChecked)
        return jsonObject
    }

    private fun uploadTodo() {
        val token = getToken(this) ?: ""

        mongoApi.postTodo("bearer $token", createBody()).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                toast("업로드 완료")
            }

        })
    }


    private fun removeTodo() {
        val token = getToken(this) ?: ""
        val jsonObject = JsonObject()
        jsonObject.addProperty("_id", todoId)

        mongoApi.removeTodo("bearer $token", jsonObject).enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                toast("제거 완료")
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_item_remove_todo -> {
                removeTodo()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
                false
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.activity_todo_detail, menu)
        return true

    }

}
