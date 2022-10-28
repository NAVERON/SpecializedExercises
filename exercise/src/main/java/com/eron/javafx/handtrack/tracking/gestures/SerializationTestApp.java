package com.eron.javafx.handtrack.tracking.gestures;

import com.eron.javafx.handtrack.tracking.Hand;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import javafx.geometry.Point3D;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class SerializationTestApp {

    private static Hand coords;
    public static Hand getCoords(){
        return coords;
    }

    private static Hand savedHand;
    public static Runnable setSavedHand(Hand hand) {
        if (hand == null)
            return null;

        savedHand = hand;
        return null;
    }

    public static Hand getSavedHand() {
        return savedHand;
    }

    public static class Point3DDeserializer extends JsonDeserializer<Point3D> {

        @Override
        public Point3D deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            var struct = jsonParser.readValueAs(Point3DStruct.class);

            return new Point3D(struct.x, struct.y, struct.z);
        }

        private static class Point3DStruct {
            public double x;
            public double y;
            public double z;
        }
    }

    public static void main(String[] args) {
        int i = 0;

        var hand = savedHand
        ;

        try {
            var module = new SimpleModule();
            module.addDeserializer(Point3D.class, new Point3DDeserializer());

            var mapper = new ObjectMapper();
            mapper.registerModule(module);

            mapper.writeValue(
                        Paths.get(
                            SerializationTestApp.class
                            .getResource("/handtrack/hand.json").toExternalForm()
                        ).toFile(), 
                        hand
                    );

            coords = mapper.readValue(
                        Paths.get(
                            SerializationTestApp.class
                            .getResource("/handtrack/hand.json").toExternalForm()
                        ).toFile(), 
                        Hand.class
                    );

        } catch (Exception e) {
            System.out.println("Error SerializationTestApp: " + e);
        }
    }
}
