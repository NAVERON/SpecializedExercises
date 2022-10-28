package com.eron.javafx.handtrack.tracking.gestures;

import com.almasb.fxgl.logging.Logger;
import com.eron.javafx.handtrack.tracking.GestureEvaluationResult;
import com.eron.javafx.handtrack.tracking.GestureEvaluator;
import com.eron.javafx.handtrack.tracking.Hand;
import com.eron.javafx.handtrack.tracking.HandGesture;
import com.eron.javafx.handtrack.tracking.HandLandmark;
import com.eron.javafx.handtrack.tracking.HandMetadata;
import com.eron.javafx.handtrack.tracking.HandMetadataAnalyser;
import com.eron.javafx.handtrack.tracking.HandOrientation;

import javafx.geometry.Point3D;
import javafx.util.Pair;

import java.util.EnumMap;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static com.eron.javafx.handtrack.tracking.HandGesture.*;
import static com.eron.javafx.handtrack.tracking.HandLandmark.*;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class GeometricGestureEvaluator implements GestureEvaluator {

    private static final Logger log = Logger.get(GeometricGestureEvaluator.class);
    private EnumMap<HandGesture, BiFunction<Hand, HandMetadata, Double>> evaluators = new EnumMap<>(HandGesture.class);

    public GeometricGestureEvaluator() {
        // TODO: populate evaluators
        //evaluators.put(THUMB_INDEX_PINCH, this::evalThumbIndexPinch);
        //evaluators.put(THUMB_PINKY_PINCH, this::evalThumbPinkyPinch);
        //evaluators.put(THUMB_RING_FINGER_PINCH, this::evalThumbRingPinch);
        //evaluators.put(THUMB_MIDDLE_FINGER_PINCH, this::evalThumbMiddlePinch);
        evaluators.put(THUMB_INDEX_MIDDLE_DOWN, this::evalMiddleIndexThumbDown); // 大拇指 食指 中指 朝下  
        evaluators.put(THUMBS_UP, this::evalThumbsUp);  // 大拇指朝上 
        evaluators.put(PEACE, this::evalPeaceSign);  // victory 
        evaluators.put(FINGERGUN, this::evalFingerGun);  // 指向 
        evaluators.put(OK, this::evalOKSign);  // OK 手型 
        evaluators.put(THUMBS_DOWN, this::evalThumbsDown); // 大拇指朝下 
    }

    // Use Finger Tip Landmark to find if that finger is curled 
    public static boolean isFingerDown(Hand hand, HandLandmark landmark) {

        switch (getOrientation(hand)) {
            case LEFT -> {
                switch(landmark)
                {
                    case THUMB_TIP -> {
                        return hand.getPoint(landmark).getY() < hand.getPoint(THUMB_MCP).getY();
                    }
                    case INDEX_FINGER_TIP -> {
                        return hand.getPoint(landmark).getX() < hand.getPoint(INDEX_FINGER_PIP).getX();
                    }
                    case MIDDLE_FINGER_TIP -> {
                        return hand.getPoint(landmark).getX() < hand.getPoint(MIDDLE_FINGER_PIP).getX();
                    }
                    case RING_FINGER_TIP -> {
                        return hand.getPoint(landmark).getX() < hand.getPoint(RING_FINGER_PIP).getX();
                    }
                    case PINKY_TIP -> {
                        return hand.getPoint(landmark).getX() < hand.getPoint(PINKY_PIP).getX();
                    }
                }
            }
            case RIGHT -> {
                switch(landmark)
                {
                    case THUMB_TIP -> {
                        return hand.getPoint(landmark).getY() > hand.getPoint(THUMB_MCP).getY();
                    }
                    case INDEX_FINGER_TIP -> {
                        return hand.getPoint(landmark).getX() > hand.getPoint(INDEX_FINGER_PIP).getX();
                    }
                    case MIDDLE_FINGER_TIP -> {
                        return hand.getPoint(landmark).getX() > hand.getPoint(MIDDLE_FINGER_PIP).getX();
                    }
                    case RING_FINGER_TIP -> {
                        return hand.getPoint(landmark).getX() > hand.getPoint(RING_FINGER_PIP).getX();
                    }
                    case PINKY_TIP -> {
                        return hand.getPoint(landmark).getX() > hand.getPoint(PINKY_PIP).getX();
                    }
                }
            }
            case UP -> {
                switch(landmark)
                {
                    case THUMB_TIP -> {
                        return hand.getPoint(landmark).getX() < hand.getPoint(THUMB_MCP).getX();
                    }
                    case INDEX_FINGER_TIP -> {
                        return hand.getPoint(landmark).getY() > hand.getPoint(INDEX_FINGER_PIP).getY();
                    }
                    case MIDDLE_FINGER_TIP -> {
                        return hand.getPoint(landmark).getY() > hand.getPoint(MIDDLE_FINGER_PIP).getY();
                    }
                    case RING_FINGER_TIP -> {
                        return hand.getPoint(landmark).getY() > hand.getPoint(RING_FINGER_PIP).getY();
                    }
                    case PINKY_TIP -> {
                        return hand.getPoint(landmark).getY() > hand.getPoint(PINKY_PIP).getY();
                    }
                }
            }
            case DOWN -> {
                switch(landmark)
                {
                    case THUMB_TIP -> {
                        return hand.getPoint(landmark).getX() > hand.getPoint(THUMB_MCP).getX();
                    }
                    case INDEX_FINGER_TIP -> {
                        return hand.getPoint(landmark).getY() < hand.getPoint(INDEX_FINGER_PIP).getY();
                    }
                    case MIDDLE_FINGER_TIP -> {
                        return hand.getPoint(landmark).getY() < hand.getPoint(MIDDLE_FINGER_PIP).getY();
                    }
                    case RING_FINGER_TIP -> {
                        return hand.getPoint(landmark).getY() < hand.getPoint(RING_FINGER_PIP).getY();
                    }
                    case PINKY_TIP -> {
                        return hand.getPoint(landmark).getY() < hand.getPoint(PINKY_PIP).getY();
                    }
                }
            }
        }
        return false;
    }

    // Not currently working
    public static boolean getPalmFacingFowards(Hand hand)  // 判断手掌朝前 
    {
        // Find raw direction (regardless of hands rotation)
        if(hand.getPoint(INDEX_FINGER_MCP).getX() > hand.getPoint(PINKY_MCP).getX())
        {
            // Check if hand facing down or up and use to deduce if facing forward or back.
            if(hand.getPoint(WRIST).getY() < hand.getPoint(INDEX_FINGER_MCP).midpoint(hand.getPoint(PINKY_MCP)).getY())
            {
                return false;
            } else {
                return true;
            }
        } else {
            // Check if hand facing down or up and use to deduce if facing forward or back.
            if(hand.getPoint(WRIST).getY() < hand.getPoint(INDEX_FINGER_MCP).midpoint(hand.getPoint(PINKY_MCP)).getY())
            {
                return true;
            } else {
                return false;
            }
        }
    }

    public static HandOrientation getOrientation(Hand hand) {

        boolean palmForwards = getPalmFacingFowards(hand);
        // ISSUE: LEFT-RIGHT ONLY WORKS WITH PALM FACING FORWARDS
        // Creates 4 Points at the Midpoints of a box around the palm.
        // Highest point is used to determine current direction.
        Point3D leftPoint = hand.getPoint(INDEX_FINGER_MCP).midpoint(hand.getPoint(WRIST));
        Point3D rightPoint = hand.getPoint(PINKY_MCP).midpoint(hand.getPoint(WRIST));
        Point3D upperPoint = hand.getPoint(INDEX_FINGER_MCP).midpoint(hand.getPoint(PINKY_MCP));
        Point3D lowerPoint = hand.getPoint(WRIST);
        if( 
            leftPoint.getY() < rightPoint.getY() 
            && leftPoint.getY() < upperPoint.getY() 
            && leftPoint.getY() < lowerPoint.getY()
        )
        {
            if(palmForwards) {
                return HandOrientation.RIGHT;
            } else {
                return HandOrientation.LEFT;
            }
        } else if (rightPoint.getY() < upperPoint.getY() && rightPoint.getY() < lowerPoint.getY()) {
            if(palmForwards) {
                return HandOrientation.LEFT;
            } else {
                return HandOrientation.RIGHT;
            }
        } else if (upperPoint.getY() < lowerPoint.getY()) {
            return HandOrientation.UP;
        } else {
            return HandOrientation.DOWN;
        }
    }

    @Override
    public GestureEvaluationResult evaluate(Hand hand, HandMetadataAnalyser analyser) {
        var metadata = analyser.analyse(hand);

        var map = evaluators.entrySet()
                .stream()
                .map(entry -> new Pair<>(entry.getKey(), entry.getValue().apply(hand, metadata)))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));

        return new GestureEvaluationResult(map);
    }

    // TODO: take into account z
    // TODO: allow setting thresholds
    /*private double evalThumbIndexPinch(Hand hand, HandMetadata metadata) {
        // TODO: double range map
        return hand.getPoint(THUMB_TIP).distance(hand.getPoint(INDEX_FINGER_TIP)) < 0.05 ? 1.0 : 0.0;
    }*/

    /*private double evalThumbPinkyPinch(Hand hand, HandMetadata metadata) {
        // TODO: double range map
        return hand.getPoint(THUMB_TIP).distance(hand.getPoint(PINKY_TIP)) < 0.05 ? 1.0 : 0.0;
    }*/

    /*private double evalThumbMiddlePinch(Hand hand, HandMetadata metadata) {
        // TODO: double range map
        return hand.getPoint(THUMB_TIP).distance(hand.getPoint(MIDDLE_FINGER_TIP)) < 0.05 ? 1.0 : 0.0;
    }*/

    /*private double evalThumbRingPinch(Hand hand, HandMetadata metadata) {
        // TODO: double range map
        return hand.getPoint(THUMB_TIP).distance(hand.getPoint(RING_FINGER_TIP)) < 0.05 ? 1.0 : 0.0;
    }*/

    private double evalMiddleIndexThumbDown(Hand hand, HandMetadata metadata) {
        return (isFingerDown(hand, MIDDLE_FINGER_TIP)
                && isFingerDown(hand, INDEX_FINGER_TIP)
                && isFingerDown(hand, THUMB_TIP)
                && !isFingerDown(hand, RING_FINGER_TIP)
                && !isFingerDown(hand, PINKY_TIP)
        ) ? 1.0 : 0.0;
    }

    private double evalPeaceSign(Hand hand, HandMetadata metadata)
    {
        return (isFingerDown(hand, THUMB_TIP)
                && isFingerDown(hand, RING_FINGER_TIP)
                && isFingerDown(hand, PINKY_TIP)
                && !isFingerDown(hand, INDEX_FINGER_TIP)
                && !isFingerDown(hand, MIDDLE_FINGER_TIP)
                && hand.getPoint(INDEX_FINGER_TIP).distance(hand.getPoint(MIDDLE_FINGER_TIP)) > 0.05
        ) ? 1.0 : 0.0;
    }

    private double evalThumbsUp(Hand hand, HandMetadata metadata)
    {
        return(isFingerDown(hand, INDEX_FINGER_TIP)
                && isFingerDown(hand, MIDDLE_FINGER_TIP)
                && isFingerDown(hand, RING_FINGER_TIP)
                && isFingerDown(hand, PINKY_TIP)
                && !isFingerDown(hand, THUMB_TIP)
                && hand.getPoint(THUMB_TIP).getY() < hand.getPoint(WRIST).midpoint(hand.getPoint(MIDDLE_FINGER_TIP)).getY()
        ) ? 1.0 : 0.0;
    }

    private double evalThumbsDown(Hand hand, HandMetadata metadata)
    {
        return(isFingerDown(hand, INDEX_FINGER_TIP)
                && isFingerDown(hand, MIDDLE_FINGER_TIP)
                && isFingerDown(hand, RING_FINGER_TIP)
                && isFingerDown(hand, PINKY_TIP)
                && !isFingerDown(hand, THUMB_TIP)
                && hand.getPoint(THUMB_TIP).getY() > hand.getPoint(WRIST).midpoint(hand.getPoint(MIDDLE_FINGER_TIP)).getY()
        ) ? 1.0 : 0.0;
    }

    private double evalFingerGun(Hand hand, HandMetadata metadata)
    {
        return(!isFingerDown(hand, INDEX_FINGER_TIP)
                && isFingerDown(hand, MIDDLE_FINGER_TIP)
                && isFingerDown(hand, RING_FINGER_TIP)
                && isFingerDown(hand, PINKY_TIP)
                && !isFingerDown(hand, THUMB_TIP)
                && getOrientation(hand) != HandOrientation.DOWN
                && hand.getPoint(WRIST).midpoint(hand.getPoint(PINKY_TIP)).getY() > hand.getPoint(WRIST).midpoint(hand.getPoint(INDEX_FINGER_TIP)).getY()
        ) ? 1.0 : 0.0;
    }

    private double evalOKSign(Hand hand, HandMetadata metadata)
    {
        return (!isFingerDown(hand, MIDDLE_FINGER_TIP)
                && !isFingerDown(hand, RING_FINGER_TIP)
                && !isFingerDown(hand, PINKY_TIP)
                && hand.getPoint(THUMB_TIP).distance(hand.getPoint(INDEX_FINGER_TIP)) < 0.07  // 大拇指 食指 顶点在一起 
                && getPalmFacingFowards(hand)
        ) ? 1.0 : 0.0;
    }
    
    
}






