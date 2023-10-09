package app.ui.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        viewModel.adapter = ScheduleAdapter(R.layout.event_item,viewModel.scheduleLiveData.value ?: emptyList())

        recyclerView?.let {
            it.layoutManager = LinearLayoutManager(activity)
            it.adapter = viewModel.adapter
        }
        recyclerView.addItemDecoration(
            DividerItemDecoration(recyclerView.context,
                RecyclerView.VERTICAL)
        )

        viewModel.scheduleLiveData.observe(viewLifecycleOwner, Observer {
            viewModel.adapter.updateData(it)
        })

    }

}