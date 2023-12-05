package parsing

import com.charleskorn.kaml.Yaml
import model.Benchmark
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import org.yaml.snakeyaml.Yaml as SnakeYaml

interface Parser {
    fun parse(path: String): Benchmark
}

class ParserImpl : Parser {
    override fun parse(path: String): Benchmark {
        val inputFile = File(path)
        val benchmark = Yaml.default.decodeFromString(Benchmark.serializer(), inputFile.readText())
        // Call the method insert duration for each Alchemist scenario that does have a duration != 0
        benchmark.simulators.forEach { simulator ->
            if (simulator.name == "Alchemist") {
                simulator.scenarios.forEach { scenario ->
                    if (scenario.duration != 0) {
                        insertDuration(scenario.input, scenario.duration)
                    }
                }
            }
        }

        return benchmark
    }

    private fun insertDuration(inputPath: String, duration: Int) {
        val yaml = SnakeYaml()
        val inputFile = FileReader(inputPath)
        val data: MutableMap<String, Any> = yaml.load(inputFile)
        // Update the value
        data.remove("terminate")

        // Add a new "terminate" part with the correct duration
        val newTerminate = mapOf(
            "type" to "AfterTime",
            "parameters" to duration
        )
        data["terminate"] = listOf(newTerminate)

        // Save the updated YAML file
        val outputFile = FileWriter(inputPath)
        yaml.dump(data, outputFile)

        // Close the files
        inputFile.close()
        outputFile.close()
    }
}