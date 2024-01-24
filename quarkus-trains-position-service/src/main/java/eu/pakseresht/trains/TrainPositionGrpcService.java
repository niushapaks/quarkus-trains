package eu.pakseresht.trains;

import io.quarkus.grpc.GrpcService;

import io.smallrye.mutiny.Uni;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@GrpcService
public class TrainPositionGrpcService implements TrainPositionService {

    private final Map<Long, Map<Long, TrainPosition>> trackPositions = new ConcurrentHashMap<>();

    public TrainPositionGrpcService() {
        long demonstrationTrackId = 42L;
        ConcurrentHashMap<Long, TrainPosition> demonstrationTrack = new ConcurrentHashMap<>();

        demonstrationTrack.put(1L, TrainPosition.newBuilder()
                .setTrainId(1L)
                .setTrackId(demonstrationTrackId)
                .setKilometer(16)
                .build());

        demonstrationTrack.put(2L, TrainPosition.newBuilder()
                .setTrainId(2L)
                .setTrackId(demonstrationTrackId)
                .setKilometer(32)
                .build());

        demonstrationTrack.put(3L, TrainPosition.newBuilder()
                .setTrainId(3L)
                .setTrackId(demonstrationTrackId)
                .setKilometer(64)
                .build());

        trackPositions.put(demonstrationTrackId, demonstrationTrack);
    }

    @Override
    public Uni<TrainPosition> getPosition(GetPositionRequest request) {
        Long trainId = request.getTrainId();

        return Uni.createFrom().item(() ->
                trackPositions.values().stream()
                        .map(trackMap -> trackMap.get(trainId))
                        .filter(Objects::nonNull)
                        .findFirst()
                        .orElse(TrainPosition.newBuilder().build()));
    }

    @Override
    public Uni<PositionResponse> reportPosition(PositionRequest request) {
        long trainId = request.getTrainId();
        long trackId = request.getTrackId();
        float kilometer = request.getKilometer();

        TrainPosition currentPosition = TrainPosition.newBuilder()
                .setTrainId(trainId)
                .setTrackId(trackId)
                .setKilometer(kilometer)
                .build();

        trackPositions.computeIfAbsent(trackId, k -> new ConcurrentHashMap<>()).put(trainId, currentPosition);

        TrainPosition previousTrainPosition = getPreviousTrainPosition(trackId, trainId, kilometer);
        TrainPosition nextTrainPosition = getNextTrainPosition(trackId, trainId, kilometer);

        PositionResponse finalResponse = PositionResponse.newBuilder()
                .setActualTrain(currentPosition)
                .setPreviousTrain(previousTrainPosition)
                .setNextTrain(nextTrainPosition)
                .build();

        return Uni.createFrom().item(finalResponse);
    }

    private TrainPosition getPreviousTrainPosition(long trackId, long currentTrainId, float currentKilometer) {
        return trackPositions.getOrDefault(trackId, new ConcurrentHashMap<>()).values().stream()
                .filter(position -> position.getTrainId() != currentTrainId && position.getKilometer() < currentKilometer)
                .max(Comparator.comparing(TrainPosition::getKilometer))
                .orElse(TrainPosition.getDefaultInstance());
    }

    private TrainPosition getNextTrainPosition(Long trackId, Long currentTrainId, float currentKilometer) {
        return trackPositions.getOrDefault(trackId, new ConcurrentHashMap<>()).values().stream()
                .filter(position -> position.getTrainId() != currentTrainId && position.getKilometer() > currentKilometer)
                .min(Comparator.comparing(TrainPosition::getKilometer))
                .orElse(TrainPosition.getDefaultInstance());
    }

}
