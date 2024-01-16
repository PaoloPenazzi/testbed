package processing

import model.BenchmarkOutput
import model.BenchmarkResult
import model.ScenarioResult
import model.VisualisationType
import java.math.RoundingMode

@Suppress("UndocumentedPublicProperty")
val process: (BenchmarkOutput) -> BenchmarkResult = { benchmarkOutput ->
    val averageNumberOfWolvesPerRun = averageNumberOfWolvesPerRun(benchmarkOutput)

    // Alchemist processing
    val alchemistMetric = benchmarkOutput["Alchemist-Protelis-1"]
    val timeCounter = alchemistMetric?.get("time ")
    val timeResult = ScenarioResult("Alchemist time steps: ", timeCounter.orEmpty(), VisualisationType.LIST_OF_VALUES)

    // Return the list of results
    listOf(averageNumberOfWolvesPerRun, timeResult)
}

private fun averageNumberOfWolvesPerRun(benchmarkOutput: BenchmarkOutput): ScenarioResult {
    val netlogoRunsNumber = 3
    var wolvesAverageList = listOf<Double>()
    for (i in 1..netlogoRunsNumber) {
        val wolvesCounter = benchmarkOutput["NetLogo-Tutorial-$i"]?.get("count wolves")
        val wolvesAverage = wolvesCounter.orEmpty().map {
            when (it) {
                is Int -> it
                is String -> it.toInt()
                else -> throw IllegalArgumentException("Unexpected type")
            }
        }.average()
        wolvesAverageList =
            wolvesAverageList + wolvesAverage.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
    }
    return ScenarioResult("Average number of wolves per run: ", wolvesAverageList, VisualisationType.LIST_OF_VALUES)
}
