package app.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.ui.addons.videodialog.VideoDialog
import app.utils.Status
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
        viewModel.adapter = EventsAdapter(this)

        recyclerView.let {
            it.layoutManager = LinearLayoutManager(activity)
            it.adapter = viewModel.adapter
        }
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                RecyclerView.VERTICAL
            )
        )

        val swipeLayout = view?.findViewById<SwipeRefreshLayout>(R.id.eventSwipeRefreshLayout)
        swipeLayout?.setOnRefreshListener {
            viewModel.refreshEvents()
            swipeLayout.isRefreshing = false
        }

        viewModel.repositoryResultLiveData.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    swipeLayout?.isRefreshing = false
                    viewModel.adapter.updateData(it.data ?: emptyList())
                }
                Status.FAILURE -> {
                    swipeLayout?.isRefreshing = false
                    Toast.makeText(
                        context,
                        "${context?.getText(R.string.network_problem)} ${it.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
                Status.LOADING -> {
                    swipeLayout?.isRefreshing = true
                }
            }
        })
    }

    override fun onItemClick(uri: String) {
    var dialog = VideoDialog()
        dialog.arguments = bundleOf("uri" to uri)
        fragmentManager?.let { dialog.show(it,"") }
    }
}