package ch.bfh.assessment.bitcoin.app

import ch.bfh.assessment.bitcoin.controller.MainController
import ch.bfh.assessment.bitcoin.model.History
import javafx.application.Platform
import javafx.geometry.Pos
import javafx.scene.control.TableView
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import javafx.stage.Stage
import kotlinx.coroutines.*
import tornadofx.*

class MyApp : App(BitcoinAppView::class, Style::class)

class BitcoinAppView: View("Bitcoin prices") {

    private val mainController: MainController by inject()
    private var historyView = mainController.historyObservableList
    private var initialValueUSD = 0.0f
    private var initialValueGBP = 0.0f
    private var initialValueEUR = 0.0f
    private val usdDifference = label("")
    private val gbpDifference = label("")
    private val eurDifference = label("")

    //private var differenceLabel: Label by singleAssign()

    override val root = borderpane{
        //define minimum window size
        setMinSize(300.0,500.0)


        runBlocking {
            launch (Dispatchers.Default + SupervisorJob()){
                while (true ) {
                    //get the Bitcoin data
                    val data = mainController.getBitCoinData()

                    //update GUI async
                    Platform.runLater {
                        //creates an instance object of History class
                        val historyObject = History(
                            data.time.updated,
                            data.bpi.USD.rate_float,
                            data.bpi.EUR.rate_float,
                            data.bpi.GBP.rate_float
                        )

                        // add historyObject into observable list
                        historyView.add(historyObject)
                        //compute the usd difference_rate between the current and the last prices
                        if (initialValueUSD == 0.0f || initialValueEUR == 0.0f || initialValueGBP == 0.0f) {
                            initialValueUSD = historyObject.rateUsd
                            initialValueEUR = historyObject.rateEur
                            initialValueGBP = historyObject.rateGbp
                            usdDifference.text = "0.0"
                            gbpDifference.text = "0.0"
                            eurDifference.text = "0.0"
                        }else {
                            val usdDiff = historyObject.rateUsd - initialValueUSD
                            val eurDiff = historyObject.rateEur - initialValueEUR
                            val gbpDiff = historyObject.rateGbp - initialValueGBP

                            usdDifference.text = "$usdDiff"
                            eurDifference.text = "$eurDiff"
                            gbpDifference.text = "$gbpDiff"
                            if (usdDiff < 0 || eurDiff < 0 || gbpDiff < 0) {
                                usdDifference.textFill = c("#C7370F")
                                eurDifference.textFill = c("#C7370F")
                                gbpDifference.textFill = c("#C7370F")

                            }else if (usdDiff > 0 || eurDiff > 0 || gbpDiff > 0) {
                                usdDifference.textFill = c("#00A86B")
                                eurDifference.textFill = c("#00A86B")
                                gbpDifference.textFill = c("#00A86B")

                            }else {
                                usdDifference.textFill = c("#000000")
                                eurDifference.textFill = c("#000000")
                                gbpDifference.textFill = c("#000000")

                            }
                            initialValueUSD = historyObject.rateUsd
                            initialValueGBP = historyObject.rateGbp
                            initialValueEUR = historyObject.rateEur
                        }
                        //differenceLabel.text = difference.toString()
                        print("usdDifference_Rate: ${usdDifference.text}")
                        print("\teurDifference_Rate: ${eurDifference.text}")
                        print("\tgbpDifference_Rate: ${gbpDifference.text}")
                        println()

                    }

                    //update prices in every minute
                    delay(60000)
                }
            }
        }


        top {

            vbox {
                spacing = 20.0
                hbox {
                    alignment = Pos.CENTER
                    label("Difference_Rate") {
                        style {
                            fontWeight = FontWeight.BOLD
                            fontSize = 15.px
                            textFill = Color.DARKBLUE
                        }
                    }
                }
                hbox {
                    alignment = Pos.CENTER
                    spacing = 100.0
                    vbox {
                        label("USD "){style{textFill = Color.BLUE; fontWeight = FontWeight.BOLD}}
                        add(usdDifference)
                    }
                    vbox {
                        label("EUR "){style{textFill = Color.BLUE; fontWeight = FontWeight.BOLD}}
                        add(eurDifference)
                    }
                    vbox {
                        label("GBP "){style{textFill = Color.BLUE; fontWeight = FontWeight.BOLD}}
                        add(gbpDifference)
                    }

                }
                hbox {
                    alignment = Pos.CENTER

                    label("CURRENT PRICES") {
                        style {
                            fontWeight = FontWeight.BOLD
                            fontSize = 18.px
                            fontFamily = "arial"
                        }
                    }
                }
            }

        }

        center {
            tableview(historyView) {
                readonlyColumn("Date", History::date)
                readonlyColumn("USD", History::rateUsd)
                readonlyColumn("EUR", History::rateEur)
                readonlyColumn("GBP", History::rateGbp)
                columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY

                vboxConstraints {
                    vGrow = Priority.ALWAYS
                }
            }
        }

        bottom {

            hbox {
                alignment = Pos.CENTER
                label("data retrieved from \"https://api.coindesk.com/v1/bpi/currentprice.json\"")
            }
        }
    }

}

fun main() {
    launch<MyApp>()
}