Server version based on https://github.com/websockets/ws

Docker build based on https://nodejs.org/en/docs/guides/nodejs-docker-webapp/

To test the app on EC2:
* push your image to docker hub
* Create EC2 instance with key pair
* ssh -i /path/my-key-pair.pem ec2-user@ec2-198-51-100-1.compute-1.amazonaws.com. If you see 'WARNING: UNPROTECTED PRIVATE KEY FILE!' refer to https://stackoverflow.com/questions/1556119/ssh-private-key-permissions-using-git-gui-or-ssh-keygen-are-too-open
* instal [docker on EC2 instance](https://bharadwajofficial.wordpress.com/2017/10/10/configure-docker-in-amazon-linux-ami/)