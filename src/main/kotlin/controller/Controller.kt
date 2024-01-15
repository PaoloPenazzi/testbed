package controller

import executors.Executor
import kotlinx.coroutines.runBlocking
import listeners.Listener
import model.Benchmark
import model.BenchmarkOutput
import model.Scenario
import model.Simulator
import parsing.ParserImpl
import processing.process
import view.View

/**
 * The testbed controller.
 */
interface Controller {
    /**
     * Runs the benchmark.
     * @param inputPath the path to the YAML file
     */
    fun run(inputPath: String)
}

/**
 * The implementation of the testbed controller.
 */
class ControllerImpl : Controller {
    private var benchmarkOutput: BenchmarkOutput = mapOf()

    override fun run(inputPath: String) {
        val parser = ParserImpl()
        val benchmark = parser.parse(inputPath)
        executeBenchmark(benchmark)
    }

    // Tricky method that simply calls createExecutor() in the right order and the right number of times
    private fun executeBenchmark(benchmark: Benchmark) {
        val scenarioNameOrder: List<String> = benchmark.strategy.executionOrder
        val scenarioMap: Map<String, Triple<Simulator, Scenario, Int>> = benchmark.simulators
            .flatMap { simulator ->
                simulator.scenarios.map { scenario ->
                    scenario.name to Triple(simulator, scenario, scenario.repetitions)
                }
            }
            .toMap()
        scenarioNameOrder.forEach { scenarioName ->
            val (simulator, scenario, repetitions) = scenarioMap.getOrElse(scenarioName) {
                throw IllegalArgumentException("Scenario $scenarioName not found")
            }
            for (i in 1..repetitions) {
                runBlocking {
                    createExecutor(simulator.name, simulator.simulatorPath, scenario)
                    val reader = createReader(simulator.name)
                    val runName = "$scenarioName-$i"
                    val metric = reader.readCsv("./export.csv")
                    benchmarkOutput = benchmarkOutput + mapOf(runName to metric)
                }
            }
        }
        val output = process(benchmarkOutput)
        val view: View = view.ViewImpl(output)
        view.render()
    }

    private fun createExecutor(simulatorName: String, simulatorPath: String, scenario: Scenario) {
        val executor: Executor = when (simulatorName) {
            "Alchemist" -> executors.AlchemistExecutor()
            "NetLogo" -> executors.NetLogoExecutor()
            else -> throw IllegalArgumentException("Simulator $simulatorName not found")
        }
        executor.run(simulatorPath, scenario)
    }

    private fun createReader(simulatorName: String): Listener {
        val reader: Listener = when (simulatorName) {
            "Alchemist" -> listeners.AlchemistListener()
            "NetLogo" -> listeners.NetLogoListener()
            else -> throw IllegalArgumentException("Simulator $simulatorName not found")
        }
        return reader
    }
}
