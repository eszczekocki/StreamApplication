package domain.repository

import domain.model.Schedule

interface SchedulesRepository {
    suspend fun getAll(): ArrayList<Schedule>?
}