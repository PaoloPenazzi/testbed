package listeners

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

/**
 * An implementation of the Listener interface for Alchemist.
 */
class AlchemistListener : Listener {
    override fun clearCSV(outputFilePath: String) {
        removeExtraLines(outputFilePath)
        formatLines(outputFilePath)
    }

    // Remove from the Alchemist output all the excess lines before parsing the .csv file.
    private fun removeExtraLines(path: String) {
        val lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8)
        val regexPatterns = listOf(
            Regex("#.*#"),
            Regex("#$"),
            Regex("# $"),
            Regex("# T.*"),
        )
        val modifiedLines = lines.filter { line ->
            !regexPatterns.any { pattern -> pattern.matches(line) }
        }
        Files.write(Paths.get(path), modifiedLines, StandardCharsets.UTF_8)
    }

    // Format each line of the .csv file to make the parsing easier.
    private fun formatLines(path: String) {
        val lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8)
        val modifiedLines = lines.map { line ->
            val modifiedLine = line.replace(Regex("# "), "")
            modifiedLine
        }
        val finalLines = modifiedLines.map { line ->
            if (modifiedLines.indexOf(line) == 0) {
                val headers = line.split(" ").filter {
                    it.contains("time") || it.contains("[")
                }
                headers
            } else {
                line
            }
        }
        val commaLines = finalLines.map { line ->
            println(line)
            val doubleSpace = line.toString().replace(Regex(", "), ",")
            println(doubleSpace)
            val modifiedLine = doubleSpace.replace(Regex(" "), ",")
            println(modifiedLine)
            modifiedLine
        }
        Files.write(Paths.get(path), commaLines, StandardCharsets.UTF_8)
    }
}
