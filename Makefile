version=0.0.1-SNAPSHOT
options=-Drevision=$(version) -Pfix

.PHONY: *

compile:
	mvn clean test-compile $(options)

build:
	mvn clean verify $(options)

test:
	mvn clean test $(options)

clean:
	mvn clean $(options)

run:
	java -jar app/target/app-*.jar
