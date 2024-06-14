version=0.0.1-SNAPSHOT
options=-Drevision=$(version) -Pfix

.PHONY: *

# The first command will be invoked with `make` only
build:
	docker info && ./mvnw clean verify $(options)

clean:
	./mvnw clean $(options)

compile:
	./mvnw clean test-compile $(options)

test:
	docker info && ./mvnw clean test $(options)

run:
	docker info && java -jar app/target/app-*.jar

update:
	./mvnw versions:update-parent versions:update-properties versions:use-latest-versions $(options)
