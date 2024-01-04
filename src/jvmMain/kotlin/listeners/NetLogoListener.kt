package reader

import java.io.File
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

    private fun getValues(path: String): Metric {
        val csvDataMap = mutableMapOf<String, MutableList<Any>>()
        // Read the first line to get the headers
        // Read each column and add it to the map, with the header as key
        File(path).forEachLine { line ->
            val columns = line.split(",")
            columns.forEachIndexed { index, column ->
                if (index < columns.size) {
                    csvDataMap[columns[index]]?.add(column)
                }
            }
        }
        println(csvDataMap)
        return csvDataMap
    }
}