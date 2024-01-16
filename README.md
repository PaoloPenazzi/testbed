# Testbed

This is an open benchmarking platform for Collective Adaptive Systems.

## Requirements

The Testbed requires you to have Java 8 installed on your machine.

## Installation
The standard way to run the Testbed is by using the executable jar file. 
You can download the latest version from the [releases](https://github.com/PaoloPenazzi/testbed/releases) page.

To execute a benchmark issue the following command:
```java -jar testbed.jar <path_to_config_file>```

An example of the command is:
```java -jar testbed.jar ./resources/benchmark.yml```

## Configuration file
The configuration file is a YAML file that contains all the information needed to run a benchmark.
This is an example of a configuration file:
```yaml
strategy:
  executionOrder:
    - Alchemist-Protelis
    - NetLogo-Tutorial

simulators:
  - name: NETLOGO
    simulatorPath: "./NetLogo 6.4.0"
    scenarios:
      - name: NetLogo-Tutorial
        description: A tutorial to NetLogo
        input: "../src/main/resources/netlogo/netlogo-tutorial.xml"
        modelPath: "./models/IABM Textbook/chapter 4/Wolf Sheep Simple 5.nlogo"
        repetitions: 3

  - name: ALCHEMIST
    simulatorPath: "./"
    scenarios:
      - name: Alchemist-Protelis
        description: A tutorial to Alchemist and Protelis incarnation
        input: "src/main/resources/alchemist/protelis-tutorial.yml"
        repetitions: 1
        duration: 10
```
As you can see, there are two main sections: strategy and simulators.
The strategy section contains the information about the execution strategy of the benchmark: for now, the only available option is the execution order.
Please note that the execution order is a list of scenario names, not simulator names.

The simulators section contains the information about the simulators that will be used in the benchmark.
Each simulator has a name, a path to the simulator executable, and a list of scenarios.

Each scenario has a name, a description, a path to the input file, the number of repetitions.
There are also two optional parameters: the model path, which is used by NetLogo to load the model, and the duration, which is used by Alchemist to set the simulation duration.
Please note that the path needs to be relative to the simulator executable.

## Extending the testbed
If you want to add support for a new simulator you will need to do a few things:
- Add the simulator's name to the 'SupportedSimulator' enum in the benchmark model.
- Add the Supported Simulator in the Controller class (in both match cases).
- Implement the Executor interface for the simulator.
- Implement the Listener interface for the simulator.
- Implement the ConfigFileHandler interface and add the corresponding case to the Parser (not mandatory).

## Issues
In case you encounter any issues with the testbed, please open an issue on GitHub, specifying the problem and the steps to reproduce it.

## Contributing
If you want to contribute to the project, please fork the repository, implement your changes and submit a pull request.