package testbed

import controller.ControllerImpl

/**
 * Execute the benchmark.
 * @param args the path to the YAML file
 */
fun main(args: Array<String>) {
    val controller = ControllerImpl()
    controller.run(args[0])
}
