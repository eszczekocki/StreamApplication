package app.ui.events

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import data.repository.EventsRepository
import domain.model.Event
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(var repository: EventsRepository): ViewModel() {
    var eventsLiveData:MutableLiveData<List<Event>> = MutableLiveData()

    fun refreshEvents() {
        viewModelScope.launch {
            repository.getAll()?.let {
                eventsLiveData.postValue(it.sortedBy { x->x.date })
            }
        }
    }

    lateinit var  adapter : EventsAdapter
}