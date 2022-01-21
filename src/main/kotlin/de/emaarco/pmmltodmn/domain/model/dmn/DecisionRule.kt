package de.emaarco.pmmltodmn.domain.model.dmn

import de.emaarco.pmmltodmn.domain.model.dmn.condition.CategoricalCondition
import de.emaarco.pmmltodmn.domain.model.dmn.condition.DecisionRuleCondition
import de.emaarco.pmmltodmn.domain.model.dmn.condition.EmptyCondition
import de.emaarco.pmmltodmn.domain.model.dmn.condition.NumericalCondition
import de.emaarco.pmmltodmn.domain.model.tree.TreeDictionary
import de.emaarco.pmmltodmn.domain.utils.IdUtils
import de.emaarco.pmmltodmn.domain.utils.NodeUtils
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node

/**
 * A specific rule of a dmn-decision table.
 * It has 'X' input expressions - and '1' output expression
 */
class DecisionRule(document: Document, dictionary: TreeDictionary, treeRoute: List<Node>) {

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
        buildRule(document, dictionary, treeRoute)
        ruleRoot.appendChild(outputExpression)
    }

    fun appendTo(parent: Element) {
        parent.appendChild(ruleRoot)
    }

    /* -------------------------- private helper methods -------------------------- */

    private fun buildRule(document: Document, dictionary: TreeDictionary, treeRoute: List<Node>) {

        val groupedTree = groupByField(treeRoute)

        dictionary.getNonTargetFields().forEach { attribute ->

            val inputExpressionOfRule = document.createElement("inputEntry")
            inputExpressionOfRule.setAttribute("id", "UnaryTests_${IdUtils.buildRandomId()}")
            ruleRoot.appendChild(inputExpressionOfRule)

            val relevantEntries = groupedTree[attribute.name] ?: ArrayList()
            val conditionForField: DecisionRuleCondition = buildCondition(attribute.opType, relevantEntries)

            val conditionOfRuleInput = document.createElement("text")
            conditionOfRuleInput.appendChild(document.createTextNode(conditionForField.getFeelCondition()))
            inputExpressionOfRule.appendChild(conditionOfRuleInput)
            inputExpressions.add(inputExpressionOfRule)
        }
        updateOutputExpression(treeRoute.last())
    }

    private fun updateOutputExpression(outputNode: Node) {
        val output = outputExpression.childNodes.item(0)
        val outputValue: String = NodeUtils.getValueOfNodeAttribute(outputNode, "score")
        output.textContent = "\"$outputValue\""
    }

    private fun groupByField(nodes: List<Node>): Map<String, List<Node>> {
        return nodes.groupBy { node ->
            val conditionOfCurrentNode: Node = NodeUtils.getConditionOfTreeNode(node)
            NodeUtils.getValueOfNodeAttribute(conditionOfCurrentNode, "field")
        }
    }

    private fun buildCondition(attributeType: String, relevantEntries: List<Node>): DecisionRuleCondition {
        return if (relevantEntries.isEmpty()) {
            EmptyCondition()
        } else if (attributeType == "continuous") {
            NumericalCondition(relevantEntries)
        } else {
            CategoricalCondition(relevantEntries[0])
        }
    }

}