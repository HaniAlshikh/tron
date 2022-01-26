# Messaging Protocol

## Introduction

To transfer the RPC Function Calls and the Result back.

## Request Object

* `id` Random Request UUID
* `service` Service ID
* `method`  Method Name
* `params` Method Parameter Objects (see below)
* `callbackIp` IP where to send the result of the Method
* `callbackPort` Port where to send the result of the Method

## Parameter Object

* `type` Primitive Type of Argument, like Integer or String
* `argument` The actual Argument Value

## Error Object

* `code` Integer Error Code
* `message` String Error Message

## Tron Objects

Default Attributes:

* `type` Name of the Class that called the Method is being called on

### IRpcCallerAppStub

* `ip` The IP Address of the Application Caller Stub
* `port` The Port of the Application Caller Stub

### PlayerUpdate

* `movingDirection` The Name of the Direction the Player is heading
* `dead` Boolean wether the Player is dead or not
* `version` Incremented Version Number

### DirectoryServiceEntry

* `providerId`
* `serviceId`
* `address`
* `port`
* `reachable`

### InetAddress

* `address` The IP Address in String format

### UUID

* `uuid` The String Value of the UUID
