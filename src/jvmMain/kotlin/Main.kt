import controller.ControllerImpl
import reader.AlchemistResultReader
import reader.AlchemistResultReaderImpl

fun main(args: Array<String>) {
    // Run the controller
    args.forEach { println(it) }
    val controller = ControllerImpl()
    controller.run("./src/commonMain/yaml/benchmarks/test-duration.yml")


    // ./NetLogo-6.4.0-64/NetLogo_Console --headless --model "models/IABM Textbook/chapter 4/Wolf Sheep Simple 5.nlogo" --setup-file src/main/resources/netlogo/netlogo-tutorial.xml --experiment "netlogo-tutorial" --table output.csv
}