package executors

import java.io.BufferedReader
import java.io.InputStreamReader

class NetLogo: SimulatorExecutor {
    override fun run(input: String) {
        val netLogoPath = "NetLogo 6.2.0/netlogo-gui.sh"

        // Replace this with the path to your NetLogo model
        val modelPath = "NetLogo 6.2.0/app/models/Sample Models/Earth Science/Fire.nlogo"

        try {
            val processBuilder = ProcessBuilder(netLogoPath, "--model", modelPath)
            processBuilder.redirectErrorStream(true)

            val process = processBuilder.start()
            val reader = BufferedReader(InputStreamReader(process.inputStream))

            var line: String?
            while (reader.readLine().also { line = it } != null) {
                println(line)
            }

            val exitCode = process.waitFor()
            println("NetLogo process exited with code $exitCode")

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
