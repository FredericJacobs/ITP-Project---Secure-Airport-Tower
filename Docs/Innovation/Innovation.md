# Innovation

We chose to build an API and light web interface on top of our project. 

## The Web Interface

It would allow users to get a web view of what's happening at the airport in an easy and convienient way. Not having to run Java which is really painful in my opinion with it's horrible AWT/Swing GUI classes. Our main web view is divided in two panels.

### The Map Panel

We want this to be a simple canvas where you can visualize the planes on top of a Google Map or Open Street Map since Google messed up recently with their fees and API conditions.

### The Logging Panel

The logging panel would be a web interface that could run anywhere. You'll say "Written once, runs everywhere" is what Java was meant for, right ? Well it used to be. Most mobile devices really suck at running Java (if they do). The browser platform is the most spread one and hence, it's the one we chose.
We'll try to have an AJAX table refreshing automatically to show incoming and outgoing messages everywhere.


## The API

The API is powering the web interface and hence we thought we could document it and make it public. 

## How does it all work together ? 

The Java Program that the tower is running sending all updates to a MongoDB database. This allows the airport to have a safe, off-site history. Why did we choose MongoDB ? We hate SQL so much. MongoDB offers drivers for a wide variety of language and is so JSON-friendly. Moreover, it has a distributed, fault-tolerant architecture
We thought pushing updates to the database was also safer in terms of security and stability of the tower. The tower has no incomming traffic for the web interface at all. It's only sending to the MongoDB database.

From there on, we simply use JavaScript to display it to the end-user.

## Database Architecture

MongoDB is a schemeless NoSQL and is a document-oriented database. We choose to have separate databases for the positions and the logging. I would have used an extra Redis-server for the position update to improve scalabilty/speed of the program. But since this is project will never be run "for real" and our time is very limited, I just sticked to MongoDB for everything.

### Planes position

Each MongoDB document contains the plane-type, current fuel level and position information.

### Logging

BSON could have been used but we chose JSON for better handeling on the front-end side.

There is no specific scheme. More to come

### Demo Link 

It's running on a [VPS](http://itp.fredericjacobs.com)

Just run the tower and it should automatically connect ! 

