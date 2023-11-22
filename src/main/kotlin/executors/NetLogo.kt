package executors

import java.io.BufferedReader
import java.io.InputStreamReader

class NetLogo: SimulatorExecutor {
    override fun run(input: String) {
        val netLogoPath = "NetLogo 6.2.0/netlogo-gui.sh"
        val modelPath = "NetLogo 6.2.0/app/models/Sample Models/Earth Science/Fire.nlogo"

        try {
            val processBuilder = ProcessBuilder(netLogoPath, modelPath)
            processBuilder.redirectErrorStream(true)

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
