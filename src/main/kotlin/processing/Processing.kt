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
    val timeValues = benchmarkOutput["Alchemist-SAPERE-gradient-1"]!!["[time"]
    val timeValuesDouble =
        timeValues?.map { it.toString().toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble() }!!
    println()
    println("Time values: $timeValuesDouble")
    val gradientValues =
        benchmarkOutput["Alchemist-SAPERE-gradient-1"]!!["Noooooooooooooooooooooooooooooooooooooooooooo[mean]]"]
    val gradientValuesNoNaN = gradientValues?.map { it.toString().replace("NaN", "0") }
    val gradientValuesDouble =
        gradientValuesNoNaN?.map { it.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble() }!!
    println("Gradient values: $gradientValuesDouble")
    println()
    val timeToStabilize = gradientValuesDouble.reversed().foldRight(Triple(0.00, 0, 0)) { value, acc ->
        if (acc.second == 0) {
            if (value == acc.first && value != 0.00) {
                Triple(
                    acc.first,
                    gradientValuesDouble.indexOf(acc.first),
                    acc.third - 1,
                )
            } else {
                Triple(value, 0, acc.third + 1)
            }
        } else {
            acc
        }
    }
    return listOf(
        ScenarioResult(
            "Time to stabilize: ",
            listOf(timeValuesDouble[timeToStabilize.third]),
            VisualisationType.SINGLE_VALUE,
        ),
    )
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
