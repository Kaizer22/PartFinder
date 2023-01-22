package ru.desh.partfinder.core.domain.repository

import android.os.Build
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.desh.partfinder.core.data.api.business_news.*
import ru.desh.partfinder.core.data.api.business_news.model.mapper.ApiArticleMapper
import ru.desh.partfinder.core.data.api.business_news.model.mapper.ApiPaginatedNewsMapper
import java.io.IOException
import java.io.InputStream

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [Build.VERSION_CODES.P])
class BusinessNewsRepositoryTest {

    private lateinit var businessNewsRepository: BusinessNewsRepositoryImpl

    // MockWebServer.kt:361
    private val host = "localhost"
    private val port = 8080

    private val pageSize = 3
    private val page = 1

    private lateinit var mockWebServer: MockWebServer
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var newsApi: NewsApi


    @Before
    fun before() {
        mockWebServer = MockWebServer()
        mockWebServer.start(port)

        okHttpClient = OkHttpClient.Builder().build()

        newsApi = Retrofit.Builder()
            .baseUrl("http://$host:$port")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build().create(NewsApi::class.java)
        businessNewsRepository = BusinessNewsRepositoryImpl(
            newsApi, ApiPaginatedNewsMapper(ApiArticleMapper())
        )
    }

    @Test
    fun testGetLatestBusinessNewsSuccess() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(getJson("news_articles.json"))
        )

        runBlocking {
            val res = businessNewsRepository.getLatestBusinessNews(pageSize, page)
            Assertions.assertEquals(false, res.isException)
        }
    }

    @Test
    fun testGetLatestBusinessNewsFail() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setBody(getJson("news_api_error.json"))
        )

        runBlocking {
            val res = businessNewsRepository.getLatestBusinessNews(pageSize, page)
            Assertions.assertEquals(true, res.isException)
        }
    }

    @After
    fun after() {
        mockWebServer.shutdown()
    }

    fun getJson(path: String): String {
        return try {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            val jsonStream: InputStream = context.assets.open(path)
            String(jsonStream.readBytes())
        } catch (exception: IOException) {
            throw exception
        }
    }
}