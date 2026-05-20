# The wrapper makes sure we all use the same version, so if you bump into odd behaviour try running with the wrapper enabled.
## Use mvnd if present (unless you override the use of mvnd by setting the USE_WRAPPER variable) `USE_WRAPPER=true make <target>`
MVN := $(shell \
	[ "$$USE_WRAPPER" = "true" ] && echo ./mvnw || command -v mvnd || echo ./mvnw \
)

RELEASE_VERSION?=local-SNAPSHOT

.PHONY: *

# The first command will be invoked with `make` only and should be `build`
build: ## Standard build, unit-test, format, and analyze-dependencies
	$(MVN) verify -Pformat -Ddependency-analyze.strict=false -Preturn-value-not-used-strict

## Equivalent to `make clean build test`
all: clean build test

ci: ## Install a local artifact
	$(MVN) install

clean: ## Clean the project
	$(MVN) clean

format: ## Format the code and pom files
	$(MVN) test-compile -DskipTests -Dquality.skip -Pformat

## Install a local artifact with a version defined in this Makefile with RELEASE_VERSION
local: clean version ci version-revert

test: ## Run tests
	$(MVN) verify

update: ## Update versions in the pom files
	$(MVN) versions:update-parent versions:update-properties versions:use-latest-versions

version: ## Set the version according to the RELEASE_VERSION variable
	$(MVN) versions:set -DnewVersion=$(RELEASE_VERSION)

version-revert: ## Revert the version set by `make version`
	$(MVN) versions:revert versions:commit

yolo: ## Quick build, no validation or QA - use with caution
	$(MVN) verify -DskipTests -Dquality.skip

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
