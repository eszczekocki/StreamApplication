package app.ui.events

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.ui.addons.videodialog.VideoDialog
import com.example.streamingapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventsFragment : Fragment(), EventsAdapter.OnItemClickListener {

    companion object {
        fun newInstance() = EventsFragment()
    }

    private val viewModel: EventsViewModel by viewModels()

    private lateinit var recyclerView:RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView = view?.findViewById<RecyclerView>(R.id.eventRecyclerView)!!
        viewModel.adapter = EventsAdapter(R.layout.event_item,viewModel.eventsLiveData.value ?: emptyList()
        , this)

        recyclerView?.let {
            it.layoutManager = LinearLayoutManager(activity)
            it.adapter = viewModel.adapter
        }
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context,RecyclerView.VERTICAL))

        viewModel.eventsLiveData.observe(viewLifecycleOwner, Observer {
            viewModel.adapter?.updateData(it)
        })

        var swipeLayout = view?.findViewById<SwipeRefreshLayout>(R.id.eventSwipeRefreshLayout)
        swipeLayout?.setOnRefreshListener {
            viewModel.refreshEvents()
            swipeLayout?.isRefreshing = false
        }
        viewModel.refreshEvents()
    }

    override fun onItemClick(uri: String) {
    var dialog = VideoDialog()
        dialog.arguments = bundleOf("uri" to uri)
        fragmentManager?.let { dialog.show(it,"") }
    }
}