FROM alpine:latest

USER root

RUN \
  apk update && \
  apk upgrade && \
  apk add --update bash && \
  apk add openjdk11 && \
  rm -rf /var/cache/apk/* && \
  mkdir /benchmark

ENV JAVA_HOME /usr/lib/jvm/java-1.11-openjdk

WORKDIR /benchmark

CMD ["bash"]
