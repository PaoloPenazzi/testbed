package listeners

import com.opencsv.CSVReader
import model.ScenarioOutput
import java.io.File
import java.io.FileReader

/**
 * The interface for the listener component.
 * The listener is responsible for reading the CSV file containing the simulation results.
 */
interface Listener {
    /**
     * Reads the CSV file containing the simulation results.
     *
     * @param path The path of the CSV file.
     * @return A map with the column names as keys and the values as a list of values.
     */
    fun read(path: String = ""): ScenarioOutput {
        val csvDataMap = linkedMapOf<String, List<String>>()

        clearCSV(path)

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
                        @Suppress("UnsafeCallOnNullableType")
                        csvDataMap[headers[i]] = csvDataMap[headers[i]]!! + record!![i]
                    } else {
                        csvDataMap[headers[i]] = csvDataMap[headers[i]]!! + "" // Empty string for missing values
                    }
                }
            }
        }
        File(path).delete()
        return csvDataMap
    }

    /**
     * Clears the CSV file containing the simulation results.
     * Remove any extra lines from the CSV file and format each line to make the parsing easier.
     *
     * @param outputFilePath The path of the CSV file.
     */
    fun clearCSV(outputFilePath: String)
}
