package de.emaarco.pmmltodmn.domain.model.tree

data class DataField(
    val name: String,
    val dataType: String,
    val opType: String,
    val isTarget: Boolean,
)