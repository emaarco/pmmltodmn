package de.emaarco.pmmltodmn.domain.model.dmn

import org.w3c.dom.Document
import org.w3c.dom.Element

/**
 * A decision, that contains decision-representations, like a decision-table
 */
class Decision(document: Document, model: DmnModel, id: String, name: String) {

    val decision: Element

    init {
        decision = document.createElement("decision")
        decision.setAttribute("id", id)
        decision.setAttribute("name", name)
        model.dmnModel.appendChild(decision)
    }
}