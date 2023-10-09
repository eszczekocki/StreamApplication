package app.ui.events

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.utils.date.DateFromNow
import app.utils.date.LocalDateTimeExtendedFunctions.parseWithTodayAndYesterdayNaming
import com.bumptech.glide.Glide
import com.example.streamingapp.R
import domain.model.Event

class EventsAdapter(
    private var listener:OnItemClickListener
) : RecyclerView.Adapter<EventsAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(photoURIAddress: String)
    }

    private lateinit var view: View

    init {
        setHasStableIds(true)
    }

    private val diffUtil = object : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.imageUrl == newItem.imageUrl &&
                    oldItem.title == newItem.title &&
                    oldItem.subtitle == newItem.subtitle &&
                    oldItem.date == newItem.date &&
                    oldItem.videoUrl == newItem.videoUrl
        }

    }
    val listDiffer = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        view = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = listDiffer.currentList[position]
        item.imageUrl.let { uri ->
            Glide.with(holder.imageEvent)
                .load(uri)
                .into(holder.imageEvent)
        }
        holder.titleTextView.text = item.title
        holder.subtitleTextView.text = item.subtitle

        var date = item.date.parseWithTodayAndYesterdayNaming()
        var dateName = when (date?.dateFromNow) {
            DateFromNow.TODAY -> view.context?.getString(R.string.today)
            DateFromNow.TOMORROW -> view.context?.getString(R.string.tomorrow)
            DateFromNow.YESTERDAY -> view.context?.getString(R.string.yesterday)
            else -> date?.date
        }

        holder.dateTextView.text = "${dateName} ${date?.time}"
        holder.itemView.setOnClickListener {
            listener.onItemClick(item.videoUrl)
        }
    }

    fun updateData(newData: List<Event>) = listDiffer.submitList(newData)

    override fun getItemCount(): Int {
        return listDiffer.currentList.size
    }

    override fun getItemId(position: Int): Long = listDiffer.currentList.get(position).id

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTextView = itemView.findViewById<TextView>(R.id.titleTextView)
        var subtitleTextView = itemView.findViewById<TextView>(R.id.subtitleTextView)
        var dateTextView = itemView.findViewById<TextView>(R.id.dateTextView)
        var imageEvent = itemView.findViewById<ImageView>(R.id.eventImage)
    }
}


