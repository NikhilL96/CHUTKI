package example.assignment.chutki.apiservice

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import javax.inject.Inject

class VideoCategoryInfoApiService @Inject constructor() {
    suspend fun getResponse(url: String): String? = withContext(Dispatchers.IO) {
        var connection: HttpURLConnection? = null
        var reader: BufferedReader? = null
        try {
            connection = URL(url).openConnection() as HttpURLConnection
            connection.connect()
            val stream: InputStream = connection.getInputStream()
            reader = BufferedReader(InputStreamReader(stream))
            val buffer = StringBuffer()
            var line = ""
            while (reader?.readLine()?.also { line = it } != null) {
                buffer.append(line.trimIndent())
            }
            return@withContext buffer.toString()
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            connection?.disconnect()
            try {
                reader?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return@withContext null
    }
}