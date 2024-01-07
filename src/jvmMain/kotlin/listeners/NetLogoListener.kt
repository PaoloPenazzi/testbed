package listeners

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

interface NetLogoListener : Listener

class NetLogoListenerImpl : NetLogoListener {
    override fun clearCSV(outputFilePath: String) {
        var lines = Files.readAllLines(Paths.get(outputFilePath), StandardCharsets.UTF_8)
        repeat(6) {
            lines.removeFirst()
        }
        Files.write(Paths.get(outputFilePath), lines, StandardCharsets.UTF_8)
    }
}