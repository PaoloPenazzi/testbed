package model

import kotlinx.serialization.Serializable

interface Benchmark {
    val simulator: Simulator
    val input: String
}

@Serializable
data class BenchmarkImpl(override val simulator: SimulatorImpl, override val input: String): Benchmark