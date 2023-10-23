package executors

import it.unibo.alchemist.Alchemist

class AlchemistDriver {
    // a public method to call the Alchemist main method
    fun runAlchemist() {
        println("starting Alchemist...")
        Alchemist.main(arrayOf("run", "./src/main/yaml/simulation.yml"))
    }
}