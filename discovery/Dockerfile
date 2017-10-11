FROM bluezd/tomcat-oracle-java-8:latest
WORKDIR /usr/local/tomcat
RUN mkdir -p /scratch/apm_staging
RUN yum install -y hostname unzip bc
ARG REG_KEY

ENV STAGE_DIR=/scratch/apm_staging \
DESTINATION=/usr/local/tomcat \
REG_KEY="RCB6Vi9tGxYnjckZ7Km7G8GM_W"

COPY target/discovery-1.0.war /usr/local/tomcat/webapps/
COPY apm/AgentInstall.sh /AgentInstall.sh
RUN chmod a+x /AgentInstall.sh
COPY start.sh /start.sh
RUN chmod a+x /start.sh
EXPOSE 8080
CMD ["/start.sh"]