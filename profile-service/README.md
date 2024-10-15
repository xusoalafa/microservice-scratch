# profile service
## neo4j
1. docker pull neo4j:latest
2. docker run --name neo4j --publish=7474:7474 --publish=7687:7687 -e NEO4J_AUTH=neo4j/12345678 neo4j:latest
3. go to admin page by localhost:7687 