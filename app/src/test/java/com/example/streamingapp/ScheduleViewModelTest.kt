package com.example.streamingapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.ui.schedule.ScheduleViewModel
import app.utils.Resource
import domain.model.Schedule
import domain.repository.SchedulesRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
class ScheduleViewModelTest {

    class MainDispatcherRule(
        private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    ) : TestWatcher() {
        override fun starting(description: Description) {
            Dispatchers.setMain(testDispatcher)
        }

        override fun finished(description: Description) {
            Dispatchers.resetMain()
        }
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val rule1 = MainDispatcherRule()

    @Mock
    private lateinit var mockRepository: SchedulesRepository

    @Mock
    private lateinit var mockObserver: Observer<Resource<List<Schedule>>>

    private lateinit var viewModel: ScheduleViewModel

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope =
        createTestCoroutineScope(TestCoroutineDispatcher() + CoroutineExceptionHandler { _, exception ->
            println("CoroutineExceptionHandler got $exception")
        } + testDispatcher)

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = ScheduleViewModel(mockRepository)
        viewModel.repositoryResultLiveData.observeForever(mockObserver)
    }

    @Test
    fun `test refreshSchedule with tomorrow data success`() = testScope.runBlockingTest {
        val schedules = arrayListOf(
            Schedule(
                LocalDateTime.now().plusDays(1),
                1,
                "http://image",
                "subtitle",
                "title"
            )
        )
        Mockito.`when`(mockRepository.getAll()).thenReturn(schedules)
        viewModel.refreshSchedule()

        Mockito.verify(mockObserver).onChanged(Resource.loading()) // Initial loading state
        Mockito.verify(mockObserver)
            .onChanged(Resource.success(schedules.sortedBy { it.date })) // Success state
    }

    @Test
    fun `test refreshSchedule with today data success`() = testScope.runBlockingTest {
        val schedules = arrayListOf(
            Schedule(
                LocalDateTime.now(),
                1,
                "http://image",
                "subtitle",
                "title"
            )
        )
        Mockito.`when`(mockRepository.getAll()).thenReturn(schedules)
        viewModel.refreshSchedule()

        Mockito.verify(mockObserver).onChanged(Resource.loading()) // Initial loading state
        Mockito.verify(mockObserver).onChanged(Resource.success(emptyList())) // Success state
    }


    @Test
    fun `test refreshSchedule with runtime Exception failure`() = testScope.runBlockingTest {
        val errorMessage = "Error message"
        Mockito.`when`(mockRepository.getAll()).thenThrow(RuntimeException(errorMessage))
        viewModel.refreshSchedule()

        Mockito.verify(mockObserver).onChanged(Resource.loading()) // Initial loading state
        Mockito.verify(mockObserver)
            .onChanged(Resource.error(message = errorMessage)) // Error state
    }
}