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

## From source

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

## From release

### Running
```bash
$ unzip ioio-bundle*
$ java -jar ioio-bundle*/ioio-core-*.jar
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

**Diff**
```
$ curl -X POST \
  http://localhost:9090/ioio-core/api/v1/diff \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Content-Length: 127' \
  -H 'Content-Type: application/json' \
  -H 'Host: localhost:9090' \
  -H 'cache-control: no-cache' \
  -d '{
    "oldText": "ABCDELMN\nPPPP\nXXXX\nnextline\nABC\nAAAA\ntest\n",
    "newText": "ABCFGLMN\nSTH\nXXXX\nABC\ntest\nBBBB\n"
}'
------
{"oldText":[-1,-1,2,-1,3,-1,4],"newText":[-1,-1,2,4,6,-1]}
```
Output format:
There is returned a json object with 2 attributes. Each attribute contains an array which length is equal to the number of lines in the text. If the line is not present in the second text, array element corresponding to this line is equal to -1. Otherwise it contains the index of the line in the second text.
