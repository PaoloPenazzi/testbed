package listeners

import com.opencsv.CSVReader
import java.io.FileReader
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

interface AlchemistListener : Listener

class AlchemistListenerImpl : AlchemistListener {
    override fun readCsv(path: String): Metric {
        cleanCSV(path)
        formatCSV(path)
        return getValues(path)
    }

    // Remove from the Alchemist output all the excess lines before parsing the .csv file.
    private fun cleanCSV(path: String) {
        val lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8)
        val regexPatterns = listOf(
            Regex("#.*#"),
            Regex("#$"),
            Regex("# $"),
            Regex("# T.*")
        )
        val modifiedLines = lines.filter { line ->
            !regexPatterns.any { pattern -> pattern.matches(line) }
        }
        Files.write(Paths.get(path), modifiedLines, StandardCharsets.UTF_8)
    }

    // Format each line of the .csv file to make the parsing easier.
    private fun formatCSV(path: String) {
        val lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8)
        val modifiedLines = lines.map { line ->
            val modifiedLine = line.replace(Regex("# "), "")
            modifiedLine
        }
        Files.write(Paths.get(path), modifiedLines, StandardCharsets.UTF_8)
    }
}