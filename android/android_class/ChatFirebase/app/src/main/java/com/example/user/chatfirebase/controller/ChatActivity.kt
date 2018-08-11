package com.example.user.chatfirebase.controller

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.user.chatfirebase.R
import com.example.user.chatfirebase.model.ChatModel
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


class ChatViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat, parent, false))

class ChatAdapter : RecyclerView.Adapter<ChatViewHolder>() {
    //    var items: List<ChatItem> = emptyList()
    var items: List<ChatItem> by Delegates.observable(emptyList()) { _, _, _ ->

        notifyDataSetChanged()
    }
    // 첫번째 인자는 KProperty(reflection)

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
    private lateinit var adapter: ChatAdapter // recycler view adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        adapter = ChatAdapter()

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        // recycler view 에 adapter 등록, layoutManager 설정.

//        adapter.items = chatModel.chatItems
        chatModel.onChangeChatItems = {
            adapter.items = it
//            recyclerView.scrollToPosition(it.size - 1)
            recyclerView.smoothScrollToPosition(recyclerView.adapter.itemCount - 1)
//            recyclerView.betterSmoothScrollToPosition(it.size - 1)

        }



        sendButton.setOnClickListener {
            //            chatModel.postChat(ChatItem(name = "yun", body = "hello"))
            FirebaseAuth.getInstance().currentUser?.let {
                val name = it.displayName
                val body = editText.text

                if (body.isNotBlank()) {
                    chatModel.postChat(ChatItem(name
                            ?: "unnamed", body.toString()))
//                    recyclerView.smoothScrollToPosition(recyclerView.adapter.itemCount)
                    editText.text.clear()

                }

            }
        }
    }
}

fun RecyclerView.betterSmoothScrollToPosition(targetItem: Int) {
    layoutManager?.apply {
        val maxScroll = 10
        when (this) {
            is LinearLayoutManager -> {
                val topItem = findFirstVisibleItemPosition()
                val distance = topItem - targetItem
                val anchorItem = when {
                    distance > maxScroll -> targetItem + maxScroll
                    distance < -maxScroll -> targetItem - maxScroll
                    else -> topItem
                }
                if (anchorItem != topItem) scrollToPosition(anchorItem)
                post {
                    smoothScrollToPosition(targetItem)
                }
            }
            else -> smoothScrollToPosition(targetItem)
        }
    }
}
// smoothScrollToPosition 은 가장 아래 position 으로 가기위해서 위의 리스트를 모두 스크롤 해야 하지만,
// betterSmoothScrollToPosition 은 maxScroll 까지의 개수만 스크롤하고 바로 최근의 리스트로 위치를 이동한다.


// _ (파라미터) :쓰지는 않지만 경고를 막기위한것.