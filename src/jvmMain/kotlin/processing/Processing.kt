package processing

import model.BenchmarkOutput
import model.Output
import model.Result
import model.VisualisationType

val process: (BenchmarkOutput) -> Output = { benchmarkOutput ->
    // NetLogo processing
    val netlogoMetric = benchmarkOutput["NetLogo-Tutorial-1"]
    val wolvesCounter = netlogoMetric?.get("count wolves")
    val wolvesAverage = wolvesCounter.orEmpty().map {
        when (it) {
            is Int -> it
            is String -> it.toInt()
            else -> throw IllegalArgumentException("Unexpected type")
        }
    }.average()
    val wolfResult = Result("Average number of wolves: ", listOf(wolvesAverage), VisualisationType.SINGLE_VALUE)

    // Alchemist processing
    val alchemistMetric = benchmarkOutput["Alchemist-Protelis-1"]
    val timeCounter = alchemistMetric?.get("time ")
    val timeResult = Result("Time steps done: ", timeCounter.orEmpty(), VisualisationType.LIST)

    // Return the list of results
    listOf(wolfResult, timeResult)
}