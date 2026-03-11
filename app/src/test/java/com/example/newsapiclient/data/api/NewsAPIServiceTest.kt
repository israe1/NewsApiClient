package com.example.newsapiclient.data.api

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(RobolectricTestRunner::class)
class NewsAPIServiceTest {
    private lateinit var service: NewsAPIService
    private lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
        service = Retrofit.Builder()
            .baseUrl(server.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsAPIService::class.java)
    }

    private fun enqueueMockResponse(fileName: String) {
        val inputStream = javaClass.classLoader!!.getResourceAsStream(fileName)
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse(body = source.readString(Charsets.UTF_8))
        server.enqueue(mockResponse)
    }

    @After
    fun tearDown() {
        server.close()
    }

    @Test
    fun `getTopHeadlines sentRequest should receive expected result`() = runBlocking {
        enqueueMockResponse("newsresponse.json")
        val responseBody = service.getTopHeadlines("us", 1).body()
        val request = server.takeRequest()
        assertThat(responseBody).isNotNull()
        assertThat(request.url.encodedPath+"?"+request.url.query).isEqualTo("/v2/top-headlines?country=us&page=1&apiKey=657e87d494b34e0d8689a08c09a3dff3")
    }

    @Test
    fun `getTopHeadlines receivedResponse should have correct pageSize`() = runBlocking{
        enqueueMockResponse("newsresponse.json")
        val responseBody = service.getTopHeadlines("us", 1).body()
        val articlesList = responseBody!!.articles
        assertThat(articlesList.size).isEqualTo(17)
    }

    @Test
    fun `getTopHeadlines receivedResponse should have correct content`() = runBlocking{
        enqueueMockResponse("newsresponse.json")
        val responseBody = service.getTopHeadlines("us", 1).body()
        val article = responseBody!!.articles.first()
        assertThat(article.author).isEqualTo("Steve Kopack")
        assertThat(article.url).isEqualTo("https://www.nbcnews.com/business/markets/oil-price-iran-war-markets-rcna262697")
        assertThat(article.publishedAt).isEqualTo("2026-03-10T18:35:18Z")
    }
}