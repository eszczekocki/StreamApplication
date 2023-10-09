package app.ui.events

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.streamingapp.BR
import com.example.streamingapp.R
import com.example.streamingapp.databinding.EventItemBinding
import domain.model.Event

class EventsAdapter(
    @LayoutRes var layout:Int,
    list: List<Event>,
    private var listener:OnItemClickListener
) : RecyclerView.Adapter<EventsAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun onItemClick(photoURIAddress: String)
    }
    var eventList:List<Event> = list

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = eventList[position]
        item.imageUrl?.let{ uri -> Glide.with(holder.imageEvent)
                    .load(uri)
                    .into(holder.imageEvent)
        }
        holder.titleTextView.text = item.title
        holder.subtitleTextView.text = item.subtitle
        holder.dateTextView.text = item.getTime()
        holder.itemView.setOnClickListener {
            listener.onItemClick(item.videoUrl)
        }
    }

    fun updateData(newData:List<Event>) {
        eventList = newData
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = eventList.size

    override fun getItemId(position: Int): Long = eventList.get(position).id

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var titleTextView = itemView.findViewById<TextView>(R.id.titleTextView)
        var subtitleTextView = itemView.findViewById<TextView>(R.id.subtitleTextView)
        var dateTextView = itemView.findViewById<TextView>(R.id.dateTextView)
        var imageEvent = itemView.findViewById<ImageView>(R.id.eventImage)
    }
}
