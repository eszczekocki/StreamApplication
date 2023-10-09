package app.ui.schedule

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.utils.CoroutinesExtendedFunctions.launchPeriodicAsync
import app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import domain.model.Schedule
import domain.repository.SchedulesRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(var repository: SchedulesRepository): ViewModel() {
    var sec_30: Long = 30000
    var repositoryResultLiveData: MutableLiveData<Resource<List<Schedule>>> = MutableLiveData()
    lateinit var adapter: ScheduleAdapter

    init {
        repositoryResultLiveData.postValue(Resource.loading())
    }

    var job = viewModelScope.launchPeriodicAsync(sec_30) {
        refreshSchedule()
    }

    fun refreshSchedule() {
        viewModelScope.launch {
            try {
                repository.getAll()?.let {
                    repositoryResultLiveData.postValue(Resource.success(it.sortedBy { x ->
                        x.date
                    }.filter { x ->
                        LocalDate.now().plusDays(1)
                            .compareTo(x.date.toLocalDate()) == 0
                    }))
                }
            } catch (e: Exception) {
                repositoryResultLiveData.postValue(Resource.error(message = e.message))
            }
        }
    }
}