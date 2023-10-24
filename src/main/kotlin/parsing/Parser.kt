package parsing

import com.charleskorn.kaml.Yaml
import model.Benchmark
import java.io.File

interface Parser {
    fun parse(path: String): Benchmark
}

class ParserImpl: Parser {
    override fun parse(path: String): Benchmark {
        val inputFile = File(path)
        return Yaml.default.decodeFromString(Benchmark.serializer(), inputFile.readText())
    }
}