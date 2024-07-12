package com.mygdx.trongame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class ControlsScreen implements Screen {

    private Stage stage;

    private Orchestrator parent; // a field to store our orchestrator

    private Label titleLabel;
    private Label player, moveleft, moveright;
    private Label p1, left1, right1;
    private Label p2, left2, right2;
    private Label p3, left3, right3;
    private Label p4, left4, right4;
    private Label p5, left5, right5;
    private Label p6, left6, right6;
    private Label p7, left7, right7;
    private Label p8, left8, right8;
    private Texture trail1Texture, trail2Texture, trail3Texture, trail4Texture;
    private Texture trail5Texture, trail6Texture, trail7Texture, trail8Texture;
    private Image trail1, trail2,trail3,trail4, trail5, trail6, trail7, trail8;
    private Slider numberOfPlayersSlider;

    public ControlsScreen(Orchestrator orchestrator){
        parent = orchestrator;

        stage = new Stage(new ScreenViewport());
//        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
//        stage.draw();
        trail1Texture = new Texture("p1.png");
        trail2Texture = new Texture("p2.png");
        trail3Texture = new Texture("p3.png");
        trail4Texture = new Texture("p4.png");
        trail5Texture = new Texture("p5.png");
        trail6Texture = new Texture("p6.png");
        trail7Texture = new Texture("p7.png");
        trail8Texture = new Texture("p8.png");
        trail1 = new Image(trail1Texture);
        trail2 = new Image(trail2Texture);
        trail3 = new Image(trail3Texture);
        trail4 = new Image(trail4Texture);
        trail5 = new Image(trail5Texture);
        trail6 = new Image(trail6Texture);
        trail7 = new Image(trail7Texture);
        trail8 = new Image(trail8Texture);

    }

    @Override
    public void show() {
        stage.clear();
        Gdx.input.setInputProcessor(stage);

        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table();
        table.setFillParent(true);
//        table.setDebug(true);
        stage.addActor(table);

        // create button skin
        Skin skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));

        final TextButton backButton = new TextButton("Back", skin, "small"); // the extra argument here "small" is used to set the button to the smaller version instead of the big default version
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(Orchestrator.MENU);
            }
        });


        titleLabel = new Label( "Controls", skin );
        player = new Label("Player",skin);
        moveleft = new Label("Move left",skin);
        moveright = new Label("Move right",skin);
        p1 = new Label("1",skin);
        left1 = new Label("DPAD_LEFT",skin);
        right1 = new Label("DPAD_RIGHT",skin);
        p2 = new Label("2",skin);
        left2 = new Label("1",skin);
        right2 = new Label("2",skin);
        p3 = new Label("3",skin);
        left3 = new Label("S",skin);
        right3 = new Label("D",skin);
        p4 = new Label("4",skin);
        left4 = new Label("V",skin);
        right4 = new Label("B",skin);
        p5 = new Label("5",skin);
        left5 = new Label("6",skin);
        right5 = new Label("7",skin);
        p6 = new Label("6",skin);
        left6 = new Label("O",skin);
        right6 = new Label("P",skin);
        p7 = new Label("7",skin);
        left7 = new Label("SHIFT_RIGHT",skin);
        right7 = new Label("ENTER",skin);
        p8 = new Label("8",skin);
        left8 = new Label("SPACE",skin);
        right8 = new Label("M",skin);


        table.add(titleLabel).colspan(3);
        table.row().pad(10,0,0,10);
        table.add(player).left();
        table.add(moveleft);
        table.add(moveright).right();
        table.row().pad(5,0,0,10);
        table.add(trail1).left().width(15f).height(15f);
        table.add(left1);
        table.add(right1).right();
        table.row().pad(5,0,0,10);
        table.add(trail2).left().width(15f).height(15f);
        table.add(left2);
        table.add(right2).right();
        table.row().pad(10,0,0,10);
        table.add(trail3).left().width(15f).height(15f);
        table.add(left3);
        table.add(right3).right();
        table.row().pad(10,0,0,10);
        table.add(trail4).left().width(15f).height(15f);
        table.add(left4);
        table.add(right4).right();
        table.row().pad(10,0,0,10);
        table.add(trail5).left().width(15f).height(15f);
        table.add(left5);
        table.add(right5).right();
        table.row().pad(10,0,0,10);
        table.add(trail6).left().width(15f).height(15f);
        table.add(left6);
        table.add(right6).right();
        table.row().pad(10,0,0,10);
        table.add(trail7).left().width(15f).height(15f);
        table.add(left7);
        table.add(right7).right();
        table.row().pad(10,0,0,10);
        table.add(trail8).left().width(15f).height(15f);
        table.add(left8);
        table.add(right8).right();
        table.row().pad(10,0,0,10);
        table.add(backButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
