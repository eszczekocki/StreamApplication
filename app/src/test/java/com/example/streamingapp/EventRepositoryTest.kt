import data.repository.EventsRepositoryImpl
import data.sources.remote.RemoteDataSource
import domain.model.Event
import junit.framework.Assert
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class EventRepositoryTest {

    @Mock
    private lateinit var mockRemoteDataSource: RemoteDataSource

    private lateinit var eventsRepository: EventsRepositoryImpl

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        eventsRepository = EventsRepositoryImpl(mockRemoteDataSource)
    }

    @Test
    fun `test getAll with empty list returns a list of events`() = testScope.runBlockingTest {
        val expectedEvents = ArrayList<Event>()
        Mockito.`when`(mockRemoteDataSource.fetchEvents()).thenReturn(expectedEvents)
        val result = eventsRepository.getAll()

        Assert.assertNotNull(result)
        Assert.assertEquals(expectedEvents, result)
    }

    @Test(expected = RuntimeException::class)
    fun `test getAll with network error returns null when remote data source fails`() =
        testScope.runBlockingTest {
            Mockito.`when`(mockRemoteDataSource.fetchEvents())
                .thenThrow(RuntimeException("Network error"))
            val result = eventsRepository.getAll()
        }
}
