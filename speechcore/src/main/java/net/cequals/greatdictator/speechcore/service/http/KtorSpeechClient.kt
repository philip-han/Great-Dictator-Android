package net.cequals.greatdictator.speechcore.service.http

//import io.ktor.client.features.json.* <-- Json clashes with kotlinx.serialization
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import kotlinx.serialization.json.Json
import net.cequals.greatdictator.speechcore.model.dto.SpeechData

class KtorSpeechClient: AbstractSpeechClient() {

  override suspend fun platformParseSpeech(speech: String): SpeechData {
    val uri = uri()
    return HttpClient(CIO) {
      install(JsonFeature) {
        val json = Json { ignoreUnknownKeys = true }
        serializer = KotlinxSerializer(json)
      }
    }.use { httpClient ->
      httpClient.post(uri) {
        body = speech
      }
    }
  }

}