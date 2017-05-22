# FIS-Yasumi

![alt text](https://github.com/tdeborge/FIS-Yasumi/blob/master/src/site/images/pieces.png "Introduction puzzle box")

## Introduction

Back in 2001 I wrote a Java Application that was able to calculate all solutions to a Standard Yasumi Puzzle. In those days (on a Pentium III) it took about 20 minutes to calculate
all possible solutions.

After many years I decided to get the program back out and test it on my current hardware Macbook Pro i7 and found the program is now taking under 3 minutes (without any changes).

I then took the program and created some Fuse Services around it and ported that to the FIS environment running on OpenShift ... Let us see if we can beat the 3 minutes by scaling out ...

![alt text](https://github.com/tdeborge/FIS-Yasumi/blob/master/src/site/images/solutionarchitecture.png "Scale-out image")

## Pre-Requisites

This environment will need the following items

* CDK 2.4 (this is what is tested ... you can see how it works on your OCP environment)
* An installation of Fuse/AMQ. The Message Broker is used to glue all services together and is not included in this project
* Make the Broker available on 0.0.0.0:[port#]

## Getting Started

* Clone/Fork the repository using the browser or on the commandline:

**git clone https://github.com/tdeborge/FIS-Yasumi.git**

---

* Then modify the following file:

**configureCDK.sh**

---

* In this file, you find reference to the OpenShift IP address your services need to connect to -- so please adjust this:

```
OCP_IP=192.168.2.71
```

---

* Edit the configureCDK.sh file so it reflects the environment you want to setup. The script is handling the following:

1. Setup of a new project in the OCP environment
1. Adding the FIS images to the environment
1. Adding a default View Policy
1. Uploading the ConfigMaps that hold all environment variables into the project
1. When java/maven/repos are configured correctly on your system, it will deploy the pods in the environment.

** Validation

Once the script is finished, you can validate the following:

![alt text](https://github.com/tdeborge/FIS-Yasumi/blob/master/src/site/images/ocppods.png "OpenShift Console View")

There should be 3 Services deployed

* yasumipuzzlestarter
* yasumipuzzlebox
* yasumipuzzlecalculator (with 2 pods)

![alt text](https://github.com/tdeborge/FIS-Yasumi/blob/master/src/site/images/amqView.png "AMQ Queue Creation")

In the AMQ/Fuse console, you will find the following queues created:

* qa.test.yasumi.forwardEntry
* qa.test.yasumi.puzzlebox.start
* qa.test.yasumi.start

** Reaching this step, and all items created, you are ready to move to the next step (Making the services work)**

## The Application

In the cloned environment, open the file:

**FISGuiScaleOut/src/main/resources/resources/block.properties**

In this file, you will be able to insert the BROKERURL for your AMQ connection. Also make sure that the entry queue definition is the same as the definition in the YasumyPuzzleStarter setting.

	blocks.jms.username				= admin
	blocks.jms.password				= admin
	blocks.jms.url					= tcp://localhost:61616
	blocks.jms.destination			= qa.test.yasumi.start

**Build the application**

	mvn clean install
	
**Run the applictation**

in the target directory:

    java -jar FISGuiScaleOut-1.0.0-SNAPSHOT-jar-with-dependencies.jar

![alt text](https://github.com/tdeborge/FIS-Yasumi/blob/master/src/site/images/blockguiinit.png "Init state of puzzler")

**Click on the 'New Interactive' Button** to send a start message to the Broker. The application will create a Temporary Queue to retrieve the results and close it when all solutions are received.

![alt text](https://github.com/tdeborge/FIS-Yasumi/blob/master/src/site/images/tempqueueview.png "Temporary Queue View")

At the end, you can scroll through the solutions that were found.

![alt text](https://github.com/tdeborge/FIS-Yasumi/blob/master/src/site/images/blockguisolution.png "blockguisolution")




