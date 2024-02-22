package unit

import com.charleskorn.kaml.InvalidPropertyValueException
import io.kotest.core.spec.style.FreeSpec
import model.Benchmark
import model.Scenario
import model.Simulator
import model.Strategy
import model.SupportedSimulator
import org.junit.jupiter.api.assertThrows
import parsing.Parser
import parsing.ParserImpl

class ParserTest : FreeSpec({
    "The parser" - {
        val parser: Parser = ParserImpl()
        "should parse a partial input file" {
            // ARRANGE
            val expectedBenchmark = simpleBenchmarkBuilder()
            // ACT
            val benchmark = parser.parse("src/test/resources/SimpleBenchmark.yml")
            // ASSERT
            assert(benchmark == expectedBenchmark)
        }
        "should parse a full input file" {
            val expectedBenchmark = fullBenchmarkBuilder()
            val benchmark = parser.parse("src/test/resources/FullBenchmark.yml")
            assert(benchmark == expectedBenchmark)
        }
        "should fail to parse a wrong file" {
            assertThrows<InvalidPropertyValueException> {
                parser.parse("src/test/resources/WrongBenchmark.yml")
            }
        }
    }
})

private fun simpleBenchmarkBuilder(): Benchmark {
    val strategy: Strategy = Strategy(
        executionOrder = listOf("Alchemist-test"),
    )
    val scenario: Scenario = Scenario(
        name = "Alchemist-test",
        description = "Testing the parser",
        input = listOf("protelis-tutorial.yml"),
    )
    val simulator: Simulator = Simulator(
        name = SupportedSimulator.ALCHEMIST,
        simulatorPath = "./",
        scenarios = listOf(scenario),
    )
    val benchmark: Benchmark = Benchmark(
        strategy = strategy,
        simulators = listOf(simulator),
    )
    return benchmark
}

private fun fullBenchmarkBuilder(): Benchmark {
    val strategy: Strategy = Strategy(
        executionOrder = listOf("Alchemist-Protelis", "NetLogo-Tutorial", "Alchemist-Sapere"),
    )
    val protelisScenario: Scenario = Scenario(
        name = "Alchemist-Protelis",
        description = "A tutorial to Alchemist and Protelis incarnation",
        input = listOf("./src/main/yaml/protelis-tutorial.yml"),
        postProcessing = "",
        repetitions = 1,
        duration = 10,
    )
    val netlogoScenario: Scenario = Scenario(
        name = "NetLogo-Tutorial",
        description = "A tutorial to NetLogo",
        input = listOf("./netlogo/netlogo-tutorial.xml", "./models/wolf-sheep.nlogo"),
        postProcessing = "",
        repetitions = 3,
    )
    val sapereScenario: Scenario = Scenario(
        name = "Alchemist-Sapere",
        description = "A tutorial to Alchemist and Sapere incarnation",
        input = listOf("./src/main/yaml/sapere-gradient.yml"),
        postProcessing = "",
        repetitions = 5,
        duration = 100,
    )
    val alchemistSimulator: Simulator = Simulator(
        name = SupportedSimulator.ALCHEMIST,
        simulatorPath = "./",
        scenarios = listOf(protelisScenario, sapereScenario),
    )
    val netlogoSimulator: Simulator = Simulator(
        name = SupportedSimulator.NETLOGO,
        simulatorPath = "./NetLogo 6.4.0/",
        scenarios = listOf(netlogoScenario),
    )
    val benchmark: Benchmark = Benchmark(
        strategy = strategy,
        simulators = listOf(netlogoSimulator, alchemistSimulator),
    )
    return benchmark
}
