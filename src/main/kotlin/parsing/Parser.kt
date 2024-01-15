package parsing

import com.charleskorn.kaml.Yaml
import model.Benchmark
import java.io.File

interface Parser {
    fun parse(path: String): Benchmark
}

class ParserImpl : Parser {
    /**
     * Parses the YAML file containing the benchmark.
     *
     * @param path the path to the YAML file
     * @return the benchmark
     */
    override fun parse(path: String): Benchmark {
        val inputFile = File(path)
        val benchmark = Yaml.default.decodeFromString(Benchmark.serializer(), inputFile.readText())
        benchmark.simulators.forEach { simulator ->
            if (simulator.name == "Alchemist") {
                val configFileHandler = AlchemistConfigFileHandler()
                simulator.scenarios.forEach { scenario ->
                    configFileHandler.editConfigurationFile(scenario)
                }
            }
        }
        return benchmark
    }
}
