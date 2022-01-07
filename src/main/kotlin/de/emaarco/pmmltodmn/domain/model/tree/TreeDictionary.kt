package de.emaarco.pmmltodmn.domain.model.tree

import org.w3c.dom.Document
import org.w3c.dom.Node
import kotlin.collections.HashMap

/**
 * All data-fields that are used in the decision tree
 * (--> either as an input- or as an output-field)
 */
class TreeDictionary(document: Document) {

    var dictionary: HashMap<String, Node> = HashMap()

    init {
        this.fillDictionary(document)
    }

    /* -------------------------- private helper methods -------------------------- */

    private fun fillDictionary(document: Document) {
        val dataFields = document.getElementsByTagName("DataField")
        for (i in 0 until dataFields.length) {
            val field = dataFields.item(i)
            val fieldName = field.attributes.getNamedItem("name").nodeValue
            dictionary[fieldName] = field
        }
    }

}