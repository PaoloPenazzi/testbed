package controller

import model.Benchmark
import parsing.ParserImpl

interface Controller {
    fun run(inputPath: String)
}

class ControllerImpl : Controller {
    override fun run(inputPath: String) {
        val parser = ParserImpl()
        val benchmark = parser.parse(inputPath)
        createExecutor(benchmark)
    }

    private fun createExecutor(benchmark: Benchmark) {
        when (benchmark.simulator.name) {
            "Alchemist" -> {
                val alchemistDriver = executors.AlchemistDriver()
                alchemistDriver.runAlchemist()
            }
            "NS3" -> {
                println("Starting NS-3...")
            }
            else -> {
                println("Simulator not supported")
            }
        }
    }
}