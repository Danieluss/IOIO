# IOIO
[![Build Status](https://travis-ci.org/Danieluss/IOIO.svg?branch=master)](https://travis-ci.org/Danieluss/IOIO)


## Doc
https://danieluss.github.io/IOIO/docs/apidocs/

# Project structure

### ioio-base
Module providing some basic functionalities for micro-services.

### ioio-bundle
Packaging module.

### ioio-core
Module containing core functionality.

### ioio-core-api
Module containing core REST description.

### ioio-frontend
Frontend module.

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
  http://localhost:9090/ioio-core/api/v1/modifier/minify \
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
------
{"obj":{"arr":[{"abc":"some text","def":999},{"abc":"some other text","def":112}],"xyz":"value"}}
```

**Maxifier**

```bash
$ curl -X POST \
  http://localhost:9090/ioio-core/api/v1/modifier/maxify \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Content-Length: 108' \
  -H 'Content-Type: text/plain' \
  -H 'Host: localhost:9090' \
  -H 'cache-control: no-cache' \
  -d '{"obj": {"arr": [{"abc": "some text", "def": 999}, {"abc": "some other text", "def": 112}], "xyz": "value"}}'
------
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
------
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
------
{"some_field":{"nested_object2":{"nested_object":456}}}
```

**Combined**

```bash
$ curl -X POST \
  http://localhost:9090/ioio-core/api/v1/combined \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Content-Length: 457' \
  -H 'Content-Type: application/json' \
  -H 'Host: localhost:9090' \
  -H 'cache-control: no-cache' \
  -d '{"json":"{\"some_field\": [1,2,3,4,{\"nested_object\": 123}]}","modifiers":[{"type":"minify","params":""},{"type":"whitelist","params":"{\"some_field\": true}"},{"type":"maxify","params":""}]}'
------
{
  "some_field" : [ 1, 2, 3, 4, {
    "nested_object" : 123
  } ]
}
```
