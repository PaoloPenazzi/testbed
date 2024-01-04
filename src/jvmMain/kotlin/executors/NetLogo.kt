package executors

import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class NetLogo: SimulatorExecutor {
    override fun run(input: String) {
        val netLogoPath = "./NetLogo_Console"
        val modelPath = "./models/IABM Textbook/chapter 4/Wolf Sheep Simple 5.nlogo"
        val setupFilePath = "../src/commonMain/resources/netlogo/netlogo-tutorial.xml"
        val netlogoFolder = File("NetLogo 6.4.0")

        try {
            val processBuilder = ProcessBuilder(netLogoPath, "--headless", "--model", modelPath, "--setup-file", setupFilePath, "--table", "export.csv")
                .directory(netlogoFolder)
                .redirectErrorStream(true)

            val process = processBuilder.start()
            val reader = BufferedReader(InputStreamReader(process.inputStream))

            var line: String?
            while (reader.readLine().also { line = it } != null) {
                println(line)
            }
            process.waitFor()
            println("[TESTBED] NetLogo test run finished")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
