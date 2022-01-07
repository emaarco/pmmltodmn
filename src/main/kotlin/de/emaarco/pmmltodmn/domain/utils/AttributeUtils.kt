package de.emaarco.pmmltodmn.domain.utils

import java.util.*

object AttributeUtils {

    fun getVariableName(attributeName: String): String {
        return attributeName
            .lowercase(Locale.getDefault())
            .replace(" ".toRegex(), "_")
    }

}