package executors

import model.Scenario
import java.io.File

/**
 * An executor for NetLogo.
<<<<<<< HEAD
=======
 * Creates the following command:
 * ./NetLogo_Console --headless --model <model-file> --setup-file <setup-file> --table ./export.csv
 * where <model-file> is the second input file and <setup-file> is the first input file.
 * This command will be called from the folder where the simulator is located.
>>>>>>> b71c7d5 (fix: add broken files)
 */
class NetLogoExecutor : Executor {
    override fun getCommand(simulatorPath: String, scenario: Scenario): ProcessBuilder {
        return ProcessBuilder(
            "./NetLogo_Console",
            "--headless",
            "--model",
            scenario.input[1],
            "--setup-file",
            scenario.input[0],
            "--table",
            "./export.csv",
        )
            .directory(File(simulatorPath))
            .redirectErrorStream(true)
    }
}
