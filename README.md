# Judgeval Java SDK

[![Maven Central](https://img.shields.io/maven-central/v/com.judgmentlabs/judgeval-java)](https://central.sonatype.com/artifact/com.judgmentlabs/judgeval-java)
[![javadoc](https://javadoc.io/badge2/com.judgmentlabs/judgeval-java/javadoc.svg)](https://javadoc.io/doc/com.judgmentlabs/judgeval-java)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## Installation

Find the latest version on [Maven Central](https://central.sonatype.com/artifact/com.judgmentlabs/judgeval-java).

**Maven:**

```xml
<dependency>
    <groupId>com.judgmentlabs</groupId>
    <artifactId>judgeval-java</artifactId>
    <version>LATEST_VERSION</version>
</dependency>
```

**Gradle:**

```gradle
implementation 'com.judgmentlabs:judgeval-java:LATEST_VERSION'
```

## Usage

### Tracer

```java
Judgeval client = Judgeval.builder()
    .apiKey(System.getenv("JUDGMENT_API_KEY"))
    .organizationId(System.getenv("JUDGMENT_ORG_ID"))
    .build();

Tracer tracer = client.tracer()
    .create()
    .projectName("my-project")
    .build();

tracer.span("operation", () -> {
    // your code here
});
```

### Scorer

```java
BaseScorer scorer = client.scorers()
    .builtIn()
    .answerCorrectness()
    .threshold(0.8)
    .build();

Example example = Example.builder()
    .property("input", "What is 2+2?")
    .property("actual_output", "4")
    .property("expected_output", "4")
    .build();

tracer.asyncEvaluate(scorer, example);
```

## Documentation

- [API Documentation](https://javadoc.io/doc/com.judgmentlabs/judgeval-java)
- [Full Documentation](https://docs.judgmentlabs.ai/)

## License

Apache 2.0
