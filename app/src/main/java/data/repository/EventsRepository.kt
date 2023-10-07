package data.repository

import data.sources.remote.RemoteDataSource
import domain.model.Event
import javax.inject.Inject

class EventsRepository @Inject constructor(var remoteDataSource: RemoteDataSource) {

    suspend fun getAll(): ArrayList<Event>? {
        return remoteDataSource.fetchEvents()
    }
}