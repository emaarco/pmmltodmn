package de.emaarco.pmmltodmn.domain.model.dmn

data class DmnModelRequest(
    val dmnModelID: String,
    val dmnModelName: String,
    val dmnDecisionID: String,
    val dmnDecisionName: String,
)