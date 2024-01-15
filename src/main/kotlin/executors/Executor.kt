package executors

import model.Scenario
import java.io.BufferedReader
import java.io.InputStreamReader

interface Executor {
    /**
     * Runs the scenario.
     * @param simulatorPath the path to the simulator
     * @param scenario the scenario to run
     */
    fun run(simulatorPath: String, scenario: Scenario) {
        try {
            val processBuilder = getCommand(simulatorPath, scenario)

            val process = processBuilder.start()
            val reader = BufferedReader(InputStreamReader(process.inputStream))

            var line: String?
            while (reader.readLine().also { line = it } != null) {
                println(line)
            }
            process.waitFor()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Returns a ProcessBuilder with the command to run the simulation.
     * @param simulatorPath the path to the simulator
     * @param scenario the scenario to run
     */
    fun getCommand(simulatorPath: String, scenario: Scenario): ProcessBuilder
}
