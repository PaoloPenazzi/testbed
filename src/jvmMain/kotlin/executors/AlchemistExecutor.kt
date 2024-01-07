package executors

import model.Scenario

class AlchemistExecutor : Executor {
    override fun getCommand(scenario: Scenario): ProcessBuilder {
        return ProcessBuilder("java", "-jar", "Alchemist.jar", "run", scenario.input).redirectErrorStream(true)
    }
}