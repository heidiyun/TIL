package kr.ac.ajou.heidi.todolist

class TodoLab {
    var todolist = arrayListOf<Todo>()

    companion object {
        val todoLab = TodoLab()
        fun get(): TodoLab {
           return todoLab
        }
    }

    fun getTodo(id: String): Todo? {

        for (i in 0.until(todolist.size)) {
            if (todolist[i]._id == id)
                return todolist[i]
        }

        return null
    }

    fun addTodo(todo: Todo) {
        todolist.add(todo)
    }
}