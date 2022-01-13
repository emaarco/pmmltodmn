package de.emaarco.pmmltodmn.domain.model.dmn

import de.emaarco.pmmltodmn.domain.model.tree.DataField
import de.emaarco.pmmltodmn.domain.utils.IdUtils
import org.w3c.dom.Document
import org.w3c.dom.Element

/**
 * Output attribute of a dmn-decision-table
 */
class OutputAttribute(document: Document, targetAttribute: DataField) {

    private val output: Element

    fun appendTo(parent: Element) {
        parent.appendChild(output)
    }

    init {
        output = document.createElement("output")
        output.setAttribute("id", "Output_${IdUtils.buildRandomId()}")
        output.setAttribute("name", targetAttribute.name)
        output.setAttribute("typeRef", targetAttribute.dataType)
    }
}