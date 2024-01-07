package executors

class AlchemistExecutor : Executor {
    override fun getCommand(input: String): ProcessBuilder {
        return ProcessBuilder("java", "-jar", "Alchemist.jar", "run", input).redirectErrorStream(true)
    }
}