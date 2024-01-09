package parsing

import model.Scenario
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import javax.xml.parsers.DocumentBuilderFactory


interface ConfigFileHandler {
    fun editConfigurationFile(scenario: Scenario)
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

class NetLogoConfigFileHandler : ConfigFileHandler {
    override fun editConfigurationFile(scenario: Scenario) {
        //insertDuration(scenario.input, scenario.duration)
    }

    private fun insertDuration(inputPath: String, duration: Int) {
        // insert the duration in the .xml configuration file, inside the <timeLimit steps="2000"/> tag

        val modifiedFilePath = inputPath.replace("..", ".")

        val document = parseXmlFile(modifiedFilePath)


        // Modify the value of timeLimit steps
        modifyTimeLimitSteps(document, duration)

    }

    private fun parseXmlFile(filePath: String): Document {

        val factory = DocumentBuilderFactory.newInstance()
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
        val builder = factory.newDocumentBuilder()
        return builder.parse(File(filePath))
    }

    private fun modifyTimeLimitSteps(document: Document, newValue: Int) {
        val experimentsNode = document.getElementsByTagName("experiments").item(0)
        if (experimentsNode.nodeType == Node.ELEMENT_NODE) {
            val experimentElement = experimentsNode as Element
            val timeLimitNode = experimentElement.getElementsByTagName("timeLimit").item(0) as Element
            val stepsAttr = timeLimitNode.getAttributeNode("steps")
            stepsAttr.nodeValue = newValue.toString()
        }
    }
}