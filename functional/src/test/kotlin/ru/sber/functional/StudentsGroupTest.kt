package ru.sber.functional

import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import kotlin.test.assertEquals

internal class StudentsGroupTest {

    @Test
    fun `test predicate`() {
        val studentA = Student("A", "A", "A", 18, 5.0, "Moscow", "IT", null)
        val studentB = Student("B", "B", "B", 20, 3.0, "Omsk", "Design", "IT")
        val studentC = Student("C", "C", "C", 17, 1.337, "Tomsk", "Math", null)

        val studentsGroup = StudentsGroup()
        studentsGroup.students = listOf(studentA, studentB, studentC)

        assertAll(
            Executable {
                val group = studentsGroup.filterByPredicate { it.age > 18 }
                assertEquals(1, group.size)
                assertEquals(studentB, group[0])
            },
            Executable {
                val group = studentsGroup.filterByPredicate { it.prevEducation == null }
                assertEquals(2, group.size)
                assertEquals(studentA, group[0])
                assertEquals(studentC, group[1])
            }
        )
    }

}