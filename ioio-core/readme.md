[![Build Status](https://travis-ci.org/Danieluss/IOIO.svg?branch=master)](https://travis-ci.org/Danieluss/IOIO)

# Core

### Installation
1. set `JAVA_HOME` to your java11 JDK directory

2. ```mvn clean install```

3. change keymap in your IDE to `Eclipse` \
please note that using other keymaps may result in impotence, diabetes and cancer
### Running
```
mvn clean package
java -jar target/ioio-core-*.jar
```

### Health check
http://localhost:9090/ioio-core/actuator/health

### API
http://localhost:9090/ioio-core/swagger-ui.html