package controller

import executors.Executor
import kotlinx.coroutines.runBlocking
import listeners.Listener
import model.Benchmark
import model.BenchmarkOutput
import model.Scenario
import model.Simulator
import model.SupportedSimulator
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
        println("[Testbed] Parsing completed")
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
                    println("[Testbed] Running scenario $scenarioName in ${simulator.name} simulator. Run number $i")
                    createExecutor(simulator.name, simulator.simulatorPath, scenario)
                    val reader = createReader(simulator.name)
                    val runName = "$scenarioName-$i"
                    val metric = reader.readCsv(simulator.simulatorPath + "export.csv")
                    benchmarkOutput = benchmarkOutput + mapOf(runName to metric)
                }
            }
        }
        val output = process(benchmarkOutput)
        val view: View = view.ViewImpl(output)
        view.render()
    }

    private fun createExecutor(simulator: SupportedSimulator, simulatorPath: String, scenario: Scenario) {
        val executor: Executor = when (simulator) {
            SupportedSimulator.ALCHEMIST -> executors.AlchemistExecutor()
            SupportedSimulator.NETLOGO -> executors.NetLogoExecutor()
        }
        executor.run(simulatorPath, scenario)
    }

    private fun createReader(simulator: SupportedSimulator): Listener {
        val reader: Listener = when (simulator) {
            SupportedSimulator.ALCHEMIST -> listeners.AlchemistListener()
            SupportedSimulator.NETLOGO -> listeners.NetLogoListener()
        }
        return reader
    }
}
