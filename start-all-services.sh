#!/bin/bash
set -e

PROJECT_DIR="$PWD"
SERVICES=("forumx-api-gateway" "forumx-commons" "forumx-content-service")
DOCKER_COMPOSE_DIR="$PROJECT_DIR/docker"

if ! docker-compose -f "$DOCKER_COMPOSE_DIR/docker-compose.yml" ps --services --filter "status=running" | grep -q .; then
	echo "Starting docker-compose services..."
	cd "$DOCKER_COMPOSE_DIR" && docker-compose up -d >/dev/null 2>&1
	cd "$PROJECT_DIR"
else
	echo "Docker-compose services are already running"
fi

echo "Starting microservices..."
for service in "${SERVICES[@]}"; do
	echo "Starting $service..."
	mvn spring-boot:run -pl "$service" -am >/dev/null 2>&1 &
	sleep 2
done

echo "All microservices started successfully!"
