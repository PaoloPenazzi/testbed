package model

import kotlinx.serialization.Serializable

/**
 * The benchmark is the main data structure of the testbed. It contains all the information needed to run a benchmark.
 * @param strategy the strategy used to run the benchmark
 * @param simulators the list of simulators to be used in the benchmark
 */
@Serializable
data class Benchmark(
    val strategy: Strategy,
    val simulators: List<Simulator>,
)

/**
 * The strategy defines how the benchmark is run.
 * @param multiThreaded if true, the benchmark is run in parallel
 * @param executionOrder the order in which the scenarios are run
 */
@Serializable
data class Strategy(
    val multiThreaded: Boolean = false,
    val executionOrder: List<String> = emptyList(),
)

/**
 * A simulator is a software that can be used to run a scenario.
 * @param name the name of the simulator
 * @param simulatorPath the path to the simulator
 * @param scenarios the list of scenarios that can be run with the simulator
 */
@Serializable
data class Simulator(
    val name: SupportedSimulator,
    val simulatorPath: String = "",
    val scenarios: List<Scenario>,
)

/**
 * The supported simulators.
 */
enum class SupportedSimulator {
    ALCHEMIST,
    NETLOGO,
}

/**
 * A scenario contains all the information needed to run a simulation.
 * @param name the name of the scenario. This parameter is mandatory.
 * @param description a description of the scenario
 * @param input the input file of the scenario
 * @param modelPath the path to the model used in the scenario. This is needed only for NetLogo simulations.
 * @param repetitions the number of times the scenario is run. Default is 1.
 * @param duration the duration of the simulation.
 */
@Serializable
data class Scenario(
    val name: String,
    val description: String = "",
    val input: String = "",
    val modelPath: String = "",
    val repetitions: Int = 1,
    val duration: Int = 0,
)

/**
 * A ScenarioOutput is a data structure that contains the output of a scenario.
 * The key are the name of the values, the values are the list of values.
 *
 * Example:
 * "time frames" -> [1, 2, 3, 4, 5]
 * "distanceTo" -> [20, 15, 14, 13, 13]
 */
typealias ScenarioOutput = Map<String, List<Any>>

/**
 * A BenchmarkOutput is a data structure that contains the output of the benchmark.
 * It maps each scenario to its output.
 *
 * Example:
 * "NetLogo-Tutorial-1" -> ScenarioOutput
 * "NetLogo-Tutorial-2" -> ScenarioOutput
 * "Alchemist-Protelis-1" -> ScenarioOutput
 */
typealias BenchmarkOutput = Map<String, ScenarioOutput>

/**
 * A ScenarioResult is a data structure that contains the result of a scenario.
 * The result means the processed output of a scenario.
 * @param description the description of the result
 * @param value the value of the result
 * @param visualisationType the visualisation type of the result
 *
 * Example:
 * ("Average number of wolves per run: ", [9.2, 8.7, 9.4], LIST_OF_VALUES)
 */
data class ScenarioResult(
    val description: String,
    val value: List<Any>,
    val visualisationType: VisualisationType,
)

/**
 * The visualisation type defines how the output of a benchmark is visualised.
 * SINGLE_VALUE the output is a single value
 * LIST_OF_VALUES the output is a list of values
 */
enum class VisualisationType {
    SINGLE_VALUE,
    LIST_OF_VALUES,
}

/**
 * A BenchmarkResult is a data structure that contains the result of a benchmark.
 * It is a list of ScenarioResult.
 */
typealias BenchmarkResult = List<ScenarioResult>
