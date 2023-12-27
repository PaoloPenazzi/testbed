package reader

interface NetLogoResultReader : ResultReader

class NetLogoResultReaderImpl : NetLogoResultReader {
    override fun read(path: String) {
        println("Reading CSV")
    }
}