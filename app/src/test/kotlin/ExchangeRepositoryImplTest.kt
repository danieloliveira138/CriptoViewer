import com.danieloliveira138.criptoviewer.core.exceptions.EmptyResponseException
import com.danieloliveira138.criptoviewer.data.remote.api.ExchangeAPI
import com.danieloliveira138.criptoviewer.data.remote.dto.ExchangeAssetDTO
import com.danieloliveira138.criptoviewer.data.remote.dto.ExchangeAssetResponseDTO
import com.danieloliveira138.criptoviewer.data.remote.dto.ExchangeDTO
import com.danieloliveira138.criptoviewer.data.remote.dto.ExchangeInfoDTO
import com.danieloliveira138.criptoviewer.data.remote.dto.ExchangeInfoResponseDTO
import com.danieloliveira138.criptoviewer.data.remote.dto.ExchangeMapResponseDTO
import com.danieloliveira138.criptoviewer.data.remote.dto.ExchangeStatusDTO
import com.danieloliveira138.criptoviewer.data.remote.mapper.ExchangeAssetsMapper
import com.danieloliveira138.criptoviewer.data.remote.mapper.ExchangeInfoMapper
import com.danieloliveira138.criptoviewer.data.remote.mapper.ExchangeItemMapper
import com.danieloliveira138.criptoviewer.data.repository.ExchangeRepositoryImpl
import com.danieloliveira138.criptoviewer.domain.model.ExchangeAssets
import com.danieloliveira138.criptoviewer.domain.model.ExchangeInfo
import com.danieloliveira138.criptoviewer.domain.model.ExchangeItem
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.io.IOException

class ExchangeRepositoryImplTest {

    private val exchangeApi: ExchangeAPI = mock()
    private val exchangeItemMapper: ExchangeItemMapper = mock()
    private val exchangeInfoMapper: ExchangeInfoMapper = mock()
    private val exchangeAssetsMapper: ExchangeAssetsMapper = mock()

    private val repository = ExchangeRepositoryImpl(
        exchangeApi = exchangeApi,
        exchangeItemMapper = exchangeItemMapper,
        exchangeInfoMapper = exchangeInfoMapper,
        exchangeAssetsMapper = exchangeAssetsMapper,
    )

    @Test
    fun `getExchangesList success mapping`() = runTest {
        // Given — two DTOs returned by the API with different active states
        val dto1 = ExchangeDTO(
            id = 1, name = "Binance", slug = "binance",
            firstHistoricalData = null, lastHistoricalData = null,
            isActive = 1, isListed = 0, isRedistributable = 1,
        )
        val dto2 = ExchangeDTO(
            id = 2, name = "Kraken", slug = "kraken",
            firstHistoricalData = null, lastHistoricalData = null,
            isActive = 0, isListed = 1, isRedistributable = 0,
        )
        val expectedItem1 = ExchangeItem(id = 1, name = "Binance", isActive = true)
        val expectedItem2 = ExchangeItem(id = 2, name = "Kraken", isActive = false)

        whenever(exchangeApi.getExchanges(any(), any())).thenReturn(
            ExchangeMapResponseDTO(data = listOf(dto1, dto2), status = mock<ExchangeStatusDTO>())
        )
        whenever(exchangeItemMapper.map(dto1)).thenReturn(expectedItem1)
        whenever(exchangeItemMapper.map(dto2)).thenReturn(expectedItem2)

        // When
        val result = repository.getExchangesList(start = 1, limit = 20)

        // Then — mapped items are returned in order and mapper was called once per DTO
        assertEquals(listOf(expectedItem1, expectedItem2), result)
        verify(exchangeItemMapper).map(dto1)
        verify(exchangeItemMapper).map(dto2)
    }

    @Test
    fun `getExchangesList null data handling`() = runTest {
        // Given
        whenever(exchangeApi.getExchanges(any(), any())).thenReturn(
            ExchangeMapResponseDTO(data = null, status = mock())
        )
        // When
        val result = runCatching { repository.getExchangesList(1, 20) }
        // Then
        assertTrue(result.exceptionOrNull() is EmptyResponseException)
    }

    @Test
    fun `getExchangesList network error propagation`() = runTest {
        // Given
        whenever(exchangeApi.getExchanges(any(), any())).thenAnswer { throw IOException() }

        // When
        val result = runCatching { repository.getExchangesList(1, 20) }

        // Then
        assertTrue(result.exceptionOrNull() is IOException)
    }

    @Test
    fun `getExchangesList empty data list`() = runTest {
        // Given
        whenever(exchangeApi.getExchanges(any(), any())).thenReturn(
            ExchangeMapResponseDTO(data = emptyList(), status = mock())
        )

        // When
        val result = repository.getExchangesList(1, 20)

        // Then
        assertEquals(emptyList<ExchangeItem>(), result)
    }

    @Test
    fun `getExchangeInfo success mapping`() = runTest {
        // Given
        val exchangeId = 1
        val dto = mock<ExchangeInfoDTO>()
        val expectedInfo = mock<ExchangeInfo>()
        val response = ExchangeInfoResponseDTO(
            data = mapOf(exchangeId.toString() to dto),
            status = mock()
        )

        whenever(exchangeApi.getExchangeInfo(exchangeId)).thenReturn(response)
        whenever(exchangeInfoMapper.map(dto)).thenReturn(expectedInfo)

        // When
        val result = repository.getExchangeInfo(exchangeId)

        // Then
        assertEquals(expectedInfo, result)
        verify(exchangeInfoMapper).map(dto)
    }

    @Test
    fun `getExchangeInfo missing key handling`() = runTest {
        // Given
        val exchangeId = 1
        val response = ExchangeInfoResponseDTO(
            data = mapOf("2" to mock()),
            status = mock()
        )
        whenever(exchangeApi.getExchangeInfo(exchangeId)).thenReturn(response)

        // When
        val result = runCatching { repository.getExchangeInfo(exchangeId) }

        // Then
        assertTrue(result.exceptionOrNull() is EmptyResponseException)
    }

    @Test
    fun `getExchangeInfo null data map`() = runTest {
        // Given
        val exchangeId = 1
        val response = ExchangeInfoResponseDTO(data = null, status = mock())
        whenever(exchangeApi.getExchangeInfo(exchangeId)).thenReturn(response)

        // When
        val result = runCatching { repository.getExchangeInfo(exchangeId) }

        // Then
        assertTrue(result.exceptionOrNull() is EmptyResponseException)
    }

    @Test
    fun `getExchangeInfo network error propagation`() = runTest {
        // Given
        val exchangeId = 1
        whenever(exchangeApi.getExchangeInfo(exchangeId)).thenAnswer { throw IOException() }

        // When
        val result = runCatching { repository.getExchangeInfo(exchangeId) }

        // Then
        assertTrue(result.exceptionOrNull() is IOException)
    }

    @Test
    fun `getExchangeAssets success mapping`() = runTest {
        // Given
        val exchangeId = 1
        val dto = mock<ExchangeAssetDTO>()
        val expectedAssets = mock<ExchangeAssets>()
        val response = ExchangeAssetResponseDTO(
            data = listOf(dto),
            status = mock()
        )

        whenever(exchangeApi.getExchangeAssets(exchangeId)).thenReturn(response)
        whenever(exchangeAssetsMapper.map(response)).thenReturn(expectedAssets)

        // When
        val result = repository.getExchangeAssets(exchangeId)

        // Then
        assertEquals(expectedAssets, result)
        verify(exchangeAssetsMapper).map(response)
    }

    @Test
    fun `getExchangeAssets network error propagation`() = runTest {
        // Given
        val exchangeId = 1
        whenever(exchangeApi.getExchangeAssets(exchangeId)).thenAnswer { throw IOException() }

        // When
        val result = runCatching { repository.getExchangeAssets(exchangeId) }

        // Then
        assertTrue(result.exceptionOrNull() is IOException)
    }

    @Test
    fun `getExchangesList boundary parameters`() = runTest {
        // Given
        val start = 0
        val limit = -1
        whenever(exchangeApi.getExchanges(start, limit)).thenReturn(
            ExchangeMapResponseDTO(data = emptyList(), status = mock())
        )

        // When
        repository.getExchangesList(start, limit)

        // Then
        verify(exchangeApi).getExchanges(start, limit)
    }

    @Test
    fun `Mapper exception propagation`() = runTest {
        // Given
        val dto = ExchangeDTO(
            id = 1, name = "Binance", slug = "binance",
            firstHistoricalData = null, lastHistoricalData = null,
            isActive = 1, isListed = 0, isRedistributable = 1,
        )
        whenever(exchangeApi.getExchanges(any(), any())).thenReturn(
            ExchangeMapResponseDTO(data = listOf(dto), status = mock())
        )
        whenever(exchangeItemMapper.map(any())).thenThrow(RuntimeException("Mapper error"))

        // Then
        assertThrows(RuntimeException::class.java) {
            runTest { repository.getExchangesList(1, 20) }
        }
    }

}