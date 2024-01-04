package listeners

import com.opencsv.CSVReader
import java.io.File
import java.io.FileReader
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

interface NetLogoListener : Listener

class NetLogoListenerImpl : NetLogoListener {
    override fun readCsv(path: String): Metric {
        cleanCSV(path)
        return getValues(path)
    }

    private fun cleanCSV(inputFilePath: String) {
        val lines = Files.readAllLines(Paths.get(inputFilePath), StandardCharsets.UTF_8)
        repeat(6) {
            lines.removeFirst()
        }
        Files.write(Paths.get(inputFilePath), lines, StandardCharsets.UTF_8)
    }
}