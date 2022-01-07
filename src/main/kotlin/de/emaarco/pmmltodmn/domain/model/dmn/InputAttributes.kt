package de.emaarco.pmmltodmn.domain.model.dmn

import de.emaarco.pmmltodmn.domain.utils.AttributeUtils
import de.emaarco.pmmltodmn.domain.utils.IdUtils
import de.emaarco.pmmltodmn.domain.utils.NodeUtils
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.util.ArrayList
import java.util.HashMap
import java.util.function.Consumer

/**
 * All input attributes of a specific dmn-decision-table
 */
class InputAttributes(document: Document, usedAttributes: List<String>, attributeDictionary: HashMap<String, Node>) {

    private val inputAttributes: MutableList<Element>

    init {
        inputAttributes = ArrayList()
        usedAttributes.forEach { attribute: String ->
            val dictionaryAttribute = attributeDictionary[attribute]
            dictionaryAttribute?.let {
                val inputAttribute = createInputAttribute(document, it)
                inputAttributes.add(inputAttribute)
            }
        }
    }

    fun appendTo(parent: Element) {
        inputAttributes.forEach(Consumer { newChild: Element? -> parent.appendChild(newChild) })
    }

    /* -------------------------- private helper methods -------------------------- */

    private fun createInputAttribute(document: Document, dictionaryAttribute: Node): Element {
        // <input>..</input>
        val nameOfAttribute: String = NodeUtils.getValueOfNodeAttribute(dictionaryAttribute, "name")
        val input = createInput(document, nameOfAttribute)

        // <inputExpression>...</inputExpression/> --> within <input>...</input>
        val typeOfAttribute: String = NodeUtils.getValueOfNodeAttribute(dictionaryAttribute, "dataType")
        val inputExpression = createInputExpression(document, typeOfAttribute)
        input.appendChild(inputExpression)

        // <text>...</text> ---> within <inputExpression>...</inputExpression/>
        val text = createText(document, nameOfAttribute)
        inputExpression.appendChild(text)
        return input
    }

    /**
     * Create node '<input></input>...'
     * ...that will contain '<inputExpression>...</inputExpression>'
     */
    fun createInput(document: Document, nameOfAttribute: String?): Element {
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
        inputExpression.setAttribute("expressionLanguage", "juel")
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