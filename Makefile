version=0.0.1-SNAPSHOT
options=-Drevision=$(version) -Pfix

.PHONY: *

compile:
	./mvnw clean test-compile $(options)

build:
	./mvnw clean verify $(options)

test:
	./mvnw clean test $(options)

clean:
	./mvnw clean $(options)

run:
	java -jar app/target/app-*.jar
