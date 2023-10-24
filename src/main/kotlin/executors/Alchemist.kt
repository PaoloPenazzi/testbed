package executors

import it.unibo.alchemist.Alchemist

class AlchemistDriver {
    fun runAlchemist() {
        println("starting Alchemist...")
        Alchemist.main(arrayOf("run", "./src/main/yaml/simulation.yml"))
    }
}