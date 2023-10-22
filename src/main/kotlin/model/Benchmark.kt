package model

interface Benchmark {
    val simulator: Simulator
    val input: String
    fun withSimulator(simulator: Simulator): Benchmark
    fun withInput(input: String): Benchmark
}

data class BenchmarkImpl(override val simulator: Simulator = SimulatorImpl(),
                         override val input: String = ""): Benchmark {

    override fun withSimulator(simulator: Simulator): Benchmark = copy(simulator = simulator)

    override fun withInput(input: String): Benchmark = copy(input = input)
}