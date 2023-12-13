package reader

import com.opencsv.CSVReader
import java.io.FileReader
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

interface AlchemistResultReader : ResultReader

class AlchemistResultReaderImpl : AlchemistResultReader {
    override fun read(path: String) {
        clearCSV(path)
        formatHeaders(path)
        val values = getValues(path)
    }

    private fun clearCSV(path: String) {
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

    private fun formatHeaders(path: String) {
        val lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8)
        val modifiedLines = lines.map { line ->
            val modifiedLine = line.replace(Regex("# "), "")
            modifiedLine
        }
        Files.write(Paths.get(path), modifiedLines, StandardCharsets.UTF_8)
    }

    private fun getValues(path: String): Map<String, List<String>> {
        val csvDataMap = mutableMapOf<String, MutableList<String>>()

        FileReader(path).use { fileReader ->
            val csvReader = CSVReader(fileReader)
            val headers = csvReader.readNext()
            headers.forEach { columnName ->
                csvDataMap[columnName] = mutableListOf()
            }
            var record: Array<String>?
            while (csvReader.readNext().also { record = it } != null) {
                for (i in headers.indices) {
                    if (i < record!!.size) {
                        csvDataMap[headers[i]]?.add(record!![i])
                    } else {
                        csvDataMap[headers[i]]?.add("") // Empty string for missing values
                    }
                }
            }
        }
        return csvDataMap
    }
}