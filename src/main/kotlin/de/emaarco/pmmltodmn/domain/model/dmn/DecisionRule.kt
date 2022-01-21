package de.emaarco.pmmltodmn.domain.model.dmn

import de.emaarco.pmmltodmn.domain.model.tree.DataField
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

            // Get all conditions for the column
            val allConditionsForField = getConditionsOfNodes(relevantEntries)
            val simplifiedConditionsForField = simplifyConditions(attribute, allConditionsForField)
            val combinedConditions = getFeelCondition(simplifiedConditionsForField, attribute)
            val fullCondition = combinedConditions.joinToString("..").trim()

            val conditionOfRuleInput = document.createElement("text")
            conditionOfRuleInput.appendChild(document.createTextNode(fullCondition))
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
            val condition = DecisionCondition(conditionOfCurrentNode)
            condition.fieldName
        }
    }

    private fun getConditionsOfNodes(nodes: List<Node>): List<DecisionCondition> {
        return nodes.map { node ->
            val condition = NodeUtils.getConditionOfTreeNode(node)
            DecisionCondition(condition)
        }
    }

    private fun simplifyConditions(field: DataField, nodes: List<DecisionCondition>): List<DecisionCondition> {
        return if (field.opType != "continuous") {
            nodes
        } else nodes.filter {
            val filteredConditions: MutableList<DecisionCondition> = ArrayList()
            val groupedConditions = nodes.groupBy { condition -> condition.comparator }
            groupedConditions.forEach { (condition, values) ->
                filteredConditions.add(reduceNumericConditions(condition, values))
            }
            filteredConditions.sortBy { condition -> condition.value.toDouble() }
            return filteredConditions
        }
    }

    private fun reduceNumericConditions(comparator: String, values: List<DecisionCondition>): DecisionCondition {
        return when (comparator) {
            ">" -> findMaximum(values)
            ">=" -> findMaximum(values)
            "<" -> findMinimum(values)
            "<=" -> findMinimum(values)
            else -> throw RuntimeException("Cannot filter conditions for comparator '$comparator'")
        }
    }

    private fun findMinimum(values: List<DecisionCondition>): DecisionCondition {
        val minValue = values.map { v -> v.value.toDouble() }.minOrNull()
        return values.find { v -> v.value.toDouble() == minValue }
            ?: throw RuntimeException("Could not find minimal value for provided attribute")
    }

    private fun findMaximum(values: List<DecisionCondition>): DecisionCondition {
        val minValue = values.map { v -> v.value.toDouble() }.maxOrNull()
        return values.find { v -> v.value.toDouble() == minValue }
            ?: throw RuntimeException("Could not find maximal value for provided attribute")
    }

    private fun getFeelCondition(conditions: List<DecisionCondition>, dataField: DataField): List<String> {
        return conditions.map { condition ->
            condition.getCondition(dataField.dataType, conditions.size > 1)
        }
    }

}