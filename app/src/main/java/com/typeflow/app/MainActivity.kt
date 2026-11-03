package com.typeflow.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private val Navy = Color(0xFF172433)
private val Blue = Color(0xFF4169E1)
private val Page = Color(0xFFF6F7FA)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { TypeFlowApp() }
    }
}

@Composable
private fun TypeFlowApp() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("typeflow_progress", 0) }
    var completed by remember { mutableIntStateOf(prefs.getInt("completed", 0)) }
    var bestWpm by remember { mutableIntStateOf(prefs.getInt("wpm", 0)) }
    var accuracy by remember { mutableIntStateOf(prefs.getInt("accuracy", 100)) }
    var page by remember { mutableStateOf("Home") }
    var lesson by remember { mutableStateOf<Lesson?>(null) }

    MaterialTheme(colorScheme = lightColorScheme(primary = Blue, background = Page, surface = Color.White, onSurface = Navy)) {
        Surface(Modifier.fillMaxSize(), color = Page) {
            if (lesson != null) {
                PracticeScreen(lesson = lesson!!, onExit = { lesson = null }, onComplete = { result ->
                    completed = maxOf(completed, lesson!!.id)
                    bestWpm = maxOf(bestWpm, result.wpm)
                    accuracy = result.accuracy
                    prefs.edit().putInt("completed", completed).putInt("wpm", bestWpm).putInt("accuracy", accuracy).apply()
                    lesson = null
                })
            } else {
                Column(Modifier.fillMaxSize()) {
                    Row(Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 18.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("Type", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Navy)
                        Text("Flow", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Blue)
                        Spacer(Modifier.weight(1f))
                        Text("OFFLINE TYPING", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    }
                    AnimatedContent(page, label = "main-page", modifier = Modifier.weight(1f)) { current ->
                        when (current) {
                            "Home" -> HomeScreen(completed, bestWpm, accuracy, { lesson = Curriculum.lessons[completed.coerceIn(0, 109)] }, { page = "Learn" })
                            "Learn" -> LearnScreen(completed) { lesson = it }
                            "Progress" -> ProgressScreen(completed, bestWpm, accuracy)
                            else -> SettingsScreen()
                        }
                    }
                    NavigationBar(containerColor = Color.White) {
                        listOf("Home", "Learn", "Progress", "Settings").forEach { item ->
                            NavigationBarItem(selected = page == item, onClick = { page = item },
                                icon = { Text(when (item) { "Home" -> "⌂"; "Learn" -> "▤"; "Progress" -> "↗"; else -> "⚙" }, fontSize = 20.sp) },
                                label = { Text(item) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeScreen(done: Int, wpm: Int, accuracy: Int, continueLesson: () -> Unit, learn: () -> Unit) {
    LazyColumn(contentPadding = PaddingValues(22.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Text("Build a skill that lasts.", fontSize = 29.sp, fontWeight = FontWeight.Bold, color = Navy)
            Text("A little focused practice goes a long way.", color = Color.Gray)
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy), shape = RoundedCornerShape(22.dp), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("YOUR NEXT STEP", color = Color(0xFFB9C9FF), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(if (done == 0) "Start with the home row" else "Continue your learning journey", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text("Learn correct finger positions and build speed naturally.", color = Color(0xFFD5DCE8))
                    Button(onClick = continueLesson) { Text("Continue lesson  →") }
                }
            }
        }
        item { Text("Your overview", fontSize = 19.sp, fontWeight = FontWeight.Bold) }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Metric("Lessons", "$done / 110", Modifier.weight(1f))
                Metric("Best WPM", "$wpm", Modifier.weight(1f))
                Metric("Accuracy", "$accuracy%", Modifier.weight(1f))
            }
        }
        item {
            OutlinedButton(onClick = learn, modifier = Modifier.fillMaxWidth()) { Text("Explore all lessons") }
            Text("Works offline • No account • Progress stays on this device", color = Color.Gray, fontSize = 12.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun Metric(label: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier, colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(15.dp)) {
        Column(Modifier.padding(13.dp)) {
            Text(label, color = Color.Gray, fontSize = 11.sp)
            Text(value, color = Navy, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun LearnScreen(done: Int, start: (Lesson) -> Unit) {
    LazyColumn(contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item {
            Text("Learning path", fontSize = 27.sp, fontWeight = FontWeight.Bold, color = Navy)
            Text("110 lessons • Beginner to advanced", color = Color.Gray)
        }
        items(Curriculum.lessons) { item ->
            val unlocked = item.id <= done + 1
            Card(Modifier.fillMaxWidth().clickable(enabled = unlocked) { start(item) }, colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(14.dp)) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(40.dp).background(if (item.id <= done) Color(0xFFE4F4EA) else Color(0xFFEEF1F7), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                        Text(if (item.id <= done) "✓" else if (unlocked) "▶" else "•", color = if (item.id <= done) Color(0xFF218653) else Blue)
                    }
                    Column(Modifier.weight(1f).padding(start = 12.dp)) {
                        Text(item.title, fontWeight = FontWeight.SemiBold, color = Navy)
                        Text(item.course, color = Color.Gray, fontSize = 12.sp)
                    }
                    Text(if (unlocked) "Start" else "Locked", color = if (unlocked) Blue else Color.Gray, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun ProgressScreen(done: Int, wpm: Int, accuracy: Int) {
    Column(Modifier.fillMaxSize().padding(22.dp), verticalArrangement = Arrangement.spacedBy(15.dp)) {
        Text("Your progress", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Navy)
        LinearProgressIndicator(progress = { done / 110f }, modifier = Modifier.fillMaxWidth())
        Text("$done of 110 lessons completed", color = Color.Gray)
        Metric("Best recorded speed", "$wpm WPM", Modifier.fillMaxWidth())
        Metric("Last lesson accuracy", "$accuracy%", Modifier.fillMaxWidth())
        Text("Your results are saved locally on this device.", color = Color.Gray)
    }
}

@Composable
private fun SettingsScreen() {
    Column(Modifier.fillMaxSize().padding(22.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("Settings", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Navy)
        Text("TypeFlow works offline and does not need an account.", color = Color.Gray)
        Text("Physical keyboard: USB OTG and Bluetooth keyboards are supported through Android key events.")
        Text("Version 0.1.0 • Minimum Android 10", color = Color.Gray, fontSize = 12.sp)
    }
}

@Composable
private fun PracticeScreen(lesson: Lesson, onExit: () -> Unit, onComplete: (TypingResult) -> Unit) {
    val engine = remember(lesson.id) { TypingEngine(lesson.prompt) }
    var typed by remember { mutableStateOf("") }
    var seconds by remember { mutableIntStateOf(0) }
    var paused by remember { mutableStateOf(false) }
    var complete by remember { mutableStateOf(false) }
    val focus = remember { FocusRequester() }

    LaunchedEffect(paused, complete) {
        while (!paused && !complete) { delay(1000); seconds++ }
    }
    BackHandler { onExit() }
    Column(
        Modifier.fillMaxSize().padding(18.dp).focusRequester(focus).onPreviewKeyEvent { event ->
            if (event.type != KeyEventType.KeyDown || paused || complete) return@onPreviewKeyEvent false
            when (event.key) {
                Key.Backspace -> { engine.backspace(); typed = engine.input; true }
                Key.Enter -> { engine.accept('\n'); typed = engine.input; true }
                Key.Spacebar -> { engine.accept(' '); typed = engine.input; true }
                else -> {
                    val code = event.nativeKeyEvent.unicodeChar
                    if (code != 0 && !event.nativeKeyEvent.isCtrlPressed && !event.nativeKeyEvent.isAltPressed) {
                        engine.accept(code.toChar()); typed = engine.input; true
                    } else false
                }
            }
        }.focusable(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onExit) { Text("← Exit") }
            Column(Modifier.weight(1f)) {
                Text(lesson.title, fontWeight = FontWeight.Bold, color = Navy)
                Text(lesson.course, color = Color.Gray, fontSize = 12.sp)
            }
            Text("%02d:%02d".format(seconds / 60, seconds % 60), fontWeight = FontWeight.Bold, color = Navy)
        }
        Text(lesson.objective, color = Color.Gray)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Metric("WPM", if (seconds == 0) "0" else ((engine.correctCount() / 5f) / (seconds / 60f)).toInt().toString(), Modifier.weight(1f))
            Metric("Accuracy", if (typed.isEmpty()) "100%" else "${engine.correctCount() * 100 / typed.length}%", Modifier.weight(1f))
            Metric("Errors", engine.errors.toString(), Modifier.weight(1f))
        }
        Card(Modifier.weight(1f).fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(18.dp)) {
            Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.Center) {
                Text("TYPE THE TEXT BELOW", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(18.dp))
                Text(lesson.prompt.take(typed.length), fontSize = 23.sp, lineHeight = 36.sp, color = Color(0xFF269B67))
                Text(lesson.prompt.drop(typed.length.coerceAtMost(lesson.prompt.length)), fontSize = 23.sp, lineHeight = 36.sp, color = Color(0xFF9AA3AF))
                Spacer(Modifier.height(12.dp))
                Text("Typed: $typed", color = Navy, fontSize = 13.sp)
            }
        }
        Text(if (typed.isEmpty()) "Connect your physical keyboard and start typing." else "${typed.length} / ${lesson.prompt.length} characters", color = Color.Gray, fontSize = 12.sp)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(onClick = { paused = !paused }, modifier = Modifier.weight(1f)) { Text(if (paused) "Resume" else "Pause") }
            OutlinedButton(onClick = { engine.reset(); typed = ""; seconds = 0; paused = false; complete = false }, modifier = Modifier.weight(1f)) { Text("Restart") }
        }
        if (engine.isComplete && !complete) {
            complete = true
            onComplete(engine.result(seconds))
        }
        LaunchedEffect(Unit) { focus.requestFocus() }
    }
}
