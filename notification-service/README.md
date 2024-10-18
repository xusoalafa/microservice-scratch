# Notification service

## Prerequisites

### Signup free account brevo for email service 
* Link: `https://www.brevo.com`
* Generate api-key to use in backend`https://app.brevo.com/settings/keys/api`
* Go to Brevo dev to try send email with api-key: `https://developers.brevo.com/reference/sendtransacemail`
* Can use yopmail to test

### Mongodb
Install Mongodb from Docker Hub

`docker pull bitnami/mongodb:7.0.11`

Start Mongodb server at port 27017 with root username and password: root/root

`docker run -d --name mongodb-7.0.11 -p 27017:27017 -e MONGODB_ROOT_USER=root -e MONGODB_ROOT_PASSWORD=root bitnami/mongodb:7.0.11`
