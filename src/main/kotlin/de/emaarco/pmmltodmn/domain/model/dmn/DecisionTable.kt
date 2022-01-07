package de.emaarco.pmmltodmn.domain.model.dmn

import de.emaarco.pmmltodmn.domain.utils.IdUtils
import org.w3c.dom.Document
import org.w3c.dom.Element

/**
 * A decision table that represents a single-decision
 */
class DecisionTable(document: Document, decision: Decision) {

    val decisionTable: Element

    init {
        decisionTable = document.createElement("decisionTable")
        decisionTable.setAttribute("id", "DecisionTable_${IdUtils.buildRandomId()}")
        decision.decision.appendChild(decisionTable)
    }
}