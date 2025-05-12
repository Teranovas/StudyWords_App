package com.example.studywords.sentence

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studywords.R
import java.util.Locale

class SentenceActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var userInput: EditText
    private lateinit var sendButton: Button
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var btnBack: ImageView
    private lateinit var tts: TextToSpeech

    private val viewModel: SentenceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sentence)

        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        userInput = findViewById(R.id.userInput)
        sendButton = findViewById(R.id.sendButton)
        btnBack = findViewById(R.id.btnBack)

        tts = TextToSpeech(this) { status ->
            if (status != TextToSpeech.ERROR) {
                tts.language = Locale.US
            }
        }

        chatAdapter = ChatAdapter(mutableListOf()) { speakText(it) }
        chatRecyclerView.adapter = chatAdapter
        chatRecyclerView.layoutManager = LinearLayoutManager(this)

        btnBack.setOnClickListener {
            finish()
        }

        sendButton.setOnClickListener {
            val input = userInput.text.toString().trim()
            if (input.isNotEmpty()) {
                viewModel.sendUserMessage(input)
                userInput.setText("")
            }
        }

        userInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendButton.performClick()
                true
            } else {
                false
            }
        }

        viewModel.messages.observe(this, Observer { updatedMessages ->
            chatAdapter.submitList(updatedMessages)
            chatRecyclerView.scrollToPosition(updatedMessages.size - 1)
        })
    }

    private fun speakText(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onDestroy() {
        tts.shutdown()
        super.onDestroy()
    }
}