package view

import model.Output
import model.VisualisationType

class View(output: Output) {
    init {
        output.forEach { result ->
            when (result.visualisationType) {
                VisualisationType.SINGLE_VALUE -> println(result.description + result.value[0])
                VisualisationType.LIST -> println(result.description + result.value)
                VisualisationType.LINE_CHART -> println(result.description + result.value)
            }
        }
    }
}