import controller.ControllerImpl

fun main(args: Array<String>) {
    // Run the controller
    val controller = ControllerImpl()
    controller.run("./src/main/yaml/testbed-input.yml")
}