package executors

import model.Scenario
import java.io.File

/**
 * An executor for Alchemist.
 */
class AlchemistExecutor : Executor {
    override fun getCommand(simulatorPath: String, scenario: Scenario): ProcessBuilder {
        return ProcessBuilder("java", "-jar", "Alchemist.jar", "run", scenario.input[0])
            .directory(File(simulatorPath))
            .redirectErrorStream(true)
    }
}
