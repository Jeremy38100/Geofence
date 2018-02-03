# Geofence Tracker

## Problem

We consider the world is flat, and **covered by a square grid**.

A **geofence** is a geographic area where we want to track users. We consider circular geofences only.

A **geofence hit is defined as when a user stays more than 10 minutes** within a geofence.
We consider we have a regular tracking of the position of our users.

We have a static list of geofences, for each one we have its center position (x, y), its radius and its name.
Geofence:	int x, int y, double radius, string name

Write a class that, having initially the list of geofences, receives a stream of users’ positions (string user_id, int x, int y, long timestamp) and produce geofence hits for these users.

Dataset example:
* With fences (x, y, radius, name).
    * 1, 1, 1.0, “fence 1”
	* 10, 10, 2.0, “fence 2”
* And stream (id, x, y, times in minutes):
	* “User1”, 0, 0, 0
	* “User2”, 0, 0, 0
	* “User1”, 1, 1, 1 _// User 1 enters the fence 1_
	* “User1”, 1, 1, 15 _// User 1 have been in the fence 1 for > 10 min -> **trigger**_
	* “User2”, 10, 11, 16 _// User 2 enters the fence 2_
	* “User2”, 10, 9, 25 _// User 2 is still inside fence 2, but for 9 minutes._
	* “User2”, 1, 1, 30 _// User 2 left fence 2 -> no trigger._
	* “User1”, 1, 1, 35	_// User 1 is still in fence 1, but we already triggered._
* The output should therefore be:
	* User 1, fence 1, time 15.
	
_Problem by Nicolas Defranoux @ Situ8ed SA_

## Solution

The solution is based on the **Observer Design Pattern**. Geofences are observers subscribed to user's positions stream.

cf : https://sourcemaking.com/design_patterns/observer

### Geofences
10 Geofences are randomly generated in a 500x500 square World. 
- The radius is between 50 and 100. 
- Geofences are fully inside the world
    - Foreach Geofence(x,y,radius), x >= radius AND y>= radius AND (500-x) >= x AND 500-y >= y

###Users
4 Users moves in the world with a random path (Gaussian distribution). 
- A user starts at the (0,0) point and allways stay in the world.

###DataVisualization
To run the DataViz run a HTTPServer inside the /dataviz folder `python -m SimpleHTTPServer 8000` for example. Then go in a web browser to the `http://localhost:8000/` address.

The `com.roche.geofence.jsonlog` package is used to log Geofences and Users position in JSON format used by D3.JS in the datavisualization.