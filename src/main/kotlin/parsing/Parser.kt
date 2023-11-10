package parsing

import com.charleskorn.kaml.Yaml
import model.*
import java.io.File
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Paths
import org.yaml.snakeyaml.Yaml as SnakeYaml

interface Parser {
    fun parse(path: String): Benchmark
}

class ParserImpl : Parser {
    override fun parse(path: String): Benchmark {
        val inputFile = File(path)
        val benchmark = Yaml.default.decodeFromString(Benchmark.serializer(), inputFile.readText())
        return addProgramsToScenarios(benchmark)
    }

    private fun addProgramsToScenarios(benchmark: Benchmark): Benchmark {
        return benchmark.copy(
            simulators = benchmark.simulators.map { simulator ->
                if (simulator.name == "Alchemist") {
                    simulator.copy(
                        scenarios = simulator.scenarios.fold(emptyList()) { acc, scenario ->
                            if (scenario.input == "" && scenario.programs.isNotEmpty()) {
                                acc + scenario.programs.map { program ->
                                    val scenarioPath = "src/main/yaml/scenarios/${scenario.name}.yml"
                                    val programPath = "src/main/yaml/programs/${program.name}.yml"
                                    mergeYamls(scenarioPath, programPath)
                                    val inputPath = "src/main/yaml/generated/${scenario.name}-${program.name}.yml"
                                    Scenario(
                                        name = "${scenario.name}-${program.name}",
                                        description = scenario.description,
                                        scenarioConfiguration = scenario.scenarioConfiguration,
                                        input = inputPath,
                                        programs = emptyList()
                                    )
                                }
                            } else {
                                acc
                            }
                        }
                    )
                } else {
                    simulator
                }
            }
        )
    }

    private fun mergeYamls(scenarioPath: String, programPath: String) {
        val yaml = SnakeYaml()
        val scenarioName = scenarioPath.split("/").last().split(".").first()
        val programName = programPath.split("/").last().split(".").first()
        val scenarioYAML = String(Files.readAllBytes(Paths.get(scenarioPath)))
        val programYAML = String(Files.readAllBytes(Paths.get(programPath)))
        val scenarioData: LinkedHashMap<String, Any> = yaml.load(scenarioYAML)
        val programData: List<LinkedHashMap<String, Any>> = yaml.load(programYAML)
        val programsSection: LinkedHashMap<String, Any> = LinkedHashMap()
        programsSection["programs"] = programData
        if (scenarioData.containsKey("deployments")) {
            (scenarioData["deployments"] as LinkedHashMap<String, Any>?)?.putAll(programsSection)
        }
        println("Final $scenarioData")
        val writer = FileWriter("src/main/yaml/generated/$scenarioName-$programName.yml")
        yaml.dump(scenarioData, writer)
    }
}