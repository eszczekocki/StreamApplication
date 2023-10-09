package app.ui.events

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import domain.model.Event
import domain.repository.EventsRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(var repository: EventsRepository): ViewModel() {
    var repositoryResultLiveData: MutableLiveData<Resource<List<Event>>> = MutableLiveData()
    lateinit var adapter: EventsAdapter

    init {
        refreshEvents()
    }

    fun refreshEvents() {
        viewModelScope.launch {
            repositoryResultLiveData.postValue(Resource.loading())
            try {
                repository.getAll()?.let {
                    repositoryResultLiveData.postValue(
                        Resource.success(
                            it.sortedBy { x -> x.date })
                    )
                }
            } catch (e: Exception) {
                repositoryResultLiveData.postValue(Resource.error(message = e.message))
            }
        }
    }
}