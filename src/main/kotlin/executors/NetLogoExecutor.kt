package executors

import model.Scenario
import java.io.File

/**
 * An executor for NetLogo.
 */
class NetLogoExecutor : Executor {
    override fun getCommand(simulatorPath: String, scenario: Scenario): ProcessBuilder {
        return ProcessBuilder(
            "./NetLogo_Console",
            "--headless",
            "--model",
            scenario.modelPath,
            "--setup-file",
            scenario.input,
            "--table",
            "../export.csv",
        )
            .directory(File(simulatorPath))
            .redirectErrorStream(true)
    }
}
