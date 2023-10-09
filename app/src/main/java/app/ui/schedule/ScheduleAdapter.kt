package app.ui.schedule

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
import com.example.streamingapp.databinding.ScheduleItemBinding
import domain.model.Event
import domain.model.Schedule

class ScheduleAdapter(
    @LayoutRes var layout:Int,
    list: List<Schedule>,
) : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

    var scheduleList = list

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
        var item = scheduleList[position]
        item.imageUrl?.let{ uri -> Glide.with(holder.imageEvent)
            .load(uri)
            .into(holder.imageEvent)
        }
        holder.titleTextView.text = item.title
        holder.subtitleTextView.text = item.subtitle
        holder.dateTextView.text = item.getTime()

    }

    fun updateData(newData:List<Schedule>) {
        scheduleList = newData
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = scheduleList.size

    override fun getItemId(position: Int): Long = scheduleList.get(position).id

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var titleTextView = itemView.findViewById<TextView>(R.id.titleTextView)
        var subtitleTextView = itemView.findViewById<TextView>(R.id.subtitleTextView)
        var dateTextView = itemView.findViewById<TextView>(R.id.dateTextView)
        var imageEvent = itemView.findViewById<ImageView>(R.id.eventImage)
    }
}
