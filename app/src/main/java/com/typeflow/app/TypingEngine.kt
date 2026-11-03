package com.typeflow.app

import kotlin.math.roundToInt

data class TypingResult(val correct: Int, val errors: Int, val elapsedSeconds: Int, val total: Int) {
    val accuracy: Int get() = if (total == 0) 100 else (correct * 100f / total).roundToInt()
    val wpm: Int get() = if (elapsedSeconds <= 0) 0 else ((correct / 5f) / (elapsedSeconds / 60f)).roundToInt()
}
class TypingEngine(val target: String) {
    private val typed = StringBuilder()
    var errors: Int = 0
        private set
    val input: String get() = typed.toString()
    val isComplete: Boolean get() = typed.length >= target.length
    fun accept(char: Char) { if (!isComplete) { typed.append(char); if (target.getOrNull(typed.lastIndex) != char) errors++ } }
    fun backspace() { if (typed.isNotEmpty()) typed.deleteCharAt(typed.lastIndex) }
    fun reset() { typed.clear(); errors = 0 }
    fun correctCount(): Int = typed.indices.count { typed[it] == target[it] }
    fun result(seconds: Int) = TypingResult(correctCount(), errors, seconds.coerceAtLeast(1), typed.length)
}
