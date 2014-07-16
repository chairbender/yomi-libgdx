package com.chairbender.yomi;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.chairbender.yomi.ui.PlayFieldGroup;
import com.chairbender.yomi.ui.UIConstants;

public class YomiLibgdx extends ApplicationAdapter {
    private Stage stage;

    private PlayFieldGroup playFieldGroup = new PlayFieldGroup();

    @Override
    public void create () {
        stage = new Stage(new FitViewport(UIConstants.WORLD_WIDTH,UIConstants.WORLD_HEIGHT));
        Gdx.input.setInputProcessor(stage);
        stage.addActor(playFieldGroup);
    }

    @Override
    public void resize (int width, int height) {
        // See below for what true means.
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render () {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
