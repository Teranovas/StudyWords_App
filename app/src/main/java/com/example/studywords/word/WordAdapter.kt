import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studywords.R
import com.example.studywords.word.WordItem

class WordAdapter(
    private val items: List<WordItem>,
    private val onSpeakClick: (String) -> Unit
) : RecyclerView.Adapter<WordAdapter.WordViewHolder>() {

    inner class WordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userBubble: TextView = view.findViewById(R.id.userBubble)
        val gptBubble: TextView = view.findViewById(R.id.gptBubble)
        val btnSpeak: ImageView = view.findViewById(R.id.btnSpeak)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_word, parent, false)
        return WordViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val item = items[position]

        if (item.isUser) {
            holder.userBubble.text = item.word
            holder.userBubble.visibility = View.VISIBLE
            holder.gptBubble.visibility = View.GONE
            holder.btnSpeak.visibility = View.GONE
        } else {
            holder.gptBubble.text = item.word
            holder.gptBubble.visibility = View.VISIBLE
            holder.userBubble.visibility = View.GONE
            holder.btnSpeak.visibility = View.VISIBLE
            holder.btnSpeak.setOnClickListener { onSpeakClick(item.word) }
        }
    }

    override fun getItemCount(): Int = items.size
}
