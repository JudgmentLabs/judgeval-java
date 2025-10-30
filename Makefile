.PHONY: format format-core format-openai install install-core install-openai status status-core status-openai check test clean build generate-client

format:
	@echo "[format] judgeval-java"
	mvn -B -pl judgeval-java -am spotless:apply
	@echo "[format] instrumentation/judgeval-instrumentation-openai"
	mvn -B -pl instrumentation/judgeval-instrumentation-openai -am spotless:apply

format-core:
	mvn -B -pl judgeval-java -am spotless:apply

format-openai:
	mvn -B -pl instrumentation/judgeval-instrumentation-openai -am spotless:apply

check:
	mvn -B compile checkstyle:check spotless:check

test:
	mvn test

clean:
	mvn clean

build:
	mvn -B clean compile

install:
	@echo "[install] judgeval-java"
	mvn -B -Dgpg.skip=true -pl judgeval-java -am clean install
	@echo "[install] instrumentation/judgeval-instrumentation-openai"
	mvn -B -Dgpg.skip=true -pl instrumentation/judgeval-instrumentation-openai -am clean install

install-core:
	mvn -B -Dgpg.skip=true -pl judgeval-java -am clean install

install-openai:
	mvn -B -Dgpg.skip=true -pl instrumentation/judgeval-instrumentation-openai -am clean install

generate-client:
	./scripts/generate-client.sh
	make format

lint:
	mvn -B checkstyle:check

format-check:
	mvn -B spotless:check

ci:
	mvn -B clean compile test checkstyle:check spotless:check

status:
	$(MAKE) status-core
	$(MAKE) status-openai

status-core:
	@echo "[status] judgeval-java"
	@G=$$(mvn -q -pl judgeval-java -DforceStdout help:evaluate -Dexpression=project.groupId); \
	 A=$$(mvn -q -pl judgeval-java -DforceStdout help:evaluate -Dexpression=project.artifactId); \
	 V=$$(mvn -q -pl judgeval-java -DforceStdout help:evaluate -Dexpression=project.version); \
	 echo "GAV: $$G:$$A:$$V"; \
	 ls -1 judgeval-java/target/*.jar 2>/dev/null || echo "No jar built"

status-openai:
	@echo "[status] instrumentation/judgeval-instrumentation-openai"
	@G=$$(mvn -q -pl instrumentation/judgeval-instrumentation-openai -DforceStdout help:evaluate -Dexpression=project.groupId); \
	 A=$$(mvn -q -pl instrumentation/judgeval-instrumentation-openai -DforceStdout help:evaluate -Dexpression=project.artifactId); \
	 V=$$(mvn -q -pl instrumentation/judgeval-instrumentation-openai -DforceStdout help:evaluate -Dexpression=project.version); \
	 echo "GAV: $$G:$$A:$$V"; \
	 ls -1 instrumentation/judgeval-instrumentation-openai/target/*.jar 2>/dev/null || echo "No jar built"

run:
	@if [ -f .env ]; then \
		export $$(grep -v '^#' .env | grep -v '^$$' | xargs) && mvn exec:java -Dexec.mainClass="$(CLASS)"; \
	else \
		mvn exec:java -Dexec.mainClass="$(CLASS)"; \
	fi
