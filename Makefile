version=0.0.1-SNAPSHOT
options=-Drevision=$(version)

.PHONY: *

# The first command will be invoked with `make` only
build: docker
	./mvnw verify -Pformat $(options)

all: docker clean build

clean:
	./mvnw clean $(options)

docker:
	docker info

format:
	./mvnw test-compile $(options)

run: docker
	java -jar app/target/app-*.jar

test: docker
	./mvnw test $(options)

update:
	./mvnw versions:update-parent versions:update-properties versions:use-latest-versions $(options)
