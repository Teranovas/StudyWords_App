import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studywords.word.WordItem

class WordAdapter(
    private val items: List<WordItem>,
    private val onSpeakClick: (String) -> Unit // 발음 버튼 콜백
) : RecyclerView.Adapter<WordAdapter.WordViewHolder>() {

    inner class WordViewHolder(val binding: ItemWordBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val binding = ItemWordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            if (item.isUser) {
                userBubble.text = item.word
                userBubble.visibility = View.VISIBLE
                gptBubble.visibility = View.GONE
                btnSpeak.visibility = View.GONE
            } else {
                gptBubble.text = item.word
                gptBubble.visibility = View.VISIBLE
                userBubble.visibility = View.GONE
                btnSpeak.visibility = View.VISIBLE

                btnSpeak.setOnClickListener {
                    onSpeakClick(item.word)
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size
}
