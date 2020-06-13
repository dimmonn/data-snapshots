setup:
	docker volume create nodemodules
install:
	docker-compose -f docker-compose.builder.yml run --rm install && mvn -f data-snapshots-auth/pom.xml package
dev:
	docker-compose up --build
stop:
	docker-compose down
clear:
	docker-compose down --rmi all --remove-orphans
test:
	mvn -f data-snapshots-auth/pom.xml test
build:
	docker-compose create