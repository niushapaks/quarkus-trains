package eu.pakseresht.trains;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class TrainPositionMain {
    public static void main(String... args) {
        Quarkus.run(TrainPositionClient.class, args);
    }
}
