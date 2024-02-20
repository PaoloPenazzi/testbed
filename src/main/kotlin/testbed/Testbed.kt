package testbed

import controller.ControllerImpl

/**
 * Execute the benchmark.
 * @param args the path to the YAML file
 */
fun main(args: Array<String>) {
    args.forEach { println(it) }
    /*
    when (args.size) {
        0 -> throw IllegalArgumentException("No input file provided")
        1 -> println("[TESTBED] Starting..")
        else -> throw IllegalArgumentException("Too many arguments")
    }
     */
    val controller = ControllerImpl()
    // controller.run(args[0])
    controller.run("./src/main/yaml/case-study-benchmark.yml")
}
