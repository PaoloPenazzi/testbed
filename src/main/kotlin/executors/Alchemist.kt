package executors

import java.io.BufferedReader
import java.io.InputStreamReader

// curl -L https://github.com/AlchemistSimulator/Alchemist/releases/download/29.0.0/alchemist-full-29.0.0-all.jar > Alchemist.jar
class Alchemist: SimulatorExecutor {
    override fun run(input: String) {
        try {
            val processBuilder = ProcessBuilder("java", "-jar", "Alchemist.jar", "run", input)
            processBuilder.redirectErrorStream(true)

            val process = processBuilder.start()
            val reader = BufferedReader(InputStreamReader(process.inputStream))

            var line: String?
            while (reader.readLine().also { line = it } != null) {
                println(line)
            }
            process.waitFor()
            println("[TESTBED] Alchemist test run finished")

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}