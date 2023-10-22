package model

import kotlinx.serialization.Serializable

@Serializable
data class Benchmark(val strategy: Strategy,
                     val simulators: List<Simulator>)

@Serializable
data class Strategy(val multiThreaded: Boolean,
                    val executionOrder: List<String>)

@Serializable
data class Simulator(val name: String,
                     val version: String,
                     val scenarios: List<Scenario>)

@Serializable
data class Scenario(val name: String,
                    val description: String,
                    val input: String)
