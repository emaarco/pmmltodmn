package de.emaarco.pmmltodmn.domain.model.tree

import org.w3c.dom.Node
import java.util.*

data class TreeInfo(
    val dictionary: TreeDictionary,
    val allTreePaths: TreeMap<Int, List<Node>>,
)