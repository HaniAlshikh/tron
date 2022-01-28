# Messaging Protocol

the messaging protocol is highly inspired by this specification of [JSON-RPC](https://www.jsonrpc.org/specification)

## Introduction

as a part of the marshaling process The function and its parameters are packed into a message, and sent to the server stub to be unmarshaled again. 

this protocol specifies the meta-information agreed on by the marshaller and unmarshaller to transfer the RPC function calls.

A typical message:

```json

{
  "id": "Random Request ID",
  "callbackIp": "Callback IP",
  "callbackPort": "Callback Port",
  "method": "Method Name",
  "service": "Service ID",
  "params": [
    {
      "argument": {
        "AttributeX": "Primitive Value X",
        "AttributeY": "Primitive Value Y",
        "type": "Argument Typ"
      },
      "type": "Parameter Type"
    }
  ]
}

```

## Request Object

A rpc call is represented by sending a Request object to a Server. The Request object has the following members:

* `id` Random Request UUID
* `callbackIp` IP where to send the result of the invocation
* `callbackPort` Port where to send the result of the invocation
* `service` Service ID
* `method`  Method Name
* `params` A list of Parameter Objects (see below)

### Notification

A Notification is a Request object without an "id" member. A Request object that is a Notification signifies an async function call

Notifications have no results and therefore requires no callback. omitting the "id" member omits the "callbackIp" and "callbackPort" as well.

## Parameter Object

* `type` The Parameter Type
* `argument` Objects that can be broken down into primitive types

## Error Object

* `code` Integer Error Code
* `message` String Error Message

## Processing Instructions

Processing Instructions used when serializing and deserializing composite data types.

### Tron Objects

Default Attributes:

* `type` The Argument Typ

Please note that the differentiation between Parameter Typ and Argument Typ comes from the fact, that the system adheres to the Dependency-Inversion-Principle and multiple implementation might exist.

#### IRpcCallerAppStub

* `ip` The IP Address of the Application Caller Stub (String)
* `port` The Port of the Application Caller Stub (int)

#### PlayerUpdate

* `movingDirection` The Name of the Direction the Player is heading (String)
* `dead` weather the Player is dead or not (String)
* `version` Incremented Version Number (int)

#### DirectoryServiceEntry

* `providerId` The id of the service provider (String)
* `serviceId` The id of the offered service (String)
* `address` The rpc server address (String)
* `port` The rpc server port (int)
* `reachable` weather the service is reachable or not (boolean) 

#### InetAddress

* `address` The IP Address (String)

#### UUID

* `uuid` The uniq identifier (String)
