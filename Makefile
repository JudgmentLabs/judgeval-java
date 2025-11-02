.PHONY: format format-core format-openai install install-core install-openai status status-core status-openai check test clean build generate-client run

format:
	@echo "[format] judgeval-java"
	mvn -B -f judgeval-java/pom.xml spotless:apply
	@echo "[format] instrumentation/judgeval-instrumentation-openai"
	mvn -B -f instrumentation/judgeval-instrumentation-openai/pom.xml spotless:apply

format-core:
	mvn -B -f judgeval-java/pom.xml spotless:apply

format-openai:
	mvn -B -f instrumentation/judgeval-instrumentation-openai/pom.xml spotless:apply

check:
	cd judgeval-java && mvn -B compile checkstyle:check spotless:check

test:
	cd judgeval-java && mvn test

clean:
	cd judgeval-java && mvn clean
	cd instrumentation/judgeval-instrumentation-openai && mvn clean

build:
	cd judgeval-java && mvn -B clean compile

install:
	@echo "[install] judgeval-java"
	cd judgeval-java && mvn -B -Dgpg.skip=true clean install
	@echo "[install] instrumentation/judgeval-instrumentation-openai"
	cd instrumentation/judgeval-instrumentation-openai && mvn -B -Dgpg.skip=true clean install

install-core:
	cd judgeval-java && mvn -B -Dgpg.skip=true clean install

install-openai:
	cd instrumentation/judgeval-instrumentation-openai && mvn -B -Dgpg.skip=true clean install

generate-client:
	./scripts/generate-client.sh
	make format

lint:
	cd judgeval-java && mvn -B checkstyle:check

format-check:
	cd judgeval-java && mvn -B spotless:check
	cd instrumentation/judgeval-instrumentation-openai && mvn -B spotless:check

ci:
	cd judgeval-java && mvn -B clean compile test checkstyle:check spotless:check

status:
	$(MAKE) status-core
	$(MAKE) status-openai

status-core:
	@echo "[status] judgeval-java"
	@cd judgeval-java && \
	 G=$$(mvn -q -DforceStdout help:evaluate -Dexpression=project.groupId); \
	 A=$$(mvn -q -DforceStdout help:evaluate -Dexpression=project.artifactId); \
	 V=$$(mvn -q -DforceStdout help:evaluate -Dexpression=project.version); \
	 echo "GAV: $$G:$$A:$$V"; \
	 ls -1 target/*.jar 2>/dev/null || echo "No jar built"

status-openai:
	@echo "[status] instrumentation/judgeval-instrumentation-openai"
	@cd instrumentation/judgeval-instrumentation-openai && \
	 G=$$(mvn -q -DforceStdout help:evaluate -Dexpression=project.groupId); \
	 A=$$(mvn -q -DforceStdout help:evaluate -Dexpression=project.artifactId); \
	 V=$$(mvn -q -DforceStdout help:evaluate -Dexpression=project.version); \
	 echo "GAV: $$G:$$A:$$V"; \
	 ls -1 target/*.jar 2>/dev/null || echo "No jar built"

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
