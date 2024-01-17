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
 * @param executionOrder the order in which the scenarios are run
 */
@Serializable
data class Strategy(
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
 * @param modelPath the path to the model used in the scenario. Supported by NetLogo.
 * @param repetitions the number of times the scenario is run. Default is 1.
 * @param duration the duration of the simulation. Supported by Alchemist.
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
