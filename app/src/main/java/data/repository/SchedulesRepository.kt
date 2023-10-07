package data.repository

import data.sources.remote.RemoteDataSource
import domain.model.Event
import domain.model.Schedule
import javax.inject.Inject

class SchedulesRepository @Inject constructor(var remoteDataSource: RemoteDataSource) {

    suspend fun getAll(): ArrayList<Schedule>? {
        return remoteDataSource.fetchSchedules()
    }
}