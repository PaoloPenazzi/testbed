package executors

import model.Scenario
import java.io.BufferedReader
import java.io.InputStreamReader

interface Executor {
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

    fun getCommand(simulatorPath: String, scenario: Scenario): ProcessBuilder
}