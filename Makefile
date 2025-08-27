.PHONY: help format check test clean build generate-client

help: ## Show this help message
	@echo "Available commands:"
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-20s\033[0m %s\n", $$1, $$2}'

format: ## Format code using Spotless
	mvn spotless:apply

check: ## Run all code quality checks
	mvn compile checkstyle:check spotless:check

test: ## Run tests
	mvn test

clean: ## Clean build artifacts
	mvn clean

build: ## Build the project
	mvn clean compile

generate-client: ## Generate API client from OpenAPI spec
	./scripts/generate-client.sh

lint: ## Run linting only
	mvn checkstyle:check

format-check: ## Check formatting without applying
	mvn spotless:check

ci: ## Run CI checks (compile, test, checkstyle, spotless)
	mvn clean compile test checkstyle:check spotless:check

run: ## Run a specific Java class with environment variables (usage: make run CLASS=com.example.MyClass)
	@if [ -f .env ]; then \
		export $$(grep -v '^#' .env | grep -v '^$$' | xargs) && mvn exec:java -Dexec.mainClass="$(CLASS)"; \
	else \
		mvn exec:java -Dexec.mainClass="$(CLASS)"; \
	fi
