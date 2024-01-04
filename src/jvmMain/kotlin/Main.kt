import controller.ControllerImpl
import executors.NetLogo
import listeners.NetLogoListenerImpl

fun main(args: Array<String>) {
    // Run the controller
    args.forEach { println(it) }
    val controller = ControllerImpl()
    controller.run("./src/commonMain/yaml/benchmarks/multiple-scenarios.yml")

    // NetLogo test run
    //val sim = NetLogo()
    //sim.run("test")

    // NetLogo csv reader
    //val reader = NetLogoListenerImpl()
    //reader.readCsv("./NetLogo 6.4.0/export.csv")


    // ./NetLogo-6.4.0-64/NetLogo_Console --headless --model "models/IABM Textbook/chapter 4/Wolf Sheep Simple 5.nlogo" --setup-file src/main/resources/netlogo/netlogo-tutorial.xml --experiment "netlogo-tutorial" --table output.csv
}