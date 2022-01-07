package de.emaarco.pmmltodmn.domain.model.tree

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.lang.RuntimeException
import java.util.*

/**
 * The target attribute of the decision-tree
 */
class TreeTarget(document: Document) {

    var target: Node

    init {
        val allFields: List<Node> = getMiningFields(document)
        this.target = getTargetAttribute(allFields)
    }

    /* -------------------------- private helper methods -------------------------- */

    private fun getMiningFields(document: Document): List<Node> {
        val miningFields = ArrayList<Node>()
        val nodeList = document.getElementsByTagName("MiningField")
        for (i in 0 until nodeList.length) {
            miningFields.add(nodeList.item(i))
        }
        return miningFields
    }

    private fun getTargetAttribute(providedNodes: List<Node>): Node {
        return providedNodes.stream()
            .filter { providedNode: Node -> isTargetAttribute(providedNode) }
            .findFirst()
            .orElseThrow { RuntimeException("Could not find target-attribute of tree") }
    }

    private fun isTargetAttribute(providedNode: Node): Boolean {
        return Optional.ofNullable(providedNode.attributes.getNamedItem("usageType"))
            .map { node: Node -> node.nodeValue == "target" }
            .orElse(false)
    }

}