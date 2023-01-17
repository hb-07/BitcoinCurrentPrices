package bitcoin

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.net.URL

const val url = "https://api.coindesk.com/v1/bpi/currentprice.json"

fun main() {
    println(getBitCoinData())
}

data class TimeStamps(
    val updated: String = "",
    val updatedISO: String = "",
    val updateduk: String = ""
)

data class Currency(
    val code: String = "",
    val symbol: String = "",
    val rate: String = "",
    val description: String = "",
    val rate_float: Float = 0.0F
)

data class Bpi(
    val USD: Currency,
    val GBP: Currency,
    val EUR: Currency
)

data class BitCoinData(
    val time: TimeStamps,
    val disclaimer: String,
    val chartName: String,
    val bpi: Bpi
)

fun getBitCoinData(): BitCoinData {
    val mapper = jacksonObjectMapper()
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    val json = URL(url).readText()
    //println(json)
    return mapper.readValue(json)
}
