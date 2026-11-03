package com.typeflow.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TypingEngineTest {
    @Test fun correctAndIncorrectCharactersAreTracked() {
        val engine = TypingEngine("cat")
        engine.accept('c')
        engine.accept('x')
        engine.accept('t')
        assertEquals(2, engine.correctCount())
        assertEquals(1, engine.errors)
        assertTrue(engine.isComplete)
        assertEquals(67, engine.result(10).accuracy)
    }

    @Test fun backspaceAllowsCorrectionButKeepsMistakeCount() {
        val engine = TypingEngine("go")
        engine.accept('g')
        engine.accept('x')
        engine.backspace()
        engine.accept('o')
        assertEquals("go", engine.input)
        assertEquals(2, engine.correctCount())
        assertEquals(1, engine.errors)
    }

    @Test fun resetClearsInputAndErrors() {
        val engine = TypingEngine("hi")
        engine.accept('x')
        engine.reset()
        assertEquals("", engine.input)
        assertEquals(0, engine.errors)
        assertFalse(engine.isComplete)
    }

    @Test fun curriculumHas126UniqueSequentialLessons() {
        assertEquals(126, Curriculum.lessons.size)
        assertEquals((1..126).toList(), Curriculum.lessons.map { it.id })
        assertEquals(126, Curriculum.lessons.map { it.prompt }.toSet().size)
        assertTrue(Curriculum.stages.last().range.contains(126))
    }
}
