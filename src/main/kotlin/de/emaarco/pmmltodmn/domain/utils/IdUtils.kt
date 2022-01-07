package de.emaarco.pmmltodmn.domain.utils

import java.util.*

object IdUtils {

    fun buildRandomId(): String {
        return UUID.randomUUID().toString().replace("-".toRegex(), "")
    }

}