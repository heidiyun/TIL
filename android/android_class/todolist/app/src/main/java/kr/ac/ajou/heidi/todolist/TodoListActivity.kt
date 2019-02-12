package kr.ac.ajou.heidi.todolist

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_todo_list.*
import kotlinx.android.synthetic.main.item_todo_list.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TodoListActivity : AppCompatActivity() {

    class TodoListViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater
            .from(parent.context).inflate(R.layout.item_todo_list, parent, false))

    inner class TodoListAdapter : RecyclerView.Adapter<TodoListViewHolder>() {
        var items = arrayListOf<Todo>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListViewHolder = TodoListViewHolder(parent)

        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: TodoListViewHolder, position: Int) {
            val item = items[position]


            with(holder.itemView) {
                subjectTextView.text = item.subject
                solvedCheckBox.isChecked = item.solved

                todoDetailCardView.setOnClickListener {
                    val intent = TodoDetailActivity.newInstance(context, item._id)
                    startActivity(intent)
                }
            }
        }
    }


    val todoLab = TodoLab.get()
    lateinit var adapter: TodoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)

        adapter = TodoListAdapter()
        todolistRecyclerView.adapter = adapter
        todolistRecyclerView.layoutManager = LinearLayoutManager(this)


    }

    private fun getTodoList(token: String) {
        mongoApi.getTodolist("bearer $token").enqueue(object : Callback<ArrayList<Todo>> {
            override fun onFailure(call: Call<ArrayList<Todo>>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<ArrayList<Todo>>, response: Response<ArrayList<Todo>>) {
                response.body()?.let { result ->
                    adapter.items = result
                    todoLab.todolist = result
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_item_new_todo -> {
                val todo = Todo()
                todoLab.addTodo(todo)
                val intent = TodoDetailActivity.newInstance(this, todo._id)
                startActivity(intent)
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
        menuInflater.inflate(R.menu.activity_todo_list, menu)
        return true
    }

    override fun onResume() {
        super.onResume()
        val token = getToken(this) ?: throw Resources.NotFoundException("token is not found")
        getTodoList(token)
    }

}