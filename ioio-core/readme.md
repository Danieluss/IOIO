[![Build Status](https://travis-ci.org/Danieluss/IOIO.svg?branch=master)](https://travis-ci.org/Danieluss/IOIO)

# Core

### Installation
1. set `JAVA_HOME` to your java11 JDK directory

2. ```$ mvn clean install```

3. change keymap in your IDE to `Eclipse` \
please note that using other keymaps may result in impotence, diabetes and cancer
### Running
```bash
$ mvn clean package
$ java -jar target/ioio-core-*.jar
```

### Health check
http://localhost:9090/ioio-core/actuator/health

### API
http://localhost:9090/ioio-core/swagger-ui.html

### Features

**Maxifier**

```bash
$ curl -X POST \
  http://localhost:9090/ioio-core/api/v1/modifier/minifier \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Content-Length: 169' \
  -H 'Content-Type: text/plain' \
  -H 'Host: localhost:9090' \
  -H 'cache-control: no-cache' \
  -d '{
  "obj" : {
    "arr" : [ {
      "abc" : "some text",
      "def" : 999
    }, {
      "abc" : "some other text",
      "def" : 112
    } ],
    "xyz" : "value"
  }
}'

{"obj":{"arr":[{"abc":"some text","def":999},{"abc":"some other text","def":112}],"xyz":"value"}}
```

**Maxifier**

```bash
$ curl -X POST \
  http://localhost:9090/ioio-core/api/v1/modifier/maxifier \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Content-Length: 108' \
  -H 'Content-Type: text/plain' \
  -H 'Host: localhost:9090' \
  -H 'cache-control: no-cache' \
  -d '{"obj": {"arr": [{"abc": "some text", "def": 999}, {"abc": "some other text", "def": 112}], "xyz": "value"}}'

{
  "obj" : {
    "arr" : [ {
      "abc" : "some text",
      "def" : 999
    }, {
      "abc" : "some other text",
      "def" : 112
    } ],
    "xyz" : "value"
  }
}
```

**Whitelist**

```bash
$ curl -X POST \
  http://localhost:9090/ioio-core/api/v1/filter/whitelist \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Content-Length: 102' \
  -H 'Content-Type: application/json' \
  -H 'Host: localhost:9090' \
  -H 'cache-control: no-cache' \
  -d '{
	"json" : "{\"some_field\": 123, \"some_other_field\": 1234}",
	"filter": "{\"some_field\": true}"
}'

{"some_field":123}
```

**Blacklist**

```bash
$ curl -X POST \
  http://localhost:9090/ioio-core/api/v1/filter/blacklist \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Content-Length: 206' \
  -H 'Content-Type: application/json' \
  -H 'Host: localhost:9090' \
  -H 'cache-control: no-cache' \
  -d '{
	"json" : "{\"some_field\": {\"nested_object1\": {\"nested_object\": 123}, \"nested_object2\": {\"nested_object\": 456}}}",
	"filter": "{\"some_field\": {\"nested_object1\": {\"nested_object\": true}}}"
}'

{"some_field":{"nested_object2":{"nested_object":456}}}
```

**All**

```bash
$ curl -X POST \
  http://localhost:9090/ioio-core/api/v1/do \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Content-Length: 457' \
  -H 'Content-Type: application/json' \
  -H 'Host: localhost:9090' \
  -H 'Postman-Token: 0345d8dd-2bb4-4ac4-9d8c-64ef1a4321f2,6bd4db19-f8ea-49ab-8547-1fb6f34494ab' \
  -H 'User-Agent: PostmanRuntime/7.20.1' \
  -H 'cache-control: no-cache' \
  -d '{
    "json": "{\"some_field\": {\"nested_object1\": {\"nested_object\": 123}, \"nested_object2\": {\"nested_object\": 456}}}",
    "features": [
        {
            "type": "filter/blacklist",
            "payload": "{\"some_field\": {\"nested_object1\": {\"nested_object\": true}}}"
        },
        {
            "type": "modifier/maxifier"
        }
    ]
}'

{
  "some_field" : {
    "nested_object2" : {
      "nested_object" : 456
    }
  }
}
```

