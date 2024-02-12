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
import java.io.File

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
<<<<<<< HEAD
        if (!isFilePathValid(inputPath)) {
            throw IllegalArgumentException("Input file not found. No input file was found at $inputPath")
        }
=======
        require(isFilePathValid(inputPath)) { "Input file not found. No input file was found at $inputPath" }
>>>>>>> b71c7d5 (fix: add broken files)
        println("[TESTBED] Parsing started")
        val benchmark = parser.parse(inputPath)
        println("[TESTBED] Parsing completed")
        checkPaths(benchmark)
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
                    println("[TESTBED] Running scenario $scenarioName in ${simulator.name} simulator. Run number $i")
                    createExecutor(simulator.name, simulator.simulatorPath, scenario)
                    val reader = createReader(simulator.name)
                    val runName = "$scenarioName-$i"
<<<<<<< HEAD
                    val metric = reader.readCsv(simulator.simulatorPath + "export.csv")
=======
                    val metric = reader.read(simulator.simulatorPath + "export.csv")
>>>>>>> b71c7d5 (fix: add broken files)
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

    private fun isFilePathValid(filePath: String): Boolean {
        val file = File(filePath)
        return file.exists()
    }

    // Check all the path in the benchmark
    private fun checkPaths(benchmark: Benchmark) {
        benchmark.simulators.forEach { simulator ->
            simulator.scenarios.forEach { scenario ->
<<<<<<< HEAD
                if (!isFilePathValid(simulator.simulatorPath + scenario.modelPath)) {
                    throw IllegalArgumentException("Model path not found. No model was found at ${scenario.modelPath}")
                }
                if (!isFilePathValid(simulator.simulatorPath + scenario.input)) {
                    throw IllegalArgumentException("Input path not found. No input was found at ${scenario.input}")
=======
                scenario.input.forEach { input ->
                    require(isFilePathValid(simulator.simulatorPath + input)) {
                        "Input path not found. No input was found at $input"
                    }
>>>>>>> b71c7d5 (fix: add broken files)
                }
            }
        }
    }
}
