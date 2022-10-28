package com.eron.javafx.handtrack.tracking.impl;

import com.eron.javafx.handtrack.tracking.Hand;
import com.eron.javafx.handtrack.tracking.HandMetadata;
import com.eron.javafx.handtrack.tracking.HandMetadataAnalyser;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class SimpleHandMetadataAnalyser implements HandMetadataAnalyser {

    @Override
    public HandMetadata analyse(Hand hand) {
        return new HandMetadata(false);
    }
}
