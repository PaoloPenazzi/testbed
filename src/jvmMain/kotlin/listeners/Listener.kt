package listeners

import com.opencsv.CSVReader
import java.io.FileReader

typealias Metric = Map<String, List<Any>>

interface Listener {
    fun readCsv(path: String): Metric

    fun getValues(path: String): Metric {
        val csvDataMap = mutableMapOf<String, MutableList<Any>>()

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
        println(csvDataMap)
        return csvDataMap
    }
}