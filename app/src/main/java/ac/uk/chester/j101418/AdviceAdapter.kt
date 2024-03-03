package ac.uk.chester.j101418

import ac.uk.chester.j101418.databinding.ItemAdviceBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AdviceAdapter (private var advices:  List<AdviceData>) : RecyclerView.Adapter<AdviceAdapter.AdviceViewHolder>()
{
    inner class AdviceViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemAdviceBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdviceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_advice, parent, false)
        return AdviceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return advices.size
    }

    override fun onBindViewHolder(holder: AdviceViewHolder, position: Int) {
        holder.binding.tvAdviceItem.text = advices[position].advice
    }


}