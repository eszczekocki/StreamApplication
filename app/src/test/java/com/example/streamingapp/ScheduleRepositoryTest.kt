import data.repository.SchedulesRepositoryImpl
import data.sources.remote.RemoteDataSource
import domain.model.Schedule
import junit.framework.Assert
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ScheduleRepositoryTest {

    @Mock
    private lateinit var mockRemoteDataSource: RemoteDataSource

    private lateinit var scheduleRepository: SchedulesRepositoryImpl

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        scheduleRepository = SchedulesRepositoryImpl(mockRemoteDataSource)
    }

    @Test
    fun `test getAll with empty scheduel list returns a list of schedule`() =
        testScope.runBlockingTest {
            val expectedSchedule = ArrayList<Schedule>()
            Mockito.`when`(mockRemoteDataSource.fetchSchedule()).thenReturn(expectedSchedule)
            val result = scheduleRepository.getAll()

            Assert.assertNotNull(result)
            Assert.assertEquals(expectedSchedule, result)
        }

    @Test(expected = RuntimeException::class)
    fun `test getAll with runtimeException returns null when remote data source fails`() =
        testScope.runBlockingTest {
            Mockito.`when`(mockRemoteDataSource.fetchSchedule())
                .thenThrow(RuntimeException("Network error"))
            val result = scheduleRepository.getAll()

        }
}
