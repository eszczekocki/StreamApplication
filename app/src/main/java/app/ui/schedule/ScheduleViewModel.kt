package app.ui.schedule

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequest
import app.ui.utils.CoroutinesExtendedFunctions.launchPeriodicAsync
import dagger.hilt.android.lifecycle.HiltViewModel
import data.repository.SchedulesRepository
import domain.model.Schedule
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(var repository: SchedulesRepository): ViewModel() {
    var scheduleLiveData: MutableLiveData<List<Schedule>> = MutableLiveData()
    var job = viewModelScope.launchPeriodicAsync(30000){
        refreshSchedule()
    }

    fun refreshSchedule() {
        viewModelScope.launch {
            repository.getAll()?.let {
                scheduleLiveData.postValue(it.sortedByDescending { x->x.date })
                Log.d("srefreshSchedule","schedule refreshed")
            }
        }
    }

    lateinit var  adapter : ScheduleAdapter
}