import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studywords.R
import com.example.studywords.sentence.ChatAdapter
import com.example.studywords.word.WordItem
class WordAdapter(
    private val items: List<WordItem>,
    private val onSpeakClick: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_USER = 0
    private val VIEW_TYPE_GPT = 1

    override fun getItemViewType(position: Int): Int {
        return if (items[position].isUser) VIEW_TYPE_USER else VIEW_TYPE_GPT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_USER) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_item_user, parent, false)
            UserViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_item_bot, parent, false)
            GptViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        if (holder is UserViewHolder) {
            holder.message.text = item.word
        } else if (holder is GptViewHolder) {
            holder.message.text = item.word
            holder.btnSpeak.setOnClickListener { onSpeakClick(item.word) }
        }
    }

    override fun getItemCount(): Int = items.size

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val message: TextView = view.findViewById(R.id.messageText)
    }

    inner class GptViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val message: TextView = view.findViewById(R.id.messageText)
        val btnSpeak: ImageView = view.findViewById(R.id.btnSpeak)
    }
}


