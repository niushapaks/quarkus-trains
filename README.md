# quarkus-trains

This project is a POC to demonstrate how Quarkus is helping us to fastly build a very simple REST API exposing a list of trains, tracked by their positions by another gRPC service.

The REST API [quarkus-trains-backend](https://github.com/niushapaks/quarkus-trains/blob/main/quarkus-trains-backend) is exposing each of those trains through the [TrainsResource](https://github.com/niushapaks/quarkus-trains/blob/main/quarkus-trains-backend/src/main/java/eu/pakseresht/trains/TrainResource.java) using quarkus-resteasy:

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

The position attribute of each train is exposed by the backside [quarkus-trains-position-service](https://github.com/niushapaks/quarkus-trains/blob/main/quarkus-trains-position-service) trough this [TrainPositionGrpcService](https://github.com/niushapaks/quarkus-trains/blob/main/quarkus-trains-position-service/src/main/java/eu/pakseresht/trains/TrainPositionGrpcService.java) implementation class.

That service is receiving these positions from those trains embedding a [quarkus-trains-position-client](https://github.com/niushapaks/quarkus-trains/blob/main/quarkus-trains-position-client) embedded as a native executable. The gRPC client is written in the [TrainPositionClient](https://github.com/niushapaks/quarkus-trains/blob/main/quarkus-trains-position-client/src/main/java/eu/pakseresht/trains/TrainPositionClient.java) class.

When a train is reporting its position, it receives back the previous and next trains position on its track :

```
-------------POSITION-TRACKING-ID-42-------------
Train actuel - ID : 77, Kilomètre : 45.556736
Train précédent - ID : 2, Kilomètre : 32.0
Train suivant - ID : 78, Kilomètre : 54.08075
```

## Diagram

![image](https://github.com/niushapaks/quarkus-trains/assets/9018054/11db9b92-b6bf-4291-83f6-45e2b0392999)

## API Contract

The API contract is defined as a Protobuf file. It is located on each of the modules exposing or consuming the gRPC service that we implemented ie : https://github.com/niushapaks/quarkus-trains/blob/main/quarkus-trains-position-service/src/main/proto/trains.proto 

## Parameters of quarkus-trains-position-client

Wheter as a java app, or as a native executable, quarkus-trains-position-client's launch parameters are ordered as trainId, trackId ie for train ID 78 in the target folder of quarkus-trains-position-client :

```
target % ./quarkus-trains-position-client-1.0.0-SNAPSHOT-runner 78 42
```

