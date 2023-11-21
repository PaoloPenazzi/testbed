package model

import kotlinx.serialization.Serializable

@Serializable
data class Benchmark(val strategy: Strategy,
                     val simulators: List<Simulator>)

@Serializable
data class Strategy(val multiThreaded: Boolean = false,
                    val executionOrder: List<String> = emptyList())

@Serializable
data class Simulator(val name: String,
                     val scenarios: List<Scenario>)

@Serializable
data class Scenario(val name: String,
                    val description: String,
                    val input: String = "",
                    val scenarioConfiguration: String = "",
                    val programs: List<Program> = emptyList(),
                    val repetitions: Int = 1,
                    val duration: Int = 100)

@Serializable
data class Program(val name: String,
                   val description: String,
                   val input: String)
