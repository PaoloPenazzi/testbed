package parsing

import com.charleskorn.kaml.Yaml
import model.Benchmark
import model.SupportedSimulator
import java.io.File

/**
 * The interface for the parser component, which is responsible for parsing the YAML file containing the benchmark.
 */
interface Parser {
    /**
     * Parses the YAML file into a benchmark.
     *
     * @param path the path to the YAML file
     * @return the benchmark
     */
    fun parse(path: String): Benchmark
}

/**
 * The implementation of the parser component.
 */
class ParserImpl : Parser {
    override fun parse(path: String): Benchmark {
        val inputFile = File(path)
        val benchmark = Yaml.default.decodeFromString(Benchmark.serializer(), inputFile.readText())
        benchmark.simulators.forEach { simulator ->
            if (simulator.name == SupportedSimulator.ALCHEMIST) {
                val configFileHandler = AlchemistConfigFileHandler()
                simulator.scenarios.forEach { scenario ->
                    configFileHandler.editConfigurationFile(scenario)
                }
            }
        }
        return benchmark
    }
}
