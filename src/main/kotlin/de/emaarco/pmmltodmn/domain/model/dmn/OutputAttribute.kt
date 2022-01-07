package de.emaarco.pmmltodmn.domain.model.dmn

import de.emaarco.pmmltodmn.domain.utils.IdUtils
import de.emaarco.pmmltodmn.domain.utils.NodeUtils
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node

/**
 * Output attribute of a dmn-decision-table
 */
class OutputAttribute(document: Document, targetAttribute: Node) {

    private val output: Element

    fun appendTo(parent: Element) {
        parent.appendChild(output)
    }

    init {
        output = document.createElement("output")
        output.setAttribute("id", "Output_${IdUtils.buildRandomId()}")
        output.setAttribute("name", NodeUtils.getValueOfNodeAttribute(targetAttribute, "name"))
        output.setAttribute("typeRef", NodeUtils.getValueOfNodeAttribute(targetAttribute, "dataType"))
    }
}