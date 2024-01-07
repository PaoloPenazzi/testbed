package executors

import java.io.File

class NetLogoExecutor : Executor {
    override fun getCommand(input: String): ProcessBuilder {
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
