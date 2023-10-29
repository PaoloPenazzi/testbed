package executors

import it.unibo.alchemist.Alchemist

class Alchemist: SimulatorExecutor {
    override fun run(input: String) {
        println("starting Alchemist...")
        println("Alchemist path $input")
        Alchemist.main(arrayOf("run", input))
    }
}