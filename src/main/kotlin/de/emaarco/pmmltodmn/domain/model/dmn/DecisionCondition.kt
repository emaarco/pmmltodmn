package de.emaarco.pmmltodmn.domain.model.dmn

import de.emaarco.pmmltodmn.domain.utils.NodeUtils
import org.w3c.dom.Node

/**
 * One condition of a rule, in a dmn-decision-table
 */
class DecisionCondition(conditionNode: Node) {

    val fieldName: String
    var value: String
    var comparator: String

    init {
        fieldName = NodeUtils.getValueOfNodeAttribute(conditionNode, "field")
        value = NodeUtils.getValueOfNodeAttribute(conditionNode, "value")
        val rawComparator: String = NodeUtils.getValueOfNodeAttribute(conditionNode, "operator")
        comparator = getCompareOperator(rawComparator)
    }

    fun getCondition(dataType: String, isPartOfDisjunction: Boolean): String {
        return if (comparator == "" && dataType == "string") {
            "\"${value}\""
        } else if (comparator == "") {
            value
        } else if (isPartOfDisjunction && this.comparator == ">=") {
            "[$value"
        } else if (isPartOfDisjunction && this.comparator == "<=") {
            "${value}]"
        } else {
            "$comparator $value"
        }
    }

    fun simplifyCondition() {
        if (this.comparator == ">") {
            this.value = "${this.value.toDouble() + 1}"
            this.comparator = ">="
        } else if (this.comparator == "<") {
            this.value = "${this.value.toDouble() - 1}"
            this.comparator = "<="
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