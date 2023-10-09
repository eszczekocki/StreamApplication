package domain.repository

import domain.model.Event

interface EventsRepository {
    suspend fun getAll(): ArrayList<Event>?
}