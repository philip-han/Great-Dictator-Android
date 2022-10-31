package net.cequals.greatdictator.speechcore.service

interface Speech {
  fun start()
  fun end()
  fun registerCallback(callback: (String, Float) -> Unit)
  fun registerStateCallback(callback: (Boolean) -> Unit)
  fun close()
}