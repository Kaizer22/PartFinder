package ru.desh.partfinder.core.data.api.interceptors

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions
import ru.desh.partfinder.core.data.api.business_news.ApiEndpoints
import ru.desh.partfinder.core.data.api.business_news.ApiParams
import ru.desh.partfinder.core.data.api.business_news.ApiUtils

class NewsApiAuthenticationInterceptorTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var newsApiAuthInterceptor: NewsApiAuthenticationInterceptor
    private lateinit var okHttpClient: OkHttpClient

    @Before
    fun before() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)

        newsApiAuthInterceptor = NewsApiAuthenticationInterceptor()
        okHttpClient = OkHttpClient().newBuilder().addInterceptor(newsApiAuthInterceptor).build()
    }

    @After
    fun after() {
        mockWebServer.shutdown()
    }

    @Test
    fun `NewsAPI request should have a valid  API key query param`() {
        mockWebServer.dispatcher = getNewsAPIDispatcher()
        okHttpClient.newCall(
            Request.Builder()
                .url(mockWebServer.url(ApiEndpoints.TOP_HEADLINES_ENDPOINT_V2)).build()
        ).execute()

        val request = mockWebServer.takeRequest()

        with(request) {
            Assertions.assertEquals(
                true, requestUrl?.queryParameterNames
                    ?.contains(ApiParams.API_KEY)
            )
            Assertions.assertEquals(
                ApiUtils.QHZV_DSL_NHB,
                requestUrl?.queryParameter(ApiParams.API_KEY)
            )
        }
    }

    private fun getNewsAPIDispatcher() = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when (request.path) {
                ApiEndpoints.TOP_HEADLINES_ENDPOINT_V2 -> {
                    MockResponse().setResponseCode(200)
                }
                else -> {
                    MockResponse().setResponseCode(404)
                }
            }
        }
    }
}