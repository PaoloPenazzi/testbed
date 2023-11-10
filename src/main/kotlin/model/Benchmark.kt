package model

import kotlinx.serialization.Serializable

@Serializable
data class Benchmark(val strategy: Strategy,
                     val simulators: List<Simulator>)

infix fun Benchmark.addSimulator(simulator: Simulator): Benchmark {
    return this.copy(simulators = simulators + simulator)
}

@Serializable
data class Strategy(val multiThreaded: Boolean = false,
                    val executionOrder: List<String> = emptyList())

@Serializable
data class Simulator(val name: String,
                     val version: String,
                     val scenarios: List<Scenario>) {

    infix fun addScenario(scenario: Scenario): Simulator {
        return this.copy(scenarios = scenarios + scenario)
    }
}



infix fun Simulator.removeScenario(scenario: Scenario): Simulator {
    return this.copy(scenarios = scenarios - scenario)
}

@Serializable
data class Scenario(val name: String,
                    val description: String,
                    val input: String = "",
                    val scenarioConfiguration: String = "",
                    val programs: List<Program> = emptyList())

infix fun Scenario.withInput(input: String): Scenario {
    return this.copy(input = input)
}

infix fun Scenario.withPrograms(programs: List<Program>): Scenario {
    return this.copy(programs = programs)
}

infix fun Scenario.withName(name: String): Scenario {
    return this.copy(name = name)
}

fun Scenario.removeProgram(program: Program): Scenario {
    return this.copy(programs = programs - program)
}

@Serializable
data class Program(val name: String,
                   val description: String,
                   val input: String)
