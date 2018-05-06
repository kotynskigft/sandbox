# How to run
Run server locally: mvnw spring-boot:run

# How to build docker image
1) build local fat jar (with all dependencies)
    mvnw package
2) build docker image named e.g. mywebsockerservice
    docker build -t mywebsocketservice .
3) run image locally to check if it is working
    docker run -p 8080:8080 -it --rm mywebsocketservice

## Notes
Java code inspired by https://youtu.be/GlvyHIqT3K4

BTW Wondering what to use as the base image for docker? https://cleverbuilder.com/articles/java-docker-parent-image/