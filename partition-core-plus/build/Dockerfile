FROM azul/zulu-openjdk:17
WORKDIR /app
ARG PROVIDER_NAME
ENV PROVIDER_NAME core-plus
ARG PORT
ENV PORT $PORT

COPY tmp/os-osm-postgres-*.jar plugins/osm-postgres.jar

# Copy the jar to the production image from the builder stage.
COPY partition-core-plus/target/partition-core-plus-*-spring-boot.jar partition-${PROVIDER_NAME}.jar
# Add a non-root user
RUN groupadd -g 10001 -r nonroot \
  && useradd -g 10001 -r -u 10001 nonroot
# Run as non-root user
USER 10001:10001
# Run the web service on container startup.
CMD java -Djava.security.egd=file:/dev/./urandom \
         -Dserver.port=${PORT} \
         -Dlog4j.formatMsgNoLookups=true \
         -Dloader.path=plugins/ \
         -Dloader.main=org.opengroup.osdu.partition.coreplus.PartitionApplication \
         -jar /app/partition-${PROVIDER_NAME}.jar
