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

* Then position yourself in the directory and modify the following files:

**YasumiPuzzleStarter/src/main/fabric8/yasumipuzzler.yaml**

**YasumiPuzzleBox/src/main/fabric8/yasumipuzzleboxhandler.yaml**

**YasumiPuzzleCalculator/src/main/fabric8/yasumipuzzleboxcalculator.yaml**

---

* In these files, you find reference to the AMQ Broker URL your services need to connect to - so please adjust this to your AMQ environment:

```
Puzzler.amq.BrokerURL: tcp://192.168.2.71:61616
```

---

* Edit the configureCDK.sh file so it reflects to the environment you want to setup. The script is handling the following:

1. Setup of a new project in the OCP environment
1. Adding the FIS images to the environment
1. Adding a default View Policy
1. Uploading the ConfigMaps that hold all environment variables into the project
1. When java/maven/repos are configured correctly on your system, it will deploy the pods in the environment.