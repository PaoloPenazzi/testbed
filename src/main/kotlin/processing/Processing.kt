package processing

import model.BenchmarkOutput
import model.BenchmarkResult
import model.ScenarioResult
import view.VisualisationType
import java.math.RoundingMode

@Suppress("UndocumentedPublicProperty")
val process: (BenchmarkOutput) -> BenchmarkResult = { benchmarkOutput ->
    caseStudyProcessing(benchmarkOutput)
}

private fun caseStudyProcessing(benchmarkOutput: BenchmarkOutput): BenchmarkResult {
    println(benchmarkOutput)
    return emptyList()
}

private fun benchmarkProcessing(benchmarkOutput: BenchmarkOutput): BenchmarkResult {
    val averageNumberOfWolvesPerRun = benchmarkProcessingNetlogo(benchmarkOutput)
    val timeResult = benchmarkProcessingAlchemist(benchmarkOutput)
    return listOf(averageNumberOfWolvesPerRun, timeResult)
}

private fun benchmarkProcessingAlchemist(benchmarkOutput: BenchmarkOutput): ScenarioResult {
    val alchemistMetric = benchmarkOutput["Alchemist-Protelis-1"]
    println(alchemistMetric)
    val timeCounter = alchemistMetric?.get("time ").orEmpty()
    val timeCounterDouble = timeCounter.map { it.toString().toDouble() }
    return ScenarioResult("Alchemist time steps: ", timeCounterDouble, VisualisationType.LIST_OF_VALUES)
}

private fun benchmarkProcessingNetlogo(benchmarkOutput: BenchmarkOutput): ScenarioResult {
    val netlogoRunsNumber = 3
    var wolvesAverageList = listOf<Double>()
    for (i in 1..netlogoRunsNumber) {
        val wolvesCounter = benchmarkOutput["NetLogo-Tutorial-$i"]?.get("count wolves")
        val wolvesAverage = wolvesCounter.orEmpty().map { it.toString().toInt() }.average()
        wolvesAverageList =
            wolvesAverageList + wolvesAverage.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
    }
    return ScenarioResult("Average number of wolves per run: ", wolvesAverageList, VisualisationType.LIST_OF_VALUES)
}
