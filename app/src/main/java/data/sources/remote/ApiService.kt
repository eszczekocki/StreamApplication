package data.sources.remote

import domain.model.Event
import domain.model.Schedule
import retrofit2.Response
import retrofit2.http.GET

interface ApiService
{
@GET("getEvents")
suspend fun getEvents() : Response<ArrayList<Event>>

@GET("getSchedules")
suspend fun getSchedules() : Response<ArrayList<Schedule>>
}