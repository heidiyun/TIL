package com.example.user.chatfirebase

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.item_chat.view.*
import kotlin.properties.Delegates

data class ChatItem(val name: String, val body: String) {
    fun toJson(): Map<String, Any> {
        return mapOf(
                "name" to name,
                "body" to body
        )
    }
}

class ChatModel {
    //    var chatItems: List<ChatItem> = emptyList()
    var chatItems: List<ChatItem> by Delegates.observable(emptyList()) { _, _, new ->
        onchangeChatItems?.let {
            it(new)
        }
    }

    private val database = FirebaseDatabase.getInstance()
    private val chatRef = database.getReference("chat-tems")
    var onchangeChatItems: ((List<ChatItem>) -> Unit)? = null

    init {
        chatRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                chatItems = snapshot.children.map {
                    val name = it.child("name").value as String
                    val body = it.child("body").value as String
                    ChatItem(name, body)
                }
            }

        })
    }

    fun postChat(item: ChatItem) {

        val newRef = chatRef.push()
//        ref.setValue("hello")// 그냥 덮어쓴다.
        newRef.setValue(item.toJson())
    }
}

class ChatViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat, parent, false))

class ChatAdapter : RecyclerView.Adapter<ChatViewHolder>() {
    //    var items: List<ChatItem> = emptyList()
    var items: List<ChatItem> by Delegates.observable(emptyList()) { _, _, _ ->

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(parent)
    } // 새로운 뷰 생성

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val item = items[position]
//
//        holder.itemView.nameTextView.text = item.name
//        holder.itemView.bodyTextView.text = item.body
        with(holder.itemView) {
            nameTextView.text = item.name
            bodyTextView.text = item.body
        }

    } // 뷰 재사용
}


//controller
class ChatActivity : AppCompatActivity() {
    private val chatModel = ChatModel()
    lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        adapter = ChatAdapter()
//        adapter.items = chatModel.chatItems
        chatModel.onchangeChatItems = {
            adapter.items = it
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        sendButton.setOnClickListener {
            //            chatModel.postChat(ChatItem(name = "yun", body = "hello"))
            FirebaseAuth.getInstance().currentUser?.let {
                val name = it.displayName
                val body = editText.text

                if (body.isNotBlank()) {
                    chatModel.postChat(ChatItem(name ?: "unnamed", body.toString()))
                    recyclerView.smoothScrollToPosition(recyclerView.adapter.itemCount)
                    editText.text.clear()

                }

            }
            editText.text.clear()
        }
    }
}