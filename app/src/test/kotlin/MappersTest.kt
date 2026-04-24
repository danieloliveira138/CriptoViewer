import com.danieloliveira138.criptoviewer.data.remote.dto.CurrencyDTO
import com.danieloliveira138.criptoviewer.data.remote.dto.ExchangeAssetDTO
import com.danieloliveira138.criptoviewer.data.remote.dto.ExchangeAssetResponseDTO
import com.danieloliveira138.criptoviewer.data.remote.dto.ExchangeDTO
import com.danieloliveira138.criptoviewer.data.remote.dto.ExchangeInfoDTO
import com.danieloliveira138.criptoviewer.data.remote.dto.ExchangeStatusDTO
import com.danieloliveira138.criptoviewer.data.remote.dto.ExchangeUrlsDTO
import com.danieloliveira138.criptoviewer.data.remote.mapper.ExchangeAssetsMapper
import com.danieloliveira138.criptoviewer.data.remote.mapper.ExchangeInfoMapper
import com.danieloliveira138.criptoviewer.data.remote.mapper.ExchangeItemMapper
import com.danieloliveira138.criptoviewer.data.remote.mapper.ExchangeUrlMapper
import org.junit.Assert
import org.junit.Test

class MappersTest {

    private val urlMapper = ExchangeUrlMapper()
    private val infoMapper = ExchangeInfoMapper(urlMapper)
    private val itemMapper = ExchangeItemMapper()
    private val assetsMapper = ExchangeAssetsMapper()

    @Test
    fun `ExchangeUrlMapper maps DTO to domain correctly`() {
        val dto = ExchangeUrlsDTO(
            website = listOf("https://binance.com"),
            twitter = listOf("@binance"),
            facebook = listOf("binancefb")
        )
        val result = urlMapper.map(dto)

        Assert.assertEquals(dto.website, result.website)
        Assert.assertEquals(dto.twitter, result.twitter)
        Assert.assertEquals(dto.facebook, result.facebook)
    }

    @Test
    fun `ExchangeItemMapper maps DTO to domain correctly`() {
        val dtoActive = ExchangeDTO(
            id = 1,
            name = "Binance",
            slug = "binance",
            isActive = 1,
            isListed = 1,
            isRedistributable = 1,
            firstHistoricalData = null,
            lastHistoricalData = null
        )
        val dtoInactive = dtoActive.copy(id = 2, name = "Kraken", isActive = 0)

        val resultActive = itemMapper.map(dtoActive)
        val resultInactive = itemMapper.map(dtoInactive)

        Assert.assertEquals(1, resultActive.id)
        Assert.assertEquals("Binance", resultActive.name)
        Assert.assertTrue(resultActive.isActive)

        Assert.assertEquals(2, resultInactive.id)
        Assert.assertEquals("Kraken", resultInactive.name)
        Assert.assertTrue(!resultInactive.isActive)
    }

    @Test
    fun `ExchangeInfoMapper maps DTO to domain correctly`() {
        val urlDto = ExchangeUrlsDTO(website = listOf("site"))
        val dto = ExchangeInfoDTO(
            id = 1,
            name = "Binance",
            slug = "binance",
            logo = "logo_url",
            description = "desc",
            dateLaunched = "2017",
            notice = null,
            countries = null,
            fiats = null,
            type = null,
            makerFee = 0.1,
            takerFee = 0.2,
            weeklyVisits = null,
            spotVolumeUsd = null,
            spotVolumeLastUpdated = null,
            urls = urlDto
        )

        val result = infoMapper.map(dto)

        Assert.assertEquals(1, result.id)
        Assert.assertEquals("Binance", result.name)
        Assert.assertEquals("logo_url", result.logo)
        Assert.assertEquals(0.1, result.makerFee!!, 0.0)
        Assert.assertEquals(0.2, result.takerFee!!, 0.0)
        Assert.assertEquals(listOf("site"), result.urls?.website)
    }

    @Test
    fun `ExchangeInfoMapper handles null urls`() {
        val dto = ExchangeInfoDTO(
            id = 1, name = null, slug = null, logo = null, description = null,
            dateLaunched = null, notice = null, countries = null, fiats = null,
            type = null, makerFee = null, takerFee = null, weeklyVisits = null,
            spotVolumeUsd = null, spotVolumeLastUpdated = null, urls = null
        )

        val result = infoMapper.map(dto)
        Assert.assertNull(result.urls)
    }

    @Test
    fun `ExchangeAssetsMapper maps DTO to domain correctly`() {
        val currencyDto = CurrencyDTO(name = "Bitcoin", symbol = "BTC", priceUsd = 60000.0)
        val assetDto = ExchangeAssetDTO(currency = currencyDto)
        val responseDto = ExchangeAssetResponseDTO(data = listOf(assetDto), status = mockStatus())

        val result = assetsMapper.map(responseDto)

        Assert.assertEquals(1, result.data?.size)
        val asset = result.data!![0]
        Assert.assertEquals("Bitcoin", asset.currencyName)
        Assert.assertEquals("BTC", asset.currencySymbol)
        Assert.assertEquals(60000.0, asset.currencyPrice, 0.0)
    }

    @Test
    fun `ExchangeAssetsMapper handles null data and missing currency fields`() {
        val emptyResponse = ExchangeAssetResponseDTO(data = null, status = mockStatus())
        val resultEmpty = assetsMapper.map(emptyResponse)
        Assert.assertNull(resultEmpty.data)

        val incompleteAsset = ExchangeAssetDTO(currency = null)
        val incompleteResponse =
            ExchangeAssetResponseDTO(data = listOf(incompleteAsset), status = mockStatus())
        val resultIncomplete = assetsMapper.map(incompleteResponse)

        Assert.assertEquals(1, resultIncomplete.data?.size)
        val asset = resultIncomplete.data!![0]
        Assert.assertEquals("", asset.currencyName)
        Assert.assertEquals("", asset.currencySymbol)
        Assert.assertEquals(0.0, asset.currencyPrice, 0.0)
    }

    private fun mockStatus() = ExchangeStatusDTO(
        timestamp = "",
        errorCode = 0,
        errorMessage = null,
        elapsed = 0,
        creditCount = 0,
        notice = null
    )
}