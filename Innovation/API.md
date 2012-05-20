# API

Our API allows every third-party software developer to access the planes positions and the logs of the tower. 

## URL Scheme

Our REST API is easily accessible through simple requests. We also provide querying so you can filter in the request how many items you want, or query the position of only a specific plane. This document is a walk-through these features. 

### Positions

[Base API](http://itp.fredericjacobs.com:28017/towerDB/positions/)

The base API returns every single plane position available in the map. The Tower is updating the positions every second because it would send way to many requests if it had to process every since position update. To keep the tower responsive we are only updating it every second.

Special requests :
- Limit, only a certain number of elements will be returned : BaseAPI/?limit=10

- Skip, to skip to the other records : BaseAPI/?skip=5

- Search, find the position of a certain PlaneID : BaseAPI/?filter_planeID=X

- You can separate query's by adding a '&'. Hence we've got
BaseAPI/?filter_planeid=X&limit=10

### Logs 