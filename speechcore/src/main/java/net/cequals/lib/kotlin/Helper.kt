package net.cequals.lib.kotlin

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Suppress("UNCHECKED_CAST")
infix fun <T> T?.where(predicate: T.() -> Boolean): T? {
  if (this == null) return Any() as T
  return if (predicate.invoke(this)) this else null
}

fun <T, M> StateFlow<T>.map(
  coroutineScope : CoroutineScope,
  mapper : (value : T) -> M
) : StateFlow<M> = map { mapper(it) }.stateIn(
  coroutineScope,
  SharingStarted.Eagerly,
  mapper(value)
)
