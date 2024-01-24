package eu.pakseresht.trains;

import io.quarkus.grpc.GrpcClient;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.scheduler.Scheduled;

import java.util.concurrent.TimeUnit;

public class TrainPositionClient implements QuarkusApplication {
    @GrpcClient("trains")
    TrainPositionServiceGrpc.TrainPositionServiceBlockingStub trainPositionService;

    long trainId;
    long trackId;

    @Override
    public int run(String... args) {

        this.trainId = Long.parseLong(args[0]);
        this.trackId = Long.parseLong(args[1]);

        Quarkus.waitForExit();
        return 0;

    }

    @Scheduled(every="5s")
    void triggerPositionReport() {
        PositionRequest request = generateRandomPosition(trainId, trackId);
        PositionResponse positionResponse = null;
        try{
            positionResponse = trainPositionService.withDeadlineAfter(100, TimeUnit.MILLISECONDS).reportPosition(request);
        } catch (Exception e) {
            System.out.println("Erreur lors du reporting de position: " + e.getMessage());
        }
        if (positionResponse != null) {
            displayPositionResponse(positionResponse);
        }
    }

    private static void displayPositionResponse(PositionResponse positionResponse) {

        System.out.println("-------------POSITION-TRACKING-ID-" + positionResponse.getActualTrain().getTrackId() + "-------------");

        System.out.println("Train actuel - ID : " + positionResponse.getActualTrain().getTrainId() +
                ", Kilomètre : " + positionResponse.getActualTrain().getKilometer());

        TrainPosition previousTrain = positionResponse.getPreviousTrain();
        if (previousTrain.getTrainId() != 0) {
            System.out.println("Train précédent - ID : " + previousTrain.getTrainId() +
                    ", Kilomètre : " + previousTrain.getKilometer());
        }

        TrainPosition nextTrain = positionResponse.getNextTrain();
        if (nextTrain.getTrainId() != 0) {
            System.out.println("Train suivant - ID : " + nextTrain.getTrainId() +
                    ", Kilomètre : " + nextTrain.getKilometer());
        }
    }

    private PositionRequest generateRandomPosition(long trainId, long trackId) {

        float kilometer = (float) (Math.random() * 100);

        return PositionRequest.newBuilder()
                .setTrainId(trainId)
                .setTrackId(trackId)
                .setKilometer(kilometer)
                .build();
    }
}
