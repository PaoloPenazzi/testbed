package executors

import java.io.BufferedReader
import java.io.InputStreamReader

interface Executor {
    fun run(input: String) {
        try {
            val processBuilder = getCommand(input)

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

    fun getCommand(input: String): ProcessBuilder
}