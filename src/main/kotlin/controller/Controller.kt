package controller

import executors.Executor
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import listeners.Listener
import model.*
import parsing.ParserImpl
import processing.process
import view.View
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

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
    private var benchmarkResult: BenchmarkResult = listOf()

    override fun run(inputPath: String) {
        val parser = ParserImpl()
        require(isFilePathValid(inputPath)) { "Input file not found. No input file was found at $inputPath" }
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
            }.toMap()
        scenarioNameOrder.forEach { scenarioName ->
            val (simulator, scenario, repetitions) = scenarioMap.getOrElse(scenarioName) {
                throw IllegalArgumentException("Scenario $scenarioName not found")
            }
            for (i in 1..repetitions) {
                runBlocking {
                    println("[TESTBED] Running scenario $scenarioName in ${simulator.name} simulator. Run number $i")
                    createExecutor(simulator.name, simulator.simulatorPath, scenario)
                    readSimulatorOutput(simulator, scenario, i)
                }
            }
        }
        if (benchmarkOutput.isNotEmpty()) {
            benchmarkResult = benchmarkResult + process(benchmarkOutput)
        } else {
            benchmarkResult = process(benchmarkOutput)
        }
        val view: View = view.ViewImpl(benchmarkResult)
        view.render()
    }

    private fun readSimulatorOutput(simulator: Simulator, scenario: Scenario, repetition: Int) {
        if (scenario.postProcessing.isEmpty()) {
            val reader = createReader(simulator.name)
            val runName = "${scenario.name}-$repetition"
            val metric = reader.read(simulator.simulatorPath + "export.csv")
            benchmarkOutput = benchmarkOutput + mapOf(runName to metric)
        } else {
            val processBuilder = ProcessBuilder(scenario.postProcessing).redirectErrorStream(true)
            val process = processBuilder.start()
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                println(line)
            }
            process.waitFor()
            val file = File("./result.json")
            val scenarioResult = Json.decodeFromString<ScenarioResult>(file.readText())
            benchmarkResult = benchmarkResult + scenarioResult
        }
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
                scenario.input.forEach { input ->
                    require(isFilePathValid(simulator.simulatorPath + input)) {
                        "Input path not found. No input was found at " + simulator.simulatorPath + input
                    }
                }
            }
        }
    }
}
