package executors

import model.Scenario
import java.io.File

class NetLogoExecutor : Executor {
    override fun getCommand(scenario: Scenario): ProcessBuilder {
        val netLogoPath = "./NetLogo_Console"
        val modelPath = "./models/IABM Textbook/chapter 4/Wolf Sheep Simple 5.nlogo"
        val setupFilePath = "../src/commonMain/resources/netlogo/netlogo-tutorial.xml"
        val netlogoFolder = File("NetLogo 6.4.0")

        return ProcessBuilder(
            netLogoPath,
            "--headless",
            "--model",
            modelPath,
            "--setup-file",
            setupFilePath,
            "--table",
            "../export.csv"
        )
            .directory(netlogoFolder)
            .redirectErrorStream(true)
    }
}
