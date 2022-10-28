package com.eron.javafx.handtrack.tracking;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public interface GestureEvaluator {

    GestureEvaluationResult evaluate(Hand hand, HandMetadataAnalyser analyser);
}
