version=0.0.1-SNAPSHOT
options=-Drevision=$(version) -Pfix

.PHONY: *

# The first command will be invoked with `make` only
build: docker clean
	./mvnw verify $(options)

clean:
	./mvnw clean $(options)

compile: clean
	./mvnw test-compile $(options)

docker:
	docker info

run: docker
	java -jar app/target/app-*.jar

test: docker clean
	./mvnw test $(options)

update:
	./mvnw versions:update-parent versions:update-properties versions:use-latest-versions $(options)
