package controller

import executors.SimulatorExecutor
import model.Benchmark
import parsing.ParserImpl

interface Controller {
    fun run(inputPath: String)
}

class ControllerImpl : Controller {
    override fun run(inputPath: String) {
        val parser = ParserImpl()
        val benchmark = parser.parse(inputPath)
        executeBenchmark(benchmark)
    }

    // Tricky method that simply calls createExecutor() in the right order
    private fun executeBenchmark(benchmark: Benchmark) {
        val scenarioNameOrder: List<String> = benchmark.strategy.executionOrder
        val scenarioMap: Map<String, Pair<String, String>> = benchmark.simulators
            .flatMap { simulator ->
                simulator.scenarios.map { scenario ->
                    scenario.name to (simulator.name to scenario.input)
                }
            }
            .toMap()
       scenarioNameOrder.forEach { scenarioName ->
           val (simulatorName, inputPath) = scenarioMap[scenarioName]!!
           createExecutor(simulatorName, inputPath)
       }
    }

    private fun createExecutor(simulatorName: String, inputPath: String) {
        val driver: SimulatorExecutor = when (simulatorName) {
            "Alchemist" -> executors.Alchemist()
            "NS3" -> executors.NS3()
            else -> throw IllegalArgumentException("Simulator $simulatorName not found")
        }
        driver.run(inputPath)
    }
}