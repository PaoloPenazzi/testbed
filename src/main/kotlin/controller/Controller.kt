package controller

import executors.SimulatorExecutor
import model.Benchmark
import parsing.ParserImpl
import kotlinx.coroutines.*

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
        val scenarioMap: Map<String, Triple<String, String, Int>> = benchmark.simulators
            .flatMap { simulator ->
                simulator.scenarios.map { scenario ->
                    scenario.name to Triple(simulator.name, scenario.input, scenario.repetitions)
                }
            }
            .toMap()
        println(scenarioMap)
        scenarioNameOrder.forEach { scenarioName ->
            val (simulatorName, inputPath) = scenarioMap[scenarioName]!!
            for (i in 1..scenarioMap[scenarioName]!!.third) {
                runBlocking { createExecutor(simulatorName, inputPath) }
            }
        }
    }

    private fun createExecutor(simulatorName: String, inputPath: String) {
        val driver: SimulatorExecutor = when (simulatorName) {
            "Alchemist" -> executors.Alchemist()
            "NS3" -> executors.NS3()
            "NetLogo" -> executors.NetLogo()
            else -> throw IllegalArgumentException("Simulator $simulatorName not found")
        }
        println("[TESTBED] Running $simulatorName")
        driver.run(inputPath)
    }
}