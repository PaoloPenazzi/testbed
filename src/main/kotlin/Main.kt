import parsing.ParserImpl

fun main(args: Array<String>) {
    println("Hello World!")
    val parser = ParserImpl()
    val benchmark = parser.parse("src/main/yaml/testbed-input.yml")
    println(benchmark)
    // val alchemistDriver = AlchemistDriver()
    // alchemistDriver.runAlchemist()
}