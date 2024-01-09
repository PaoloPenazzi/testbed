package parsing

import com.charleskorn.kaml.Yaml
import model.Benchmark
import java.io.File

interface Parser {
    fun parse(path: String): Benchmark
}

class ParserImpl : Parser {
    override fun parse(path: String): Benchmark {
        val inputFile = File(path)
        val benchmark = Yaml.default.decodeFromString(Benchmark.serializer(), inputFile.readText())
        benchmark.simulators.forEach { simulator ->
            val configFileHandler = when (simulator.name) {
                "Alchemist" -> AlchemistConfigFileHandler()
                "NetLogo" -> NetLogoConfigFileHandler()
                else -> throw IllegalArgumentException("Simulator ${simulator.name} not found")
            }
            simulator.scenarios.forEach { scenario ->
                configFileHandler.editConfigurationFile(scenario)
            }
        }
        return benchmark
    }
}