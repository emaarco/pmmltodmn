package de.emaarco.pmmltodmn.domain.utils

import org.w3c.dom.Node
import java.lang.RuntimeException

object NodeUtils {

    fun getValueOfNodeAttribute(node: Node, attribute: String?): String {
        return node.attributes
            .getNamedItem(attribute)
            .nodeValue
    }

    fun getConditionOfTreeNode(node: Node): Node {
        var conditionNode: Node? = null
        val childNodes = node.childNodes
        for (i in 0 until childNodes.length) {
            val childNode = childNodes.item(i)
            val isConditionNode = childNode.nodeName == "SimplePredicate"
            if (isConditionNode) {
                conditionNode = childNode
                break
            }
        }
        if (conditionNode == null) {
            throw RuntimeException("Fehler!")
        }
        return conditionNode
    }
}