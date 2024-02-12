package integration

import executors.AlchemistExecutor
import executors.Executor
import io.kotest.core.spec.style.FreeSpec
import model.Scenario

class AlchemistTest : FreeSpec({
    "Alchemist" - {
        val executor: Executor = AlchemistExecutor()
        "should run a simulation with Protelis incarnation" {
            val scenario: Scenario = Scenario(
                name = "Alchemist-Protelis-Test",
                description = "Testing the Protelis incarnation in Alchemist",
                input = listOf("src/test/kotlin/integration/yaml/protelis-tutorial.yml"),
            )
            executor.run("./", scenario)
        }
        "should run a simulation with Sapere incarnation" {
            val scenario: Scenario = Scenario(
                name = "Alchemist-Sapere-Test",
                description = "Testing the Sapere incarnation in Alchemist",
                input = listOf("src/test/kotlin/integration/yaml/sapere-tutorial.yml"),
            )
            executor.run("./", scenario)
        }
    }
})
