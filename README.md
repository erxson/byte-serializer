## [Usage examples](https://github.com/erxson/byte-serializer/blob/main/src/test/java/xd/ericsson/serializer/SerializerTest.java)

## Adding to the project

### Maven
```xml
<repositories>
    <repository>
        <id>ericsson-repo</id>
        <url>https://ericsson.cfd/maven/</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>xd.ericsson</groupId>
        <artifactId>serializer</artifactId>
        <version>1.1-SNAPSHOT</version>
    </dependency>
</dependencies>
```

### Kotlin DSL
```groovy
repositories {
    maven("https://ericsson.cfd/maven/")
}

dependencies {
    implementation("xd.ericsson:serializer:1.1-SNAPSHOT")
}
```

### Groovy DSL
```groovy
repositories {
    maven {
        name = "ericsson"
        url = "https://ericsson.cfd/maven/"
    }
}

dependencies {
    implementation "xd.ericsson:serializer:1.1-SNAPSHOT"
}
```