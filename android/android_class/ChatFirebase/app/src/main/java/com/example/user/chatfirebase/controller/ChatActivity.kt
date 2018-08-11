package com.example.user.chatfirebase.controller

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.example.user.chatfirebase.R
import com.example.user.chatfirebase.model.ChatModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.item_chat.view.*
import java.io.ByteArrayOutputStream
import kotlin.properties.Delegates


class ChatItem private constructor(val name: String, val body: String?, val image: String?) {

    // 정적팩토리메소드
    // ChatItem 을 생성할 때 chat/image 둘 중 하나의 function 을 불러서 생성한다.
    companion object {
        fun chat(name: String, body: String): ChatItem {
            return ChatItem(name, body, null)
        }

        fun image(name: String, image: String): ChatItem {
            return ChatItem(name, null, image)
        }
    }

    fun toJson(): Map<String, Any> {
        val json = mutableMapOf<String, Any>(
                "name" to name
        )
        // image 를 받든 body 를 받든 공통적으로 name 은 들어간다.
        body?.let {
            json.put("body", body)
            // it을 안써도 이름으로 쓸 수 있게 했다.
        }

        image?.let {
            json.put("image", image)
        }

        return json
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

            item.body?.let {
                bodyTextView.text = item.body
                bodyTextView.visibility = VISIBLE
                imageView.visibility = GONE
                // invisible 로 하면 레이아웃 영역을 차지하고 내용만 안나오는것.
                // gone 으로 하면 아예 레이아웃이 없어진걸로 인지한다.
            }

            item.image?.let {
                GlideApp.with(this).load(it).centerCrop().into(imageView)
                imageView.visibility = VISIBLE
                bodyTextView.visibility = GONE
            }
        }

    } // 뷰 재사용
}


//controller
class ChatActivity : AppCompatActivity() {
    private val chatModel = ChatModel()
    private lateinit var adapter: ChatAdapter // recycler view adapter

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1000
    }

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
                recyclerView.smoothScrollToPosition(recyclerView.adapter.itemCount)
//            recyclerView.betterSmoothScrollToPosition(it.size - 1)

        }



        cameraButton.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null)
            // 카메라가 없는 경우를 대비해서.
            {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }

        sendButton.setOnClickListener {
            //            chatModel.postChat(ChatItem(name = "yun", body = "hello"))
            FirebaseAuth.getInstance().currentUser?.let {
                val name = it.displayName
                val body = editText.text

                if (body.isNotBlank()) {
                    chatModel.postChat(ChatItem.chat(name
                            ?: "unnamed", body.toString()))
//                    recyclerView.smoothScrollToPosition(recyclerView.adapter.itemCount)
                    editText.text.clear()

                }
            }
        }
    }

    // 사진을 찍은 뒤에 처리하는 코드.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
//            val extras = data.getExtras()
//            val imageBitmap = extras.get("data") as Bitmap
            // 두 번의 프로그램 종료의 위험이 있다. Bitmap 이 아닐경우.get 했을떄

            //as로 타입캐스팅시 캐스팅이 불가하면 Exception을 발생시킨다.
            //as?로 타입캐스팅을 하면 캐스팅이 불가할 경우 Exception을 발생시키는 대신
            //변수에 null 값을 넣어준다.
            data?.extras?.let { extras ->
                (extras.get("data") as? Bitmap)?.let {
                    bitmap ->
                    val bos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                    // 이미지를 jpeg 형태로 compress
                    // 이미지를 bos에 저장한다.
                    chatModel.postImage(bos.toByteArray()) { imageUri ->
                        FirebaseAuth.getInstance().currentUser?.let {
                            it.displayName?.let { displayName ->
                                chatModel.postChat(ChatItem.image(displayName, imageUri))
                            }
                        }
                    }
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


@GlideModule
class AppGlideModule : AppGlideModule()

// smoothScrollToPosition 은 가장 아래 position 으로 가기위해서 위의 리스트를 모두 스크롤 해야 하지만,
// betterSmoothScrollToPosition 은 maxScroll 까지의 개수만 스크롤하고 바로 최근의 리스트로 위치를 이동한다.


// _ (파라미터) :쓰지는 않지만 경고를 막기위한것.