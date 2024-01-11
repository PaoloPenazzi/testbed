package parsing

import model.Scenario
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

interface ConfigFileHandler {
    fun editConfigurationFile(scenario: Scenario) {}
}

class AlchemistConfigFileHandler : ConfigFileHandler {
    override fun editConfigurationFile(scenario: Scenario) {
        if (scenario.duration != 0) {
            insertDuration(scenario.input, scenario.duration)
        }
    }

    private fun insertDuration(inputPath: String, duration: Int) {
        val yaml = Yaml()
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