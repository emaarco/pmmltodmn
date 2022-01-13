package de.emaarco.pmmltodmn.domain.service

import de.emaarco.pmmltodmn.domain.model.dmn.*
import de.emaarco.pmmltodmn.domain.model.tree.DataField
import de.emaarco.pmmltodmn.domain.model.tree.TreeDictionary
import de.emaarco.pmmltodmn.domain.model.tree.TreeInfo
import org.springframework.core.io.ByteArrayResource
import org.springframework.stereotype.Service
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.ByteArrayOutputStream
import java.util.*
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

@Service
class DmnModelService {

    fun buildDmnTable(decisionTree: TreeInfo, request: DmnModelRequest): ByteArrayResource {

        val doc = initializeOutputDocument()
        val dmnModel = DmnModel(doc, request.dmnModelID, request.dmnModelName)
        val decision = Decision(doc, dmnModel, request.dmnDecisionID, request.dmnDecisionName)
        val decisionTable: Element = DecisionTable(doc, decision).decisionTable

        // Provided data
        val tree: TreeMap<Int, List<Node>> = decisionTree.allTreePaths
        val dictionary: TreeDictionary = decisionTree.dictionary

        // Set the input-fields (--> header)
        val inputAttributes = InputAttributes(doc, dictionary.getNonTargetFields())
        inputAttributes.appendTo(decisionTable)

        // Set the output fields (--> header)
        val targetAttribute: DataField = dictionary.getTargetAttribute()
        val outputAttribute = OutputAttribute(doc, targetAttribute)
        outputAttribute.appendTo(decisionTable)

        // Print the rules to the xml
        tree.forEach { (_, treeRoute: List<Node>) ->
            val rule = DecisionRule(doc, dictionary, treeRoute)
            rule.appendTo(decisionTable)
        }

        // Print output-document
        return getFinalDmnModel(doc)
    }

    /* -------------------------- private helper methods -------------------------- */

    private fun initializeOutputDocument(): Document {
        val dbFactory = DocumentBuilderFactory.newInstance()
        val dBuilder: DocumentBuilder
        return try {
            dBuilder = dbFactory.newDocumentBuilder()
            dBuilder.newDocument()
        } catch (ex: Exception) {
            throw RuntimeException("Could not build output document")
        }
    }

    private fun getFinalDmnModel(document: Document): ByteArrayResource {
        return try {
            val transformerFactory = TransformerFactory.newInstance()
            val transformer = transformerFactory.newTransformer()
            transformer.setOutputProperty(OutputKeys.INDENT, "yes")
            val source = DOMSource(document)
            val outputStream = ByteArrayOutputStream()
            val result = StreamResult(outputStream)
            transformer.transform(source, result)
            ByteArrayResource(outputStream.toByteArray())
        } catch (ex: Exception) {
            throw RuntimeException("Could not write result to xml-file")
        }
    }

}