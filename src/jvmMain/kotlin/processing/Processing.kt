package processing

import model.BenchmarkOutput
import model.Output
import model.Result
import model.VisualisationType
import java.math.RoundingMode

val process: (BenchmarkOutput) -> Output = { benchmarkOutput ->
    val averageNumberOfWolvesPerRun = averageNumberOfWolvesPerRun(benchmarkOutput)

    // Alchemist processing
    val alchemistMetric = benchmarkOutput["Alchemist-Protelis-1"]
    val timeCounter = alchemistMetric?.get("time ")
    val timeResult = Result("Alchemist time steps: ", timeCounter.orEmpty(), VisualisationType.LIST_OF_VALUES)

    // Return the list of results
    listOf(averageNumberOfWolvesPerRun, timeResult)
}

fun averageNumberOfWolvesPerRun(benchmarkOutput: BenchmarkOutput): Result {
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
        wolvesAverageList = wolvesAverageList + wolvesAverage.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
    }
    return Result("Average number of wolves per run: ", wolvesAverageList, VisualisationType.LIST_OF_VALUES)
}