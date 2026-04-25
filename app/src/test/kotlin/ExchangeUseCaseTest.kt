import com.danieloliveira138.criptoviewer.core.network.Result
import com.danieloliveira138.criptoviewer.core.exceptions.EmptyResponseException
import com.danieloliveira138.criptoviewer.core.exceptions.ExchangeIdIsNull
import com.danieloliveira138.criptoviewer.domain.model.ExchangeAsset
import com.danieloliveira138.criptoviewer.domain.model.ExchangeAssets
import com.danieloliveira138.criptoviewer.domain.model.ExchangeInfo
import com.danieloliveira138.criptoviewer.domain.model.ExchangeItem
import com.danieloliveira138.criptoviewer.domain.repository.ExchangeRepository
import com.danieloliveira138.criptoviewer.domain.usecase.ExchangeUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class ExchangeUseCaseTest {

    private val repository: ExchangeRepository = mock()
    private val useCase = ExchangeUseCase(repository)

    @Test
    fun `getExchangesList success`() = runTest {
        // Given
        val exchanges = listOf(
            ExchangeItem(1, "Binance", true),
            ExchangeItem(2, "Kraken", true)
        )
        whenever(repository.getExchangesList(any(), any())).thenReturn(exchanges)

        // When
        val result = useCase.getExchangesList(start = 1, limit = 20)

        // Then
        assertTrue("Expected Result.Success but got $result", result is Result.Success)
        assertEquals(exchanges, (result as Result.Success).data)
    }

    @Test
    fun `getExchangesList empty result`() = runTest {
        // Given — repository returns an empty list for any pagination params
        whenever(repository.getExchangesList(any(), any())).thenReturn(emptyList())

        // When
        val result = useCase.getExchangesList(start = 1, limit = 20)

        // Then — use case wraps the empty list in Result.Success, not Result.Error
        assertTrue("Expected Result.Success but got $result", result is Result.Success)
        assertEquals(emptyList<Any>(), (result as Result.Success).data)
    }

    @Test
    fun `getExchangesList repository exception`() = runTest {
        // Given
        val exception = Exception("Repository error")
        whenever(repository.getExchangesList(any(), any())).thenAnswer { throw exception }

        // When
        val result = useCase.getExchangesList(start = 1, limit = 20)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)
    }

    @Test
    fun `getExchangesList boundary pagination`() = runTest {
        // Given
        val exchanges = emptyList<ExchangeItem>()
        whenever(repository.getExchangesList(0, -1)).thenReturn(exchanges)

        // When
        val result = useCase.getExchangesList(start = 0, limit = -1)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(exchanges, (result as Result.Success).data)
    }

    @Test
    fun `getExchangeInfo success`() = runTest {
        // Given
        val exchangeInfo = ExchangeInfo(
            id = 1,
            name = "Binance",
            slug = "binance",
            logo = "logo_url",
            description = "description",
            dateLaunched = "2017-07-01",
            urls = null,
            makerFee = 0.1,
            takerFee = 0.1
        )
        whenever(repository.getExchangeInfo(1)).thenReturn(exchangeInfo)

        // When
        val result = useCase.getExchangeInfo("1")

        // Then
        assertTrue(result is Result.Success)
        assertEquals(exchangeInfo, (result as Result.Success).data)
    }

    @Test
    fun `getExchangeInfo null id`() = runTest {
        // When
        val result = useCase.getExchangeInfo(null)

        // Then
        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).exception is ExchangeIdIsNull)
    }

    @Test
    fun `getExchangeInfo empty id`() = runTest {
        // When
        val result = useCase.getExchangeInfo("")

        // Then
        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).exception is ExchangeIdIsNull)
    }

    @Test
    fun `getExchangeInfo non numeric id`() = runTest {
        // When
        val result = useCase.getExchangeInfo("abc")

        // Then
        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).exception is NumberFormatException)
    }

    @Test
    fun `getExchangeInfo repository error`() = runTest {
        // Given
        val exception = Exception("404 Not Found")
        whenever(repository.getExchangeInfo(any())).thenAnswer { throw exception }

        // When
        val result = useCase.getExchangeInfo("1")

        // Then
        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)
    }

    @Test
    fun `getExchangeAssets success`() = runTest {
        // Given
        val assetsList = listOf(
            ExchangeAsset("Bitcoin", "BTC", 60000.0),
            ExchangeAsset("Ethereum", "ETH", 3000.0)
        )
        val exchangeAssets = ExchangeAssets(data = assetsList)
        whenever(repository.getExchangeAssets(1)).thenReturn(exchangeAssets)

        // When
        val result = useCase.getExchangeAssets("1")

        // Then
        assertTrue(result is Result.Success)
        assertEquals(assetsList, (result as Result.Success).data)
    }

    @Test
    fun `getExchangeAssets null id validation`() = runTest {
        // When
        val result = useCase.getExchangeAssets(null)

        // Then
        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).exception is ExchangeIdIsNull)
    }

    @Test
    fun `getExchangeAssets empty id validation`() = runTest {
        // When
        val result = useCase.getExchangeAssets("")

        // Then
        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).exception is ExchangeIdIsNull)
    }

    @Test
    fun `getExchangeAssets invalid id format`() = runTest {
        // When
        val result = useCase.getExchangeAssets("xyz")

        // Then
        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).exception is NumberFormatException)
    }

    @Test
    fun `getExchangeAssets null response data`() = runTest {
        // Given
        val exchangeAssets = ExchangeAssets(data = null)
        whenever(repository.getExchangeAssets(1)).thenReturn(exchangeAssets)

        // When
        val result = useCase.getExchangeAssets("1")

        // Then
        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).exception is EmptyResponseException)
    }

    @Test
    fun `getExchangeAssets empty response data list`() = runTest {
        // Given
        val exchangeAssets = ExchangeAssets(data = emptyList())
        whenever(repository.getExchangeAssets(1)).thenReturn(exchangeAssets)

        // When
        val result = useCase.getExchangeAssets("1")

        // Then
        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).exception is EmptyResponseException)
    }

    @Test
    fun `getExchangeAssets repository exception handling`() = runTest {
        // Given
        val exception = Exception("Network Error")
        whenever(repository.getExchangeAssets(any())).thenAnswer { throw exception }

        // When
        val result = useCase.getExchangeAssets("1")

        // Then
        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)
    }

}