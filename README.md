# quarkus-trains

This project is a POC to demonstrate how Quarkus is helping us to fastly build a very simple REST API exposing a list of trains, tracked by their positions by another gRPC service.

Each of those trains looks like this :



```
GET http://localhost:8080/trains

[
  {
    "id": 1,
    "name": "TRAIN-1",
    "position": {
      "kilometer": 16.0,
      "trackId": 42
    }
  },
  ...
]
```

The position attribute of each train is exposed by the backside quarkus-trains-position-service.

That service is receiving these positions from those trains embedding a quarkus-trains-position-client embedded as a native executable.

When a train is reporting its position, it receives back the previous and next trains position on its track :

```
-------------POSITION-TRACKING-ID-42-------------
Train actuel - ID : 77, Kilomètre : 45.556736
Train précédent - ID : 2, Kilomètre : 32.0
Train suivant - ID : 78, Kilomètre : 54.08075
```

## Diagram

![image](https://github.com/niushapaks/quarkus-trains/assets/9018054/11db9b92-b6bf-4291-83f6-45e2b0392999)
