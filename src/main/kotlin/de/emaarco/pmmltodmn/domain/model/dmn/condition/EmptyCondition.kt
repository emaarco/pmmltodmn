package de.emaarco.pmmltodmn.domain.model.dmn.condition

/**
 * Represents an empty condition of a dmn decision-table-rule
 * (in case a specific attribute is not relevant for a rule)
 */
class EmptyCondition : DecisionRuleCondition() {

    override fun getFeelCondition(): String {
        return ""
    }

}