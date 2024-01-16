package listeners

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

/**
 * An implementation of the Listener interface for NetLogo.
 */
class NetLogoListener : Listener {
    @Suppress("MagicNumber")
    private val numbersOfLineToSkip = 6

    override fun clearCSV(outputFilePath: String) {
        val lines = Files.readAllLines(Paths.get(outputFilePath), StandardCharsets.UTF_8)
        val modifiedLines = lines.drop(numbersOfLineToSkip)
        Files.write(Paths.get(outputFilePath), modifiedLines, StandardCharsets.UTF_8)
    }
}
