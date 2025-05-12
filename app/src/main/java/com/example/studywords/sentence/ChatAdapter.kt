package com.example.studywords.sentence

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studywords.R

class ChatAdapter(
    private var messages: List<ChatMessage>,
    private val onSpeakClick: (String) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_USER = 0
        private const val TYPE_BOT = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isUser) TYPE_USER else TYPE_BOT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_USER) {
            val view = inflater.inflate(R.layout.chat_item_user, parent, false)
            UserViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.chat_item_bot, parent, false)
            BotViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is UserViewHolder) {
            holder.messageText.text = message.text
        } else if (holder is BotViewHolder) {
            holder.messageText.text = message.text
        }
    }

    override fun getItemCount(): Int = messages.size

    fun submitList(newList: List<ChatMessage>) {
        messages = newList
        notifyDataSetChanged()
    }

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageText: TextView = view.findViewById(R.id.messageText)
    }

    inner class BotViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageText: TextView = view.findViewById(R.id.messageText)
        val btnSpeak: ImageView = view.findViewById(R.id.btnSpeak)
    }
}