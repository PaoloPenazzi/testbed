import controller.ControllerImpl

/**
 * Execute the benchmark.
 * @param args the path to the YAML file
 */
fun main(args: Array<String>) {
    args.forEach { _ -> println() }
    val controller = ControllerImpl()
    controller.run("./src/main/yaml/benchmarks/multiple-scenarios.yml")
}
