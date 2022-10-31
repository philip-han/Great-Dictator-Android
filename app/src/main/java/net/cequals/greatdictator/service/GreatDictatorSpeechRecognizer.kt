package net.cequals.greatdictator.service

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import net.cequals.greatdictator.speechcore.service.Speech
import java.util.*


class GreatDictatorSpeechRecognizer(context: Context): Speech {

  private lateinit var speechCallback: (String, Float) -> Unit
  private lateinit var speechRecognizerStateCallback: (listening: Boolean) -> Unit
  private val speechRecognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)

  override fun start() {

    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

    speechRecognizer.setRecognitionListener(object : RecognitionListener {
      override fun onReadyForSpeech(p0: Bundle?) {
        println("onReadyForSpeech")
        speechRecognizerStateCallback(true)
        //readyForSpeech = true
      }

      override fun onBeginningOfSpeech() {
        println("onBeginningOfSpeech")
      }

      override fun onRmsChanged(p0: Float) {
        //println("onRmsChanged")
      }

      override fun onBufferReceived(p0: ByteArray?) {
        println("onBufferReceived")
      }

      override fun onEndOfSpeech() {
        println("onEndOfSpeech")
        speechRecognizerStateCallback(false)
      }

      override fun onError(err: Int) {
        println("onError $err")
        if (err == SpeechRecognizer.ERROR_NO_MATCH) speechRecognizer.startListening(intent)
        else speechRecognizerStateCallback(false)
      }

      override fun onResults(result: Bundle?) {
        result?.run { getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.get(0) }?.let { speech ->
          println("onResults: $speech")
          val score = result.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES)?.get(0) ?: 0f
          speechCallback("$speech", score)
        }
      }

      override fun onPartialResults(p0: Bundle?) {
        println("onPartialResults")
      }

      override fun onEvent(p0: Int, p1: Bundle?) {
        println("onEvent")
      }
    })
    speechRecognizer.startListening(intent)

  }

  override fun end() {
    speechRecognizer.cancel()
  }

  override fun registerCallback(callback: (String, Float) -> Unit) {
    this.speechCallback = callback
  }

  override fun registerStateCallback(speechRecognizerStateCallback: (Boolean) -> Unit) {
    this.speechRecognizerStateCallback = speechRecognizerStateCallback
  }

  override fun close() {
    speechRecognizer.destroy()
  }

}