package de.emaarco.pmmltodmn.domain.model.dmn

import de.emaarco.pmmltodmn.domain.utils.AttributeUtils
import de.emaarco.pmmltodmn.domain.utils.NodeUtils
import org.w3c.dom.Node
import java.lang.RuntimeException

/**
 * One condition of a rule, in a dmn-decision-table
 */
class DecisionCondition(conditionNode: Node) {

    val fieldName: String
    private val value: String
    private val comparator: String

    init {
        fieldName = NodeUtils.getValueOfNodeAttribute(conditionNode, "field")
        value = NodeUtils.getValueOfNodeAttribute(conditionNode, "value")
        val rawComparator: String = NodeUtils.getValueOfNodeAttribute(conditionNode, "operator")
        comparator = getCompareOperator(rawComparator)
    }

    fun getCondition(dataType: String): String {
        return if (comparator == "" && dataType == "string") {
            "\"${value}\""
        } else if (comparator == "") {
            value
        } else {
            "(${AttributeUtils.getVariableName(fieldName)} $comparator ${value})"
        }
    }

    /* -------------------------- private helper methods -------------------------- */

    private fun getCompareOperator(comparator: String): String {
        return when (comparator) {
            "equal" -> "" // ==
            "notEqual" -> "!="
            "less" -> "<"
            "lessOrEqual" -> "<="
            "greaterThan" -> ">"
            "greaterOrEqual" -> ">="
            else -> throw RuntimeException("Unbekannter Operator")
        }
    }

}