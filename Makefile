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

# This outputs any command in the Makefile. With a short description taken from a ## prefixed command after the command (preferred) or the line above
help: ## Show this help
	@echo "Usage: make <command>"; \
	echo ""; \
	desc=""; \
	while IFS= read -r line; do \
		case "$$line" in \
			'## '*)              desc="$${line#\#\# }" ;; \
			[a-zA-Z_-]*:*'## '*) printf '\033[36m%-20s\033[0m %s\n' "$${line%%:*}" "$${line#*\#\# }"; desc="" ;; \
			[a-zA-Z_-]*:*)       printf '\033[36m%-20s\033[0m %s\n' "$${line%%:*}" "$$desc"; desc="" ;; \
			*)                   desc="" ;; \
		esac; \
	done < $(MAKEFILE_LIST) | sort
