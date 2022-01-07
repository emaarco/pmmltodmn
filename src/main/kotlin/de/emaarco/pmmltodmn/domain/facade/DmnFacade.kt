package de.emaarco.pmmltodmn.domain.facade

import de.emaarco.pmmltodmn.domain.model.tree.TreeInfo
import de.emaarco.pmmltodmn.domain.service.DecisionTreeService
import de.emaarco.pmmltodmn.domain.service.DmnModelService
import org.springframework.web.multipart.MultipartFile
import org.springframework.core.io.ByteArrayResource
import org.springframework.stereotype.Component

@Component
class DmnFacade(private val dmnModelService: DmnModelService, private val decisionTreeService: DecisionTreeService) {

    fun buildDmnModel(rawPmmlModel: MultipartFile): ByteArrayResource {
        val info: TreeInfo = decisionTreeService.extractDecisionTreeFromPmml(rawPmmlModel)
        return dmnModelService.buildDmnTable(info)
    }

}