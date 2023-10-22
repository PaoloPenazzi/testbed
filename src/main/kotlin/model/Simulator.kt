package model

import kotlinx.serialization.Serializable

interface Simulator {
    val name: String
    val version: String
}

@Serializable
data class SimulatorImpl(override val name: String, override val version: String): Simulator