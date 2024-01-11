package model

import kotlinx.serialization.Serializable

typealias Metric = Map<String, List<Any>>
typealias BenchmarkOutput = Map<String, Metric>

data class Result(val description: String,
                  val value: List<Any>,
                  val visualisationType: VisualisationType)

typealias Output = List<Result>

enum class VisualisationType {
    SINGLE_VALUE,
    LIST_OF_VALUES
}


@Serializable
data class Benchmark(val strategy: Strategy,
                     val simulators: List<Simulator>)

@Serializable
data class Strategy(val multiThreaded: Boolean = false,
                    val executionOrder: List<String> = emptyList())

@Serializable
data class Simulator(val name: String,
                     val simulatorPath: String = "",
                     val scenarios: List<Scenario>)

@Serializable
data class Scenario(val name: String,
                    val description: String = "",
                    val input: String = "",
                    val modelPath : String = "",
                    val repetitions: Int = 1,
                    val duration: Int = 0)
