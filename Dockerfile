FROM openjdk:8-jdk

RUN apt-get update && apt-get install -y git gradle

RUN mkdir /usr/roman

WORKDIR /usr/roman

RUN git clone https://github.com/romanjaik/wallet_grpc.git

WORKDIR wallet_grpc/

RUN gradle installDist