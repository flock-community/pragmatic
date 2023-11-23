version=0.0.1-SNAPSHOT
options=-Drevision=$(version) -Pfix

.PHONY: *

clean:
	./mvnw clean $(options)

compile:
	./mvnw clean test-compile $(options)

build:
	docker info && ./mvnw clean verify $(options)

test:
	docker info && ./mvnw clean test $(options)

run:
	docker info && java -jar app/target/app-*.jar
