package eu.pakseresht.trains;

import eu.pakseresht.trains.dto.TrainDto;
import eu.pakseresht.trains.dto.TrainPositionDto;
import io.quarkus.grpc.GrpcClient;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Arrays;
import java.util.List;

@Path("/trains")
public class TrainResource {

    @GrpcClient("trains")
    TrainPositionServiceGrpc.TrainPositionServiceBlockingStub trainPositionService;

    private final List<TrainDto> trainDtos = Arrays.asList(
            new TrainDto(1L, "TRAIN-1"),
            new TrainDto(2L, "TRAIN-2"),
            new TrainDto(3L, "TRAIN-3"),
            new TrainDto(77L, "TRAIN-77"),
            new TrainDto(78L, "TRAIN-78"),
            new TrainDto(79L, "TRAIN-79")
    );

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TrainDto> getTrains() {
        return trainDtos.stream()
                // TODO : do not do that in a real example ! trainDto should be immutable.
                .peek(trainDto -> {
                    TrainPosition position = trainPositionService.getPosition(
                            GetPositionRequest.newBuilder().setTrainId(trainDto.getId()).build()
                    );
                    trainDto.setPosition(new TrainPositionDto(position.getTrackId(), position.getKilometer()));
                })
                .toList();
    }
}
