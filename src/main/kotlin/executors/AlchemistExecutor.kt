package executors

import model.Scenario
import java.io.File

class AlchemistExecutor : Executor {
    override fun getCommand(simulatorPath: String, scenario: Scenario): ProcessBuilder {
        return ProcessBuilder("java", "-jar", "Alchemist.jar", "run", scenario.input)
            .directory(File(simulatorPath))
            .redirectErrorStream(true)
    }
}
