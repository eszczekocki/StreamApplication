package data.repository

import data.sources.remote.RemoteDataSource
import domain.model.Event
import domain.repository.EventsRepository
import javax.inject.Inject

class EventsRepositoryImpl @Inject constructor(var remoteDataSource: RemoteDataSource) :
    EventsRepository {

    override suspend fun getAll(): ArrayList<Event>? {
        return remoteDataSource.fetchEvents()
    }
}