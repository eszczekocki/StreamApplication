package com.example.streamingapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.ui.events.EventsViewModel
import app.utils.Resource
import domain.model.Event
import domain.repository.EventsRepository
import kotlinx.coroutines.Dispatchers
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

class EventsViewModelTest {


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
    private lateinit var mockRepository: EventsRepository

    @Mock
    private lateinit var mockObserver: Observer<Resource<List<Event>>>

    private lateinit var viewModel: EventsViewModel

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = EventsViewModel(mockRepository)
        viewModel.repositoryResultLiveData.observeForever(mockObserver)
    }

    @Test
    fun `test refreshEvents with one event success`() = testScope.runBlockingTest {
        val events = arrayListOf(
            Event(
                LocalDateTime.now(),
                1,
                "http://image",
                "subtitle",
                "title",
                "http://video"
            )
        )
        Mockito.`when`(mockRepository.getAll()).thenReturn(events)
        viewModel.refreshEvents()

        Mockito.verify(mockObserver).onChanged(Resource.success(events.sortedBy { it.date }))
    }

    @Test
    fun `test refreshEvents with runtimeexception failure`() = testScope.runBlockingTest {
        val errorMessage = "Error message"
        Mockito.`when`(mockRepository.getAll()).thenThrow(RuntimeException(errorMessage))
        viewModel.refreshEvents()

        Mockito.verify(mockObserver)
            .onChanged(Resource.error(message = errorMessage)) // Error state
    }
}