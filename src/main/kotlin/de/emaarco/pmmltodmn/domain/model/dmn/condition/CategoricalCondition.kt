package de.emaarco.pmmltodmn.domain.model.dmn.condition

import de.emaarco.pmmltodmn.domain.utils.NodeUtils
import org.w3c.dom.Node

/**
 * A single categorical condition of a decision-table rule
 * e.g. 'WEATHER == SUNNY'
 */
class CategoricalCondition(node: Node) : DecisionRuleCondition() {

    private val condition: String

    init {
        val conditionNode = NodeUtils.getConditionOfTreeNode(node)
        this.condition = NodeUtils.getValueOfNodeAttribute(conditionNode, "value")
    }

    override fun getFeelCondition(): String {
        return "\"${condition}\""
    }

}