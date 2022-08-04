package com.eron.javafx.fxgl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;

public class TestFXGLExample extends GameApplication {

    private static final Logger log = LoggerFactory.getLogger(TestFXGLExample.class);

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(600);
        
        settings.isIntroEnabled();
    }

    public static void main(String[] args) {
        GameApplication.launch(args);
    }

}
