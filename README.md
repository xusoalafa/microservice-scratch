# microservice-scratch

# sonarqube: admin/admin -> admin/12345
docker pull sonarqube:lts-community
docker run --name sonarqube-custom -p 9000:9000 -d sonarqube:lts-community
# mysql
docker pull mysql:8.0.39-debian
docker run --name mysql-8.0.39 -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -d mysql:8.0.39-debian