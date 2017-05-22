#!/bin/sh

echo Creating the Project FIS-YasumiPuzzler
oc new-project yasumi --display-name="Yasumi Puzzler" --description="Yasumi Puzzle Solver Project"

echo Adding the FIS templates
BASEURL=https://raw.githubusercontent.com/jboss-fuse/application-templates/GA
oc replace --force -n openshift -f ${BASEURL}/fis-image-streams.json
oc replace --force -n openshift -f ${BASEURL}/quickstarts/karaf2-camel-amq-template.json
oc replace --force -n openshift -f ${BASEURL}/quickstarts/karaf2-camel-log-template.json
oc replace --force -n openshift -f ${BASEURL}/quickstarts/karaf2-camel-rest-sql-template.json
oc replace --force -n openshift -f ${BASEURL}/quickstarts/karaf2-cxf-rest-template.json
oc replace --force -n openshift -f ${BASEURL}/quickstarts/spring-boot-camel-template.json
oc replace --force -n openshift -f ${BASEURL}/quickstarts/spring-boot-camel-amq-template.json
oc replace --force -n openshift -f ${BASEURL}/quickstarts/spring-boot-camel-config-template.json
oc replace --force -n openshift -f ${BASEURL}/quickstarts/spring-boot-camel-drools-template.json
oc replace --force -n openshift -f ${BASEURL}/quickstarts/spring-boot-camel-infinispan-template.json
oc replace --force -n openshift -f ${BASEURL}/quickstarts/spring-boot-camel-rest-sql-template.json
oc replace --force -n openshift -f ${BASEURL}/quickstarts/spring-boot-camel-teiid-template.json
oc replace --force -n openshift -f ${BASEURL}/quickstarts/spring-boot-camel-xml-template.json
oc replace --force -n openshift -f ${BASEURL}/quickstarts/spring-boot-cxf-jaxrs-template.json
oc replace --force -n openshift -f ${BASEURL}/quickstarts/spring-boot-cxf-jaxws-template.json

echo Adding the policy rule
oc policy add-role-to-user view --serviceaccount=default

echo Adding the Deployment Files
oc create -f YasumiPuzzleStarter/src/main/fabric8/yasumipuzzler.yaml
oc create -f YasumiPuzzleBox/src/main/fabric8/yasumipuzzleboxhandler.yaml
oc create -f YasumiPuzzleCalculator/src/main/fabric8/yasumipuzzleboxcalculator.yaml

#Make sure java and maven are in your classpath!!!!
echo deploying the PuzzleStarter
mvn -f YasumiPuzzleStarter/pom.xml clean fabric8:deploy

echo deploying the PuzzleBox
mvn -f YasumiPuzzleBox/pom.xml clean fabric8:deploy

echo deploying the PuzzleCalculator
mvn -f YasumiPuzzleCalculator/pom.xml clean fabric8:deploy
