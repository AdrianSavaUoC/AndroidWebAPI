package ac.uk.chester.j101418

import ac.uk.chester.j101418.databinding.ItemQuoteBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class QuoteAdapter (private var quotes: List<QuoteItems> ) : RecyclerView.Adapter<QuoteAdapter.QuoteViewHolder>()
{

    inner class QuoteViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemQuoteBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_quote, parent, false)
        return QuoteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return quotes.size
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        holder.binding.tvQuote.text = quotes[position].content
    }

}