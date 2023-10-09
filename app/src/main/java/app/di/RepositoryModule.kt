package app.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import data.repository.EventsRepositoryImpl
import data.repository.SchedulesRepositoryImpl
import domain.repository.EventsRepository
import domain.repository.SchedulesRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun eventRepository(eventsRepositoryImpl: EventsRepositoryImpl): EventsRepository

    @Binds
    abstract fun scheduleRepository(scheduleRepositoryImpl: SchedulesRepositoryImpl): SchedulesRepository

}