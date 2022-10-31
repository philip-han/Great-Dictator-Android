package net.cequals.greatdictator

import android.Manifest.permission.RECORD_AUDIO
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import net.cequals.greatdictator.speechcore.model.SpeechBusinessLogic
import net.cequals.greatdictator.ui.theme.GreatDictatorTheme
import net.cequals.greatdictator.viewmodel.GreatDictatorViewModel
import net.cequals.lib.nullsafe.Scoped
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf
import java.util.*
import kotlin.LazyThreadSafetyMode.NONE

class MainActivity : ComponentActivity(), KoinComponent {

  private val viewmodel: GreatDictatorViewModel by viewModel { parametersOf(applicationContext) }
  private val speechlist by lazy(NONE) { viewmodel.speechFlow }
  val speechRecognizerStateFlow by lazy(NONE) { viewmodel.speechRecognizerStateFlow }
  val errorFlow by lazy(NONE) { viewmodel.errorFlow }
  private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
    viewmodel.onEvent()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      GreatDictatorTheme {
        MainContent(speechlist, speechRecognizerStateFlow, errorFlow, ::handleClick)
      }
    }
  }

  private fun handleClick() {
    println("clicked")
    if (ContextCompat.checkSelfPermission(
        this,
        RECORD_AUDIO
      ) != PackageManager.PERMISSION_GRANTED
    ) {
      // Pass any permission you want while launching
      requestPermission.launch(RECORD_AUDIO)
    } else viewmodel.onEvent()
  }

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalLifecycleComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun MainContent(
  speechListFlow: StateFlow<List<SpeechBusinessLogic.SpeechBusinessData>>,
  speechRecognizerStateFlow: StateFlow<Pair<Int, Color>>,
  errorFlow: Flow<Scoped<String>>,
  clickHandler: () -> Unit) {

  val flow = speechListFlow.collectAsStateWithLifecycle()
  val recognizerStateFlow = speechRecognizerStateFlow.collectAsStateWithLifecycle()
  val message = errorFlow.collectAsStateWithLifecycle(null)

  Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    LaunchedEffect(message.value) {
      scope.launch {
        message.value?.run {
          snackbarHostState.showSnackbar(
            this.value()!!,
            "",
            duration = SnackbarDuration.Short
          )
        }
      }
    }
    Scaffold(
      snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
      Column(
        modifier = Modifier
          .padding(15.dp)
          .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
      ) {
        LazyColumn(modifier = Modifier
          .fillMaxSize()
          .weight(6.5f, false)) {
          this.items(flow.value) { data ->
            sampleText(s = data.speech)
          }
        }
        Row(modifier = Modifier.weight(0.5f, false)) {
          listeningText(listening = recognizerStateFlow.value)
        }
        Spacer(modifier = Modifier.height(5.dp))
        Row(modifier = Modifier.weight(0.5f, false)) {
          DictateButton(clickHandler)
        }
      }
    }
  }
}

@Composable
private fun sampleText(s: String) {
  Text(s)
}

@Composable
private fun listeningText(listening: Pair<Int, Color>) {
  Text(stringResource(id = listening.first), color = listening.second)
}

@Composable
private fun DictateButton(clickHandler: () -> Unit) {
  Button(onClick = clickHandler) {
    Text("Dictate")
  }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  GreatDictatorTheme {
    MainContent(
      MutableStateFlow(listOf(SpeechBusinessLogic.SpeechBusinessData("preview", "white", "neutral", false))),
      MutableStateFlow(Pair(R.string.listening, Color.Black)),
      MutableStateFlow(Scoped.of(""))
    ) { println("fake handler") }
  }
}