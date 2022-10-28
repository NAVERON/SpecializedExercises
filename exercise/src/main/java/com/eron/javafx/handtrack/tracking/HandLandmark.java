package com.eron.javafx.handtrack.tracking;

/**
 * The ordinal of each item matches the format defined at:
 * https://google.github.io/mediapipe/solutions/hands.html#hand-landmark-model
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public enum HandLandmark {
    WRIST,

    THUMB_CMC,  // 拇指 
    THUMB_MCP,
    THUMB_IP,
    THUMB_TIP,

    INDEX_FINGER_MCP,
    INDEX_FINGER_PIP,
    INDEX_FINGER_DIP,
    INDEX_FINGER_TIP,

    MIDDLE_FINGER_MCP,  // 中指 
    MIDDLE_FINGER_PIP,
    MIDDLE_FINGER_DIP,
    MIDDLE_FINGER_TIP,

    RING_FINGER_MCP,
    RING_FINGER_PIP,
    RING_FINGER_DIP,
    RING_FINGER_TIP,

    PINKY_MCP,
    PINKY_PIP,
    PINKY_DIP,
    PINKY_TIP
}
