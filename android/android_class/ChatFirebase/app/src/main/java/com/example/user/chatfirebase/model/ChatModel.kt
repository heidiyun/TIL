package com.example.user.chatfirebase.model

import com.example.user.chatfirebase.controller.ChatItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.properties.Delegates

class ChatModel {
    //    var chatItems: List<ChatItem> = emptyList()
    var chatItems: List<ChatItem> by Delegates.observable(emptyList()) { _, _, new ->
        onChangeChatItems?.let {
            it(new)
        }
    }
    // 내용이 바뀌면 onChangeChatItems 에 등록되어 있는 일을 수행한다.
    // 여기서는 새로운 아이템이 들어오면 새로운 아이템을 포함한 리스트를 chatItems 에 저장하도록 하고 있다.

    private val database = FirebaseDatabase.getInstance()
    private val chatRef = database.getReference("chat-items")
    var onChangeChatItems: ((List<ChatItem>) -> Unit)? = null
    // 아이템이 바뀌면 해야 할 일을 담고 있는 변수.

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
        // chatRef 에 이벤트가 생기면 리스너가 호출되낟.
        // 데이터가 바뀌면 map 의 형태로 ChatItem 을 생성한다.
    }

    fun postChat(item: ChatItem) {

        val newRef = chatRef.push()
//        ref.setValue("hello")
//        그냥 덮어쓴다. (ref 가 새로 추가되지 않고)
        newRef.setValue(item.toJson())
    }
}
