package reader

interface AlchemistResultReader : ResultReader

class AlchemistResultReaderImpl : AlchemistResultReader {
    override fun read() {
        println("Reading CSV")
    }
}