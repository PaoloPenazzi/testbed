import controller.ControllerImpl

fun main(args: Array<String>) {
    // Run the controller
    args.forEach { println(it) }
    val controller = ControllerImpl()
    controller.run("./src/commonMain/yaml/benchmarks/multiple-scenarios.yml")
    // curl -L https://github.com/AlchemistSimulator/Alchemist/releases/download/29.0.0/alchemist-full-29.0.0-all.jar > Alchemist.jar
}