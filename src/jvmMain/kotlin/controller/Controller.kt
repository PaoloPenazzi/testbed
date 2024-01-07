package controller

import executors.Executor
import model.Benchmark
import parsing.ParserImpl
import kotlinx.coroutines.*
import listeners.Metric
import listeners.Listener

interface Controller {
    fun run(inputPath: String)
}

class ControllerImpl : Controller {
    private var output: Map<String, Metric> = mapOf()

    override fun run(inputPath: String) {
        val parser = ParserImpl()
        val benchmark = parser.parse(inputPath)
        executeBenchmark(benchmark)
    }

    // Tricky method that simply calls createExecutor() in the right order and the right number of times
    private fun executeBenchmark(benchmark: Benchmark) {
        val scenarioNameOrder: List<String> = benchmark.strategy.executionOrder
        val scenarioMap: Map<String, Triple<String, String, Int>> = benchmark.simulators
            .flatMap { simulator ->
                simulator.scenarios.map { scenario ->
                    scenario.name to Triple(simulator.name, scenario.input, scenario.repetitions)
                }
            }
            .toMap()
        scenarioNameOrder.forEach { scenarioName ->
            val (simulatorName, inputPath) = scenarioMap[scenarioName]!!
            for (i in 1..scenarioMap[scenarioName]!!.third) {
                runBlocking {
                    createExecutor(simulatorName, inputPath)
                    val reader = createReader(simulatorName)
                    val runName = "$scenarioName-$i"
                    val metric = reader.readCsv("./export.csv")
                    output = output + mapOf(runName to metric)
                }
            }
        }
        println("[TESTBED] Output: $output")
    }

    private fun createExecutor(simulatorName: String, inputPath: String) {
        val driver: Executor = when (simulatorName) {
            "Alchemist" -> executors.AlchemistExecutor()
            "NetLogo" -> executors.NetLogoExecutor()
            else -> throw IllegalArgumentException("Simulator $simulatorName not found")
        }
        println("[TESTBED] Running $simulatorName")
        driver.run(inputPath)
    }

    private fun createReader(simulatorName: String): Listener {
        val reader: Listener = when (simulatorName) {
            "Alchemist" -> listeners.AlchemistListenerImpl()
            "NetLogo" -> listeners.NetLogoListenerImpl()
            else -> throw IllegalArgumentException("Simulator $simulatorName not found")
        }
        return reader
    }
}