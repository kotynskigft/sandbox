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

What to use as the base image for docker? https://cleverbuilder.com/articles/java-docker-parent-image/

To test the app on EC2:
* push your image to docker hub (ofcourse you need to change name - add prefix to point your docker name)
* Create EC2 instance with key pair
* ssh -i /path/my-key-pair.pem ec2-user@ec2-198-51-100-1.compute-1.amazonaws.com. If you see 'WARNING: UNPROTECTED PRIVATE KEY FILE!' refer to https://stackoverflow.com/questions/1556119/ssh-private-key-permissions-using-git-gui-or-ssh-keygen-are-too-open
* instal [docker on EC2 instance](https://bharadwajofficial.wordpress.com/2017/10/10/configure-docker-in-amazon-linux-ami/)