package de.emaarco.pmmltodmn.domain.service

import de.emaarco.pmmltodmn.domain.model.tree.TreeDictionary
import de.emaarco.pmmltodmn.domain.model.tree.TreeInfo
import de.emaarco.pmmltodmn.domain.model.tree.TreePaths
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.w3c.dom.Document
import javax.xml.parsers.DocumentBuilderFactory

@Service
class DecisionTreeService {

    fun extractDecisionTreeFromPmml(rawPmmlFile: MultipartFile): TreeInfo {
        val document = mapPmmlDocumentToXML(rawPmmlFile)
        val treeDictionary = TreeDictionary(document)
        val treePaths = TreePaths(document)
        return TreeInfo(treeDictionary, treePaths.treePaths)
    }

    /* -------------------------- private helper methods -------------------------- */

    private fun mapPmmlDocumentToXML(rawPmmlFile: MultipartFile): Document {
        return try {
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            val document = builder.parse(rawPmmlFile.inputStream)
            document.documentElement.normalize()
            document
        } catch (ex: Exception) {
            throw RuntimeException("Could not map provided pmml file to xml")
        }
    }

}