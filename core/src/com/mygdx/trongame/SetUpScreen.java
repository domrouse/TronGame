package com.mygdx.trongame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class SetUpScreen implements Screen {

    private Stage stage;

    private Orchestrator parent; // a field to store our orchestrator

    private Label titleLabel, blankspacelabel;
    private Label NoPvaluelabel,screenvvaluelabel, numberOfPlayersLabel, invOnOffLabel, shrinkOnOffLabel;
    private Slider numberOfPlayersSlider, screenshrinkSlider;

    public SetUpScreen(Orchestrator orchestrator){
        parent = orchestrator;

        stage = new Stage(new ScreenViewport());
//        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
//        stage.draw();

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

        final TextButton playButton = new TextButton("Play!", skin, "small"); // the extra argument here "small" is used to set the button to the smaller version instead of the big default version
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(Orchestrator.APPLICATION2);
            }
        });

        //Invisibility power ups on or off
        final CheckBox invCheckbox = new CheckBox(null, skin);
        invCheckbox.setChecked( parent.getPreferences().isInvEnabled() );
        invCheckbox.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = invCheckbox.isChecked();
                parent.getPreferences().setInvEnabled( enabled );
                return false;
            }
        });

        //Screen shrink on or off
        final CheckBox shrinkCheckbox = new CheckBox(null, skin);
        shrinkCheckbox.setChecked( parent.getPreferences().isShrinkEnabled() );
        shrinkCheckbox.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = shrinkCheckbox.isChecked();
                parent.getPreferences().setShrinkEnabled( enabled );
                return false;
            }
        });

        numberOfPlayersSlider = new Slider( 2, 8, 1,false, skin );
        numberOfPlayersSlider.setValue(parent.getPreferences().getNoP());
        numberOfPlayersSlider.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                parent.getPreferences().setNoP( numberOfPlayersSlider.getValue() );
                return false;
            }
        });

        screenshrinkSlider = new Slider( 0, 12, 4,false, skin );
        screenshrinkSlider.setValue(parent.getPreferences().getScreenVelocity());
        screenshrinkSlider.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                parent.getPreferences().setScreenVelocity( screenshrinkSlider.getValue() );
                return false;
            }
        });

        titleLabel = new Label( "Set-up", skin );
        blankspacelabel = new Label( "                ", skin );
        numberOfPlayersLabel = new Label( "No. of players", skin );
        NoPvaluelabel = new Label(Float.toString(numberOfPlayersSlider.getValue()),skin);
        screenvvaluelabel = new Label(Float.toString(screenshrinkSlider.getValue()),skin);
        invOnOffLabel = new Label( "Power ups", skin );
        shrinkOnOffLabel = new Label( "Map shrink", skin );

        table.add(titleLabel).colspan(2);
        table.add(blankspacelabel).right();
        table.row().pad(10,0,0,10);
        table.add(numberOfPlayersLabel).left();
        table.add(numberOfPlayersSlider);
        table.add(NoPvaluelabel);
        table.row().pad(10,0,0,10);
        table.add(invOnOffLabel).left();
        table.add(invCheckbox);
        table.row().pad(10,0,0,10);
        table.add(shrinkOnOffLabel).left();
        table.add(screenshrinkSlider);
        table.add(screenvvaluelabel);
        table.row().pad(10,0,0,10);
        table.add(backButton);
        table.add(playButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        NoPvaluelabel.setText(Float.toString(numberOfPlayersSlider.getValue()));
//        screenvvaluelabel.setText(Float.toString(screenshrinkSlider.getValue()));
        screenvvaluelabel.setText(screenvLabel(screenshrinkSlider.getValue()));
    }

    private String screenvLabel(float sliderInput){
        if (sliderInput==0){
            return "Off";
        }else if(sliderInput/4==1){
            return "Normal";
        }else if(sliderInput/4==2){
            return "Hard";
        }else {return "Insane";}
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
