package data.sources.remote

import domain.model.Event
import domain.model.Schedule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService,
    private val ioDispatcher:CoroutineDispatcher
) {
    suspend fun fetchEvents(): ArrayList<Event>? =
        withContext(ioDispatcher) {
            apiService.getEvents().body()
        }

    suspend fun fetchSchedule(): ArrayList<Schedule>? =
        withContext(ioDispatcher) {
            apiService.getSchedule().body()
        }
}

