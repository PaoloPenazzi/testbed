package model

interface Simulator {
    val name: String
    val version: String
    fun withName(name: String): Simulator
    fun withVersion(version: String): Simulator
}

data class SimulatorImpl(override val name: String = "", override val version: String = ""): Simulator {

    override fun withName(name: String): Simulator = copy(name = name)

    override fun withVersion(version: String): Simulator = copy(version = version)
}