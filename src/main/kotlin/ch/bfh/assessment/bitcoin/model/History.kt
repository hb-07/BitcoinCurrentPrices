package ch.bfh.assessment.bitcoin.model

data class History(
    val date:String,
    val rateUsd: Float,
    val rateEur: Float,
    val rateGbp: Float
)
