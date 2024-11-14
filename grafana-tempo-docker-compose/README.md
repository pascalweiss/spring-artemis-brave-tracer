### Minimum docker-compose setup for Grafana with Tempo

This is a minimal setup to run Grafana with Tempo. It uses the official Grafana and Tempo images.
I pinned the versions to the following:
- Grafana: 11.3.0
- Tempo: 2.6.1

#### How to run
To start it up, simply do
```bash
docker-compose up
```

If you want to run it with newer versions, you can provide the the command with the corresponding environment variables.
For example, to run it with Grafana and Tempo latest versions, you can do
```bash
GRAFANA_VERSION=latest TEMPO_VERSION=latest docker-compose up
```

#### Data persistence
Tempo data is stored in the `tempo-data` volume, which is created in the same directory as the `docker-compose.yml` 
file when you run `docker-compose up`.