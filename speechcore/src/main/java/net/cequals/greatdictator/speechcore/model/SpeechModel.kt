package net.cequals.greatdictator.speechcore.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import net.cequals.greatdictator.speechcore.service.Speech
import net.cequals.greatdictator.speechcore.service.http.SpeechClient
import net.cequals.lib.nullsafe.Scoped

class SpeechModel(val speechRecognizer: Speech, val speechClient: SpeechClient) {

  private val businessLogic = SpeechBusinessLogic()
  private val ioScope = CoroutineScope(Dispatchers.IO)
  internal val speechListFlow = MutableStateFlow<List<SpeechBusinessLogic.SpeechBusinessData>>(emptyList())
  internal val speechRecognitionStateFlow = MutableStateFlow(false)
  internal val errorChannel = Channel<Scoped<String>>(capacity = Channel.BUFFERED)

  // each submission will execute serially
  private val serialExecutionChannel = Channel<Job>(capacity = Channel.UNLIMITED).apply {
    ioScope.launch {
      consumeEach { it.join() }
    }
  }

  init {
    speechRecognizer.registerCallback { speech, confidence ->
      serialExecutionChannel.trySend (
        ioScope.launch {
          processSpeech(speech, confidence)
        }
      )
    }
    speechRecognizer.registerStateCallback { state ->
      speechRecognitionStateFlow.value = state
    }
  }

  fun startDictation() {
    speechRecognizer.start()
  }

  fun endDictation() {
    speechRecognizer.end()
  }

  private suspend fun processSpeech(speech: String, confidence: Float) {
    val parsedSpeech = speechClient.parseSpeech(speech)
    if (parsedSpeech.isError) {
      errorChannel.trySend(Scoped.of(parsedSpeech.foldError().message ?: "unknown error"))
    } else {
      businessLogic.addSpeech(speech, confidence, parsedSpeech)
      // must create a new instance of list, otherwise flow wont notify change
      speechListFlow.value = businessLogic.speechList.toList()
    }
  }

  fun close() {
    serialExecutionChannel.close()
    errorChannel.close()
    speechRecognizer.close()
  }

}