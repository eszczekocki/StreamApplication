package app.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.utils.Status
import com.example.streamingapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleFragment : Fragment() {

    companion object {
        fun newInstance() = ScheduleFragment()
    }

    private val viewModel: ScheduleViewModel by viewModels()
    private lateinit var recyclerView:RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.job.cancel()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView = view?.findViewById<RecyclerView>(R.id.scheduleRecyclerView)!!
        viewModel.adapter = ScheduleAdapter()

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
        recyclerView.itemAnimator?.changeDuration = 0
        var swipeLayout = view?.findViewById<SwipeRefreshLayout>(R.id.scheduleSwipeRefreshLayout)
        swipeLayout?.setOnRefreshListener {
            viewModel.refreshSchedule()
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
}