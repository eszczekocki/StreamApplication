package data.repository

import data.sources.remote.RemoteDataSource
import domain.model.Schedule
import domain.repository.SchedulesRepository
import javax.inject.Inject

class SchedulesRepositoryImpl @Inject constructor(var remoteDataSource: RemoteDataSource) :
    SchedulesRepository {

    override suspend fun getAll(): ArrayList<Schedule>? {
        return remoteDataSource.fetchSchedule()
    }
}