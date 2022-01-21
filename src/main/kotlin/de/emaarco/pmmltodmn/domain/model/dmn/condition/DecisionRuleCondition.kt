package de.emaarco.pmmltodmn.domain.model.dmn.condition

/**
 * A single attribute of a decision-table rule
 * ...can consist of several conditions...
 */
abstract class DecisionRuleCondition {

    /**
     * Condition of the decision-rule represented in 'FEEL' language
     */
    abstract fun getFeelCondition(): String

}