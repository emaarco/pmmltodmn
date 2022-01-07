package de.emaarco.pmmltodmn.domain.model.tree

import de.emaarco.pmmltodmn.domain.utils.NodeUtils
import org.w3c.dom.Node
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.HashMap

data class TreeInfo(
    val dictionary: HashMap<String, Node>,
    val allTreePaths: TreeMap<Int, List<Node>>,
    val targetAttribute: Node,
) {

    fun getNameOfTargetAttribute(): String {
        return NodeUtils.getValueOfNodeAttribute(targetAttribute, "name")
    }

    fun dictionaryAttributes(): List<String> {
        val allNodes: List<Node> = getAllTreeNodes();
        return allNodes.stream()
            .map { node: Node -> getAttributeFromDictionary(node) }
            .distinct().collect(Collectors.toList())
    }

    /* -------------------------- private helper methods -------------------------- */

    private fun getAllTreeNodes(): List<Node> {
        return allTreePaths.values.stream()
            .flatMap { obj: List<Node> -> obj.stream() }
            .distinct().collect(Collectors.toList())
    }

    private fun getAttributeFromDictionary(node: Node): String {
        val conditionOfNode: Node = NodeUtils.getConditionOfTreeNode(node)
        return NodeUtils.getValueOfNodeAttribute(conditionOfNode, "field")
    }

}