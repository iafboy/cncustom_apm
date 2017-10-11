#!/bin/bash
echo "## start installing apm_agent ##"
/AgentInstall.sh AGENT_TYPE=apm_java_as_agent STAGE_LOCATION=${STAGE_DIR} AGENT_REGISTRATION_KEY=${REG_KEY} -download_only
cd ${STAGE_DIR}
chmod a+x ./ProvisionApmJavaAsAgent.sh
echo "y" | ./ProvisionApmJavaAsAgent.sh -d ${CATALINA_HOME} -no-wallet
echo -e "\n## start running tomcat ##"
cd $CATALINA_HOME/bin
cp catalina.sh catalina.sh.orig
export CATALINA_OPTS="$CATALINA_OPTS -javaagent:${CATALINA_HOME}/apmagent/lib/system/ApmAgentInstrumentation.jar -Datlassian.org.osgi.framework.bootdelegation=oracle.apmaas.*,sun.*,org.apache.xerces,org.apache.xerces.*,org.apache.naming,org.apache.naming.*,org.apache.catalina,org.apache.catalina.*,org.apache.tomcat.util.*"
catalina.sh run