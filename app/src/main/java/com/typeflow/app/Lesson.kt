package com.typeflow.app

data class Lesson(val id: Int, val title: String, val course: String, val objective: String, val prompt: String, val level: String)

object Curriculum {
    private val home = listOf("asdf jkl; asdf jkl;", "a s d f j k l ;", "ff jj dd kk ss ll aa ;;", "sad lad fall ask flask", "all dads ask; a lad falls.")
    private val top = listOf("qwer uiop qwer uiop", "q w e r u i o p", "were ripe power quiet", "write your own quiet poem", "our upper row is ready.")
    private val bottom = listOf("zxcv nm,. zxcv nm,.", "z x c v n m , .", "can mix, move, and zoom.", "vivid caves contain many maps.", "mix words with care.")
    private val passages = listOf("Practice makes progress, one careful keystroke at a time.", "A calm rhythm helps you type with accuracy and confidence.", "Small improvements become lasting skills through daily effort.", "Keep your eyes on the text and let your fingers learn.", "Good typing balances speed, comfort, and precision.")
    val lessons: List<Lesson> = (1..110).map { n ->
        val course = when (n) {
            in 1..5 -> "Getting Started"; in 6..20 -> "Home Row"; in 21..35 -> "Top Row"
            in 36..50 -> "Bottom Row"; in 51..60 -> "Complete Alphabet"; in 61..70 -> "Capitalization & Numbers"
            in 71..80 -> "Words & Sentences"; in 81..90 -> "Advanced Typing"; else -> "Professional Typing"
        }
        val prompt = when (n) {
            1 -> "asdf jkl; asdf jkl;"; 2 -> "a s d f j k l ;"; 3 -> "fff jjj ddd kkk sss lll aaa ;;;"
            4 -> "sad dad ask fall flask"; 5 -> "a dad asks; a lad falls."
            in 6..20 -> home[(n - 6) % home.size]
            in 21..35 -> top[(n - 21) % top.size]
            in 36..50 -> bottom[(n - 36) % bottom.size]
            in 51..60 -> "the quick red fox jumps over a lazy dog."
            in 61..70 -> "Type 123, 456, and 789. Use Shift for capitals!"
            else -> passages[(n - 71) % passages.size] + if (n % 2 == 0) " " + passages[(n - 70) % passages.size] else ""
        }
        Lesson(n, "Lesson $n", course, "Build accuracy and confidence with ${course.lowercase()} skills.", prompt, if (n <= 20) "Beginner" else if (n <= 70) "Intermediate" else "Advanced")
    }
}
