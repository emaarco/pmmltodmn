package de.emaarco.pmmltodmn.domain.model.dmn

import de.emaarco.pmmltodmn.domain.model.tree.DataField
import de.emaarco.pmmltodmn.domain.utils.AttributeUtils
import de.emaarco.pmmltodmn.domain.utils.IdUtils
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.util.function.Consumer

/**
 * All input attributes of a specific dmn-decision-table
 */
class InputAttributes(document: Document, dataFields: List<DataField>) {

    private val inputAttributes: MutableList<Element>

    init {
        inputAttributes = ArrayList()
        val relevantDataFields = dataFields.filter { f -> !f.isTarget }
        relevantDataFields.forEach { attribute ->
            val inputAttribute = createInputAttribute(document, attribute)
            inputAttributes.add(inputAttribute)
        }
    }

    fun appendTo(parent: Element) {
        inputAttributes.forEach(Consumer { newChild: Element? -> parent.appendChild(newChild) })
    }

    /* -------------------------- private helper methods -------------------------- */

    private fun createInputAttribute(document: Document, dictionaryAttribute: DataField): Element {
        // <input>..</input>
        val input = createInput(document, dictionaryAttribute.name)

        // <inputExpression>...</inputExpression/> --> within <input>...</input>
        val inputExpression = createInputExpression(document, dictionaryAttribute.dataType)
        input.appendChild(inputExpression)

        // <text>...</text> ---> within <inputExpression>...</inputExpression/>
        val text = createText(document, dictionaryAttribute.name)
        inputExpression.appendChild(text)
        return input
    }

    /**
     * Create node '<input></input>...'
     * ...that will contain '<inputExpression>...</inputExpression>'
     */
    private fun createInput(document: Document, nameOfAttribute: String?): Element {
        val input = document.createElement("input")
        input.setAttribute("id", "Input_${IdUtils.buildRandomId()}")
        input.setAttribute("label", nameOfAttribute)
        return input
    }

    /**
     * Create node '<inputExpression>...</inputExpression>'
     * ...that will contain '<text>...</text>'
     */
    private fun createInputExpression(document: Document, dataType: String): Element {
        val inputExpression = document.createElement("inputExpression")
        inputExpression.setAttribute("id", "InputExpression_${IdUtils.buildRandomId()}")
        inputExpression.setAttribute("typeRef", dataType)
        inputExpression.setAttribute("expressionLanguage", "feel")
        return inputExpression
    }

    /**
     * Create node '<text>...</text>'
     */
    private fun createText(document: Document, nameOfInputVariable: String): Element {
        val text = document.createElement("text")
        val nameOfVariable: String = AttributeUtils.getVariableName(nameOfInputVariable)
        text.appendChild(document.createTextNode(nameOfVariable))
        return text
    }

}