package de.emaarco.pmmltodmn.domain.model.dmn

import de.emaarco.pmmltodmn.domain.utils.IdUtils
import de.emaarco.pmmltodmn.domain.utils.NodeUtils
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.util.ArrayList
import java.util.HashMap
import java.util.function.Consumer

/**
 * A specific rule of a dmn-decision table.
 * It has 'X' input expressions - and '1' output expression
 */
class DecisionRule(document: Document, usedAttributes: List<String>) {

    private val ruleRoot: Element
    private val inputExpressions: MutableList<Element>
    private val outputExpression: Element

    init {
        ruleRoot = document.createElement("rule")
        outputExpression = document.createElement("outputEntry")
        inputExpressions = ArrayList()
        ruleRoot.setAttribute("id", "DecisionRule_${IdUtils.buildRandomId()}")
        outputExpression.setAttribute("id", "LiteralExpression_${IdUtils.buildRandomId()}")
        outputExpression.appendChild(document.createElement("text"))
        addEmptyInputExpressionsToRule(document, usedAttributes)
        ruleRoot.appendChild(outputExpression)
    }

    fun appendTo(parent: Element) {
        parent.appendChild(ruleRoot)
    }

    /**
     * Places the actual conditions into the previously (...empty initialized...) fields of the rule
     */
    fun updateConditionsOfRule(treeRoute: List<Node>, usedAttributes: List<String>, dictionary: HashMap<String, Node>) {
        for (i in treeRoute.indices) {
            // Find node & get decision-condition
            val currentNode = treeRoute[i]
            val conditionOfCurrentNode: Node = NodeUtils.getConditionOfTreeNode(currentNode)
            val condition = DecisionCondition(conditionOfCurrentNode)
            val indexOfCurrentNode = usedAttributes.indexOf(condition.fieldName)
            val dictionaryAttribute = dictionary[condition.fieldName]

            // Update node
            dictionaryAttribute?.let {
                val matchingElement = inputExpressions[indexOfCurrentNode]
                val dataType: String = NodeUtils.getValueOfNodeAttribute(it, "dataType")
                val text = matchingElement.childNodes.item(0)
                val currentFeelCondition = matchingElement.textContent
                val newFeelCondition = condition.getCondition(dataType)
                if (currentFeelCondition.isBlank()) {
                    text.textContent = newFeelCondition
                } else {
                    text.textContent = String.format("%s and %s", currentFeelCondition, newFeelCondition)
                }
            }

            // if node is leaf --> set outputEntry
            if (i == treeRoute.size - 1) {
                updateOutputExpression(currentNode)
            }
        }
    }
    /* -------------------------- private helper methods -------------------------- */

    /**
     * Initially, all conditions of a rule are empty, in order to ensure a correct order of the attributes
     * --> In a second step, existing conditions will replace the empty ones
     */
    private fun addEmptyInputExpressionsToRule(document: Document, usedAttributes: List<String>) {
        usedAttributes.forEach(Consumer { inputAttribute: String? ->
            // Container for the condition
            val inputExpressionOfRule = document.createElement("inputEntry")
            inputExpressionOfRule.setAttribute("id", "UnaryTests_${IdUtils.buildRandomId()}")
            ruleRoot.appendChild(inputExpressionOfRule)
            // Empty condition
            val conditionOfRuleInput = document.createElement("text")
            conditionOfRuleInput.appendChild(document.createTextNode(""))
            inputExpressionOfRule.appendChild(conditionOfRuleInput)
            // Add inputExpression to list
            inputExpressions.add(inputExpressionOfRule)
        })
    }

    private fun updateOutputExpression(outputNode: Node) {
        val output = outputExpression.childNodes.item(0)
        val outputValue: String = NodeUtils.getValueOfNodeAttribute(outputNode, "score")
        output.textContent = "\"" + outputValue + "\""
    }

}