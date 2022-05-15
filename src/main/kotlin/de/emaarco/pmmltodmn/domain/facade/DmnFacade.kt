package de.emaarco.pmmltodmn.domain.facade

import de.emaarco.pmmltodmn.domain.model.dmn.DmnModelRequest
import de.emaarco.pmmltodmn.domain.model.tree.TreeInfo
import de.emaarco.pmmltodmn.domain.service.DecisionTreeService
import de.emaarco.pmmltodmn.domain.service.DmnModelService
import org.springframework.core.io.ByteArrayResource
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class DmnFacade(
    private val dmnModelService: DmnModelService,
    private val decisionTreeService: DecisionTreeService,
) {

    fun buildDmnModel(rawPmmlModel: MultipartFile, request: DmnModelRequest): ByteArrayResource {
        val info: TreeInfo = decisionTreeService.extractDecisionTreeFromPmml(rawPmmlModel)
        return dmnModelService.buildDmnTable(info, request)
    }

}