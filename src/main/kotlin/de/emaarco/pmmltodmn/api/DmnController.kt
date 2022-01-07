package de.emaarco.pmmltodmn.api

import de.emaarco.pmmltodmn.domain.facade.DmnFacade
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/")
internal class DmnController(private val dmnFacade: DmnFacade) {

    @PostMapping
    fun buildDmnModel(response: HttpServletResponse, @RequestPart("tree") rawPmmlTree: MultipartFile): ResponseEntity<ByteArrayResource> {
        response.contentType = "application/xml"
        return ResponseEntity.ok().body(dmnFacade.buildDmnModel(rawPmmlTree))
    }

}