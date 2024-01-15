package listeners

import com.opencsv.CSVReader
import model.ScenarioOutput
import java.io.File
import java.io.FileReader

interface Listener {

    /**
     * Reads the CSV file containing the simulation results.
     *
     * @param path The path of the CSV file.
     * @return A map with the column names as keys and the values as a list of values.
     */
    fun readCsv(path: String): ScenarioOutput {
        val csvDataMap = mutableMapOf<String, MutableList<Any>>()

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
                        csvDataMap[headers[i]]?.add(record!![i])
                    } else {
                        csvDataMap[headers[i]]?.add("") // Empty string for missing values
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
