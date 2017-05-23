#!/bin/sh

OCP_IP=192.168.64.2
echo "Setting OpenShift IP address to ${OCP_IP}"
#I think this line became obsolete once AMQ is also deployed into the OCP environment. This allows for having static referal to the correct Rout URL.
#sed -i s/TODO_SPECIFY_IP/${OCP_IP}/ ./YasumiPuzzleBox/src/main/fabric8/yasumipuzzleboxhandler.yaml ./YasumiPuzzleStarter/src/main/fabric8/yasumipuzzler.yaml ./YasumiPuzzleCalculator/src/main/fabric8/yasumipuzzleboxcalculator.yaml

echo Login to Openshift as 'Admin'
oc login -u admin -p admin ${OCP_IP}:8443

echo Make sure we are in the 'openshift' project
oc project openshift

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

echo Install JBoss A-MQ templates
oc create -f AMQ/amq62-exposed.json

echo Switch to other user
oc login -u openshift-dev -p devel

echo Creating the Project FIS-YasumiPuzzler
oc new-project yasumi --display-name="Yasumi Puzzler" --description="Yasumi Puzzle Solver Project"

echo Define artifacts for A-MQ and deploy it as service on Openshift
oc create -f AMQ/amq-app-secret.json
oc policy add-role-to-user view system:serviceaccount:yasumi:default
oc policy add-role-to-user view system:serviceaccount:yasumi:amq-service-account
oc new-app yasumi-amq -p MQ_USERNAME="admin" -p MQ_PASSWORD="change12_me"
oc expose service broker-amq-tcp

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
