package com.sk.directudhar.ui.applyloan

data class BusinessDetailsResponseModel(
    val Msg: String,
    val Result: Boolean,
    val DynamicData: DynamicData
)

data class DynamicData(
    val LeadMasterId: Int,
    val SequenceNo: Int,

)