package com.example.user.chatfirebase.model

import com.example.user.chatfirebase.controller.ChatItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import java.lang.Exception
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
                    val body = it.child("body").value as? String
                    val image = it.child("image").value as? String
                    if (body != null) {
                        ChatItem.chat(name, body)
                    } else {
                        ChatItem.image(name, image!!)
                        // body와 달리 image에 대한 null 체크를 else로 묶어서 image!!를 해주어야 한다.
                    }

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


    private fun generateImageName(): String {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormat.forPattern("yy_MM_dd_HH_mm_ss")
        return "firsebase_chat_${now.toString(formatter)}.jpeg"
    }

    //Task라이브러리가 몇개의 Thread를 만들지 결정한다.
    // Task와 Main Thread의 Context는 다르다.
    fun postImage(data: ByteArray,
                  onFailure: ((Exception) -> Unit)? = null,
                  onSuccess: ((String) -> Unit)? = null) {
        val storage = FirebaseStorage.getInstance()
        val imageRef = storage.getReference("images")
        val uploadRef = imageRef.child(generateImageName())
//        파일명에서 ref 를 뽑아내야 한다.
        // 내가 설정한 이름으로 ref가 생성된다.

        val uploadTask = uploadRef.putBytes(data)
        with(uploadTask) {
            addOnFailureListener {
                onFailure?.invoke(it)
            }

            addOnSuccessListener {
                //                it.storage.downloadUrl

                it.storage.downloadUrl?.let {
                    task ->
                    task.addOnSuccessListener { uri ->
                        onSuccess?.invoke(uri.toString())
                    }

                }

//                it.metadata?.reference?.downloadUrl?.let { task ->
//                    task.addOnSuccessListener { uri ->
//                        onSuccess?.invoke(uri.toString())
//                    }
//                }
            }
        }
        // 언제 성공했는지 시점을 알 수 없으므로, 콜백을 만들어야 한다.


    }
}
