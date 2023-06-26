version=0.0.1-SNAPSHOT
options=-Drevision=$(version)

compile:
	mvn clean test-compile $(options)
.PHONY: compile

build:
	mvn clean verify $(options)
.PHONY: build

test:
	mvn clean test $(options)
.PHONY: test

run:
	java -jar app/target/app-*.jar
.PHONY: run
