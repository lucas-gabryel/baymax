package com.ifal.baymax.ui.triage

import org.junit.Assert.assertEquals
import org.junit.Test

class TriageLogicTest {

    private val geral = Specialties.first { it.key == "geral" } // threshold 8, EMERGENCY
    private val psicologia = Specialties.first { it.key == "psicologia" } // threshold 7, CRISIS

    @Test
    fun `below threshold returns normal`() {
        assertEquals(Outcome.NORMAL, determineOutcome(geral, 5))
    }

    @Test
    fun `at threshold on emergency specialty returns emergency`() {
        assertEquals(Outcome.EMERGENCY, determineOutcome(geral, 8))
    }

    @Test
    fun `at threshold on crisis specialty returns crisis`() {
        assertEquals(Outcome.CRISIS, determineOutcome(psicologia, 7))
    }

    @Test
    fun `summary lists chips and notes`() {
        val summary = buildSymptomsSummary(setOf("Febre", "Enjoo"), " dor no corpo ")
        assertEquals("Febre, Enjoo, dor no corpo", summary)
    }

    @Test
    fun `summary falls back when nothing informed`() {
        val summary = buildSymptomsSummary(emptySet(), "  ")
        assertEquals("nenhum sintoma detalhado", summary)
    }
}
