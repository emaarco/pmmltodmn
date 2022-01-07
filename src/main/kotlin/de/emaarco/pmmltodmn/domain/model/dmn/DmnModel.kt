package de.emaarco.pmmltodmn.domain.model.dmn

import org.w3c.dom.Document
import org.w3c.dom.Element

/**
 * Container for the final dmn-model
 */
class DmnModel(document: Document, id: String, name: String) {

    val dmnModel: Element

    init {
        dmnModel = document.createElement("definitions")
        dmnModel.setAttribute("xmlns", "https://www.omg.org/spec/DMN/20191111/MODEL/")
        dmnModel.setAttribute("xmlns:dmndi", "https://www.omg.org/spec/DMN/20191111/DMNDI/")
        dmnModel.setAttribute("xmlns:dc", "http://www.omg.org/spec/DMN/20180521/DC/")
        dmnModel.setAttribute("namespace", "http://camunda.org/schema/1.0/dmn")
        dmnModel.setAttribute("id", id)
        dmnModel.setAttribute("name", name)
        dmnModel.setAttribute("exporter", "dmnandai")
        document.appendChild(dmnModel)
    }

}