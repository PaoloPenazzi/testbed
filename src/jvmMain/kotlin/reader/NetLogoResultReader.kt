package reader

interface NetLogoResultReader : ResultReader

class NetLogoResultReaderImpl : NetLogoResultReader {
    override fun read() {
        println("Reading CSV")
    }
}