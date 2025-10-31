.PHONY: format format-core format-openai install install-core install-openai status status-core status-openai check test clean build generate-client run

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

MAIN ?=

ifneq (,$(filter run,$(MAKECMDGOALS)))
EXAMPLE := $(word 2,$(MAKECMDGOALS))
ifeq ($(EXAMPLE),)
$(error Usage: make run <example_folder> [MAIN=ClassName])
endif
$(eval $(EXAMPLE):;@:)
endif



run:
	@echo "[run] examples.$(EXAMPLE)"
	if [ -f .env ]; then export $$(grep -v '^#' .env | grep -v '^$$' | xargs); fi; \
	MAIN_CLASS=$(MAIN); \
	if [ -z "$$MAIN_CLASS" ]; then \
	  MAIN_CLASS=$$(ls examples/src/main/java/examples/$(EXAMPLE)/*.java | head -n1 | xargs -n1 basename | sed 's/\.java$$//'); \
	fi; \
    mvn -q -f examples/pom.xml -DskipTests -Dexec.cleanupDaemonThreads=false -Dexec.mainClass=examples.$(EXAMPLE).$$MAIN_CLASS clean compile exec:java
