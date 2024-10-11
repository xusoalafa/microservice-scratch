# microservice-scratch
# sonarqube: admin/admin -> admin/12345
docker pull sonarqube:lts-community
docker run --name sonarqube-custom -p 9000:9000 -d sonarqube:lts-community
