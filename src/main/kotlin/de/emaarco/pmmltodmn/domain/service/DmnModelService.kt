package de.emaarco.pmmltodmn.domain.service

import de.emaarco.pmmltodmn.domain.model.dmn.*
import de.emaarco.pmmltodmn.domain.model.tree.TreeInfo
import org.springframework.core.io.ByteArrayResource
import org.springframework.stereotype.Service
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.lang.RuntimeException
import java.util.*
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

@Service
class DmnModelService {

    private val dmnModelID = "dmn_approve_credit"
    private val dmnDecisionID = "decision_approve_credit"
    private val dmnDecisionName = "Kreditwürdigkeit prüfen"

    fun buildDmnTable(decisionTree: TreeInfo): ByteArrayResource {
        val doc = initializeOutputDocument()
        val dmnModel = DmnModel(doc, dmnModelID, dmnDecisionID)
        val decision = Decision(doc, dmnModel, dmnDecisionID, dmnDecisionName)
        val decisionTable: Element = DecisionTable(doc, decision).decisionTable

        // Provided data
        val tree: TreeMap<Int, List<Node>> = decisionTree.allTreePaths
        val usedAttributes: List<String> = decisionTree.dictionaryAttributes()
        val dictionary: HashMap<String, Node> = decisionTree.dictionary

        // Set the input-fields (--> header)
        val inputAttributes = InputAttributes(doc, usedAttributes, dictionary)
        inputAttributes.appendTo(decisionTable)

        // Set the output fields (--> header)
        val nameOfTargetAttribute: String = decisionTree.getNameOfTargetAttribute()
        val targetAttribute = dictionary[nameOfTargetAttribute]
        val outputAttribute = OutputAttribute(doc, targetAttribute!!)
        outputAttribute.appendTo(decisionTable)

        // Print the rules to the xml
        tree.forEach { (key: Int?, treeRoute: List<Node>) ->
            val rule = DecisionRule(doc, usedAttributes)
            rule.updateConditionsOfRule(treeRoute, usedAttributes, dictionary)
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