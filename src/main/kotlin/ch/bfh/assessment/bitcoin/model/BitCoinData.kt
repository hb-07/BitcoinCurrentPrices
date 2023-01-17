package ch.bfh.assessment.bitcoin.model

data class BitCoinData(
    val time: TimeStamp,
    val disclaimer: String,
    val chartName: String,
    val bpi: Bpi
   )
