package net.cequals.greatdictator.speechcore

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import net.cequals.greatdictator.speechcore.model.SpeechBusinessLogic
import net.cequals.greatdictator.speechcore.model.SpeechModel
import net.cequals.greatdictator.speechcore.model.puntuateInterrogative
import net.cequals.lib.kotlin.map
import java.util.function.Consumer

class SpeechCore @JvmOverloads constructor (val model: SpeechModel, observerScope: CoroutineScope = CoroutineScope(Dispatchers.IO)) {

  val speechRecognizerStateFlow: StateFlow<Boolean> = model.speechRecognitionStateFlow
  // channels are events, like errors
  val errorChannel = model.errorChannel
  // cannot map stateflow to stateflow - https://github.com/Kotlin/kotlinx.coroutines/issues/2514
  val speechFlow = model.speechListFlow.asStateFlow().map(coroutineScope = observerScope) {
    it.map(::puntuateInterrogative)
  }

  fun toggle() {
    if (speechRecognizerStateFlow.value) stopDictation()
    else startDictation()
  }

  fun startDictation() {
    model.startDictation()
  }

  fun stopDictation() {
    model.endDictation()
  }

  fun close() {
    model.close()
  }

  inner class FlowAdapter() {

    private val adapterScope = CoroutineScope(Dispatchers.IO)
    private lateinit var speechJob: Job
    private lateinit var stateJob: Job
    private lateinit var errorJob: Job

    fun observe(
      callback: Consumer<List<SpeechBusinessLogic.SpeechBusinessData>>,
      speechRecognizerStateCallback: Consumer<Boolean>,
      errorCallback: Consumer<String>
    ) {
      speechJob = flowToCallback(adapterScope, speechFlow, callback)
      stateJob = flowToCallback(adapterScope, speechRecognizerStateFlow, speechRecognizerStateCallback)
      errorJob = flowToCallback(adapterScope,
        errorChannel.consumeAsFlow().map { it.value()!! },
        errorCallback
      )
    }

    private fun <T> flowToCallback(scope: CoroutineScope, flow: Flow<T>, callback: Consumer<T>): Job {
      return scope.launch {
        try {
          flow.collect {
            callback.accept(it)
          }
        } catch (e: CancellationException) {
          println("collect cancelled")
        }
      }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun close() {
      speechJob.cancel()
      stateJob.cancel()
      errorJob.cancel()
      try {
        Dispatchers.shutdown()
      } catch (e: InterruptedException) {
        println("default dispatcher interrupted")
      }
    }

  }

}