package executors

import model.Scenario
import java.io.File

/**
 * An executor for Alchemist.
 */
class AlchemistExecutor : Executor {
    override fun getCommand(simulatorPath: String, scenario: Scenario): ProcessBuilder {
<<<<<<< HEAD
        return ProcessBuilder("java", "-jar", "Alchemist.jar", "run", scenario.input)
=======
        return ProcessBuilder("java", "-jar", "Alchemist.jar", "run", scenario.input[0])
>>>>>>> b71c7d5 (fix: add broken files)
            .directory(File(simulatorPath))
            .redirectErrorStream(true)
    }
}
