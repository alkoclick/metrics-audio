import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request


class PrometheusRestService(private val prometheusConfig: PrometheusConfig) {
    private val client = OkHttpClient()
    private val mapper = ObjectMapper().apply { registerModule(KotlinModule()) }

    fun query(query: String?): VectorResponse {
        val request = Request.Builder().url(createUrl("query", mapOf("query" to query))).get().build()

        return mapper.readValue(client.newCall(request).execute().body?.string(), VectorResponse::class.java)
    }

    private fun createUrl(path: String, queryParams: Map<String, String?>) =
        HttpUrl.Builder()
            .scheme(prometheusConfig.scheme).host(prometheusConfig.url).port(prometheusConfig.port)
            .addPathSegments("api/v1")
            .addPathSegment(path)
            .apply {
                queryParams.filterValues { it != null }.forEach { addQueryParameter(it.key, it.value) }
            }
            .build()
}

/**
 * Based on work from Dat Truong: https://github.com/anhdat/prometheus4j
 */
data class VectorResponse(
    var status: String,
    var data: VectorData,
) {

    data class VectorData(
        val resultType: String,
        val result: List<VectorResult>,
    )

    data class VectorResult(
        val metric: Map<String, String>,
        val value: List<Float>,
    )
}