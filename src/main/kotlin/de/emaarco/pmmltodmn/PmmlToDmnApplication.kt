package de.emaarco.pmmltodmn

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PmmlToDmnApplication {

	fun main(args: Array<String>) {
		runApplication<PmmlToDmnApplication>(*args)
	}

}
