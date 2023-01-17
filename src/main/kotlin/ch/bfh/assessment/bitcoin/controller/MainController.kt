package ch.bfh.assessment.bitcoin.controller

import ch.bfh.assessment.bitcoin.model.BitCoinData
import ch.bfh.assessment.bitcoin.model.History
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import tornadofx.Controller
import tornadofx.asObservable
import java.net.URL

const val url = "https://api.coindesk.com/v1/bpi/currentprice.json"

class MainController : Controller() {

    val historyObservableList = mutableListOf<History>().asObservable()

    fun getBitCoinData(): BitCoinData {
        val mapper = jacksonObjectMapper()
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        val json = URL(url).readText()
        return mapper.readValue(json)
    }

}