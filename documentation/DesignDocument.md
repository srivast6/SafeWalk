Purdue SafeWalk Design Document
===============================

Purpose
-------
This document describes important aspects of the implementation of the Purdue SafeWalk mobile app system. The document will provide: a summary of functional requirements, general priorities, design details, and user interface mockups.

Summary of Functional Requirements
----------------------------------

* Must be able to show the (relative) location of volunteers at all times
 * Must do so while maintaining battery life. (Suggestion, use cell tower location until match is made, handled by Google Location APIs)

* Must be able to send the profile of a volunteer to the requester. (Image, name, ETA)

* Must be able to authenticate volunteers with the police department (Use Purdue Career Account?)

* Must Log all information to the police. Of this information, requests and arrivals should be notified (at least)

* Police must be able to access all locations of volunteers and of requesters on walks

* FUTURE: Use lightmap database and camera database to propose a route for the walk to follow. 

General Requirements
--------------------

Design Outline
--------------
* Architecture
  * We are using a hybrid client-server architecture where our server handles the mobile phone users, but also receives data about the current state of the SafeWalk system from the WLPD's Computer Aided Dispatch software.
* Server
  * The server will be written in Python and run on Google App Engine because Google guarantees that App Engine will be up at least 99.95% of the time in a month. In practice, App Engine is up much more than that (https://code.google.com/status/appengine/)
* JSON
  * To transmit information between the client and the server we will use JavaScript Object Notation (JSON). JSON is designed to be a human-readable means of exchanging data. Both the mobile clients and server will encode and decode JSON messages that contain updates about the state of the SafeWalk system.
* Authentication
  * Users of the mobile clients will have to authenticate themselves by using their Purdue Career Account login before using either of the mobile clients
* Clients
  * There will be two client applications developed for the SafeWalk system. One for Android devices and another for iPhones. Both clients will communicate with the server using the same API and protocols.

SafeWalk Server Objects
--------------------
### Request 
* ID
* Requestor (points to "Person" object)
* Walker/Employee
* Requested Time
* Pickup Location
* Dropoff location
* Device ID(s)

### Person
* Last Location
* Name
* Purdue Auth Token
* Picture (URL to pic)
* ID (hashmap)
* Device IDs for push
  
SafeWalk Server API
--------------------
### /request
#### POST
##### Parameters
* Pickup Location
* Requestor ID
##### Returns
* Request ID

``This also registers the phone with a push notification service``

#### GET
##### Parameters
* Auth Token
* Get pending only (flag) 
#### Returns
* Array of all requests

### /request/{request-id}/accept
#### POST
* Auth ID
* Location

### /users
#### POST (for registering)
* name (string)
* phoneNumber (string)
* currentLocation_lat (string)
* currentLocation_lng (string)
* deviceToken (string)
* * Either a string from Google Cloud Messaging or a byte[] serialized into a string for iOS push notifications.
* purdueCASServiceTicket (string)
#### GET
* JSON array of all registered users
* Requires an administrator or SafeWalk employee account to access

###/users/{id}
#### POST (for updating user info)
* name (string, optional)
* phoneNumber (string, optional)
* currentLocation_lat (string, optional)
* currentLocation_lng (string, optional)
* deviceToken (string, optional)
* * Either a string from Google Cloud Messaging or a byte[] serialized into a string for iOS push notifications.
* purdueCASServiceTicket (string, optional)
#### GET
* JSON object of user with matching id.

###/users/{id}/update-location
#### POST
* Authorization Token
* Current location



###/Walkers
#### GET
* Array of locations of walkers

###/Authenticate
#### POST
* `These parameters should be in the encrypted POST body as members of a JSON object`
* username (string)
* password (string)
* Returns
* * auth-token (string)

![API Flow](https://raw.github.com/Purdue-ACM-SIGAPP/SafeWalk/master/documentation/images/api-requests.jpg)

Mobile Client User Interface
----------------------------


