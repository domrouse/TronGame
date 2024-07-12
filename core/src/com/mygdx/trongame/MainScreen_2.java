package com.mygdx.trongame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class MainScreen_2 implements Screen {

    //number of players
    private int NoP;


    private Stage stage;
    private Orchestrator parent; // a field to store our orchestrator
    private TextureAtlas textureAtlas;
    private SpriteBatch batch;
    private FitViewport viewport;
    private OrthographicCamera cam;
    private ShapeRenderer sr;

    //screen decls
    private float width, height, screenL, screenR, screenT, screenB;
    private Rectangle screen;
    private BitmapFont font;

    //decs for all players
    private float v=150f, sizex=10f, sizey=10f, keyTimerMax=0.15f, startTimerMax=0.2f, startTimer=startTimerMax;
    private int firstMemoryIndex=0, initialise=0;
    private int[] isAliveArray, clockwiseKeys, anticlockwiseKeys;
    private float[] keyTimer, dir, newPos, xpos_new, ypos_new;
    private float[][] xpos, ypos; // matrices for trails of all NoP players
    private Sprite p1, p2, p3, p4, p5, p6, p7, p8;
    private boolean[] inv;
    private Sprite[] sprites;

    //decs for power ups
    private boolean QInv;
    private Sprite powerup, pinv;
    private int isPowerUp=0, keyPressesMax=2;
    private int[] keyPresses;
    private float sizexp=5*sizex, sizeyp=5*sizey, xpup, ypup, xpupSave, ypupSave, resize, reshift;
    private float timerPowerUpMax=10f, timerPowerUp=timerPowerUpMax; //power up moves position of not claimed
    private Rectangle powerupRect;

    //decs for screen shrinking
    private float screenv;

    Texture trail1, trail2, trail3, trail4, trail5, trail6, trail7, trail8, trailinv;
    Texture[] trails;
    float [] rotateArray1;

    Random rand = new Random();

    public MainScreen_2(Orchestrator orchestrator){
        parent = orchestrator;
        sr = new ShapeRenderer();
        stage = new Stage(new ScreenViewport());

        //screen properties
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        screen = new Rectangle(0, 0, width, height);
        screenL = screen.getX();
        screenB = screen.getY();
        screenR = screenL + width;
        screenT = screenB + height;

        // camera and viewport
        cam = new OrthographicCamera();
        cam.setToOrtho(false,width, height);
        viewport = new FitViewport(width,height,cam);

        // sprites
        textureAtlas = new TextureAtlas("sprites.txt");
        batch = new SpriteBatch();
        p1=textureAtlas.createSprite("p1");
        p2=textureAtlas.createSprite("p2");
        p3=textureAtlas.createSprite("p3");
        p4=textureAtlas.createSprite("p4");
        p5=textureAtlas.createSprite("p5");
        p6=textureAtlas.createSprite("p6");
        p7=textureAtlas.createSprite("p7");
        p8=textureAtlas.createSprite("p8");
        sprites = new Sprite[] {p1,p2,p3,p4,p5,p6,p7,p8};
        powerup=textureAtlas.createSprite("powerup");
        pinv=textureAtlas.createSprite("invinsibility");

        //keys to rotate the players
        //{DPAD_RIGHT,2,S,B,7,P,ENTER,M}
        //{DPAD_LEFT,1,A,V,6,O,SHIFT_RIGHT,SPACE}
        clockwiseKeys=new int[] {22,9,32,30,14,44,66,41};
        anticlockwiseKeys=new int[] {21,8,47,50,13,43,60,62};
//        Gdx.input.Keys.D

        // player trails
        trail1 = new Texture("p1trail.png");
        trail2 = new Texture("p2trail.png");
        trail3 = new Texture("p3trail.png");
        trail4 = new Texture("p4trail.png");
        trail5 = new Texture("p5trail.png");
        trail6 = new Texture("p6trail.png");
        trail7 = new Texture("p7trail.png");
        trail8 = new Texture("p8trail.png");
        trails= new Texture[] {trail1, trail2, trail3, trail4, trail5, trail6, trail7, trail8};
        trailinv = new Texture("invinsibility.png");

        // font
        font = new BitmapFont();
        font.setColor(Color.GREEN);
    }

    @Override
    public void show() {
        stage.clear();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        //get number of players, screen velocity and if invisiblity and screen shrink from preferences
        NoP = (int) parent.getPreferences().getNoP();
        screenv = parent.getPreferences().getScreenVelocity();
        QInv = parent.getPreferences().isInvEnabled();

        //~~~~~~~~~~~~~~start players in initial go~~~~~~~~~~~~~~~~
        if(initialise==0){
            isAliveArray = new int[NoP];
            keyTimer = new float[NoP];
            xpos = new float[NoP][1];
            ypos = new float[NoP][1];
            dir = new float[NoP];
            inv = new boolean[NoP];
            keyPresses = new int[NoP];
            for (int i = 0; i < NoP; i++) {
                float[] position1 = initialSpritePosition(width, height);
                isAliveArray[i]=1;
                keyTimer[i]=0;
                xpos[i][0]=position1[0];
                ypos[i][0]=position1[1];
                dir[i]=position1[2];
                inv[i]=false;
                keyPresses[i]=0;
            }
            initialise=1;
        }

        //~~~~~~~~~~~reset screen with black background~~~~~~~~~~~~~~~~~~~
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        cam.update();

        //debug outputs
//        batch.begin();
//        font.draw(batch, "screenv = " + screenv, 0.75f*width,  0.9f*height);
//        batch.end();

        //~~~~~~~~~~~~~~~~shrink the screen over time
        screenT-=screenv*Gdx.graphics.getDeltaTime();
        screenR-=screenv*Gdx.graphics.getDeltaTime();
        screenB+=screenv*Gdx.graphics.getDeltaTime();
        screenL+=screenv*Gdx.graphics.getDeltaTime();

        //~~~~~~~~~~~~~~~~move sprite forward at speed v in direction it is facing~~~~~~~~~~~~~
        xpos_new = new float[NoP];
        ypos_new = new float[NoP];
        for (int i = 0; i < NoP ; i++) {
            //calculate new positions
            newPos=moveSprite(xpos[i][0], ypos[i][0], v, dir[i], isAliveArray[i]);
            //create appending arrays
            xpos_new[i]=newPos[0];
            ypos_new[i]=newPos[1];
        }
        xpos=append2(xpos,xpos_new);
        ypos=append2(ypos,ypos_new);

        //~~~~~~~~~~~~~~~~update trails~~~~~~~~~~~~~~~~~~~~~~~
        batch.begin();
        for (int i = 0; i < NoP; i++) {
            if(!inv[i]) {//if not powered up then draw normal trail
                drawTrail(trails[i], xpos[i], ypos[i]);
            }else{//if powered up draw inv trail
                drawTrail(trailinv, xpos[i], ypos[i]);
            }
        }
        batch.end();

        //~~~~~~~~~~~~~~if there is no power up or timer has ran out, set new position~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        if(QInv && (isPowerUp==0 || timerPowerUp<=0)){
            xpupSave = rand.nextFloat()*(screenR-screenL-sizexp)+screenL;
            ypupSave = rand.nextFloat()*(screenT-screenB-sizeyp)+screenB;
            xpup=xpupSave;
            ypup=ypupSave;
            powerup.setSize(sizexp,sizeyp);
            timerPowerUp=timerPowerUpMax; //start timer till power up moves
            isPowerUp=1; //there is now a power up
        }

        //~~~~~~~~~~~~~~~~~~draw sprites~~~~~~~~~~~~~~~~~~~~~~~~~~~
        batch.begin();
        for (int i = 0; i < NoP ; i++) {
            drawSprite(sprites[i], xpos[i][0], ypos[i][0], sizex, sizey, isAliveArray[i], inv[i]);
        }

        resize=timerPowerUp/timerPowerUpMax;
        powerup.setSize(sizexp*resize, sizeyp*resize); //shrink power up as it ages

        //need to adjust position so it shrinks about centre
        reshift=(1-timerPowerUp)/timerPowerUpMax;
        xpup=xpupSave+sizexp*reshift/2;
        ypup=ypupSave+sizeyp*reshift/2;
        powerup.setPosition(xpup, ypup);
        powerupRect=powerup.getBoundingRectangle(); //get rectangle for collisions
        if(QInv){powerup.draw(batch);}
        batch.end();

        //~~~~~~~~~~~~~~check to see if power up is outside screen, if so respawn
        if(QInv && isPowerUp==1
                && ((xpup+sizexp*resize/2 < screenL) || (xpup+sizexp*resize/2 > screenR)
                || (ypup+sizeyp*resize/2 < screenB) || (ypup+sizeyp*resize/2 > screenT))){
            isPowerUp=0; //remove this power up
        }

        //~~~~~~~~~~~~~~~~~~check to see if any player collides with power up~~~~~~~~~~~~~~~~~~~~
        if(QInv && isPowerUp==1){
            for (int i = 0; i < NoP; i++) {
                inv[i]=checkPowerUpCollision(powerupRect,sprites[i], inv[i], i);
            }
            timerPowerUp-=Gdx.graphics.getDeltaTime();
        }

        //~~~~~~~~~~~~~~~~~~check to see if any player runs out of power up~~~~~~~~~~~~~~~~~~~~
        if(QInv){
            for (int i = 0; i < NoP; i++) {
                if(keyPresses[i]==0){
                    inv[i]=false;
                }
            }
        }

        //~~~~~~~~~~~~~check to see if the player is within their own trail, if so kill player~~~~~~~~
        if(startTimer>=0){
            startTimer-=Gdx.graphics.getDeltaTime();
        }else if (firstMemoryIndex==0){
            firstMemoryIndex=xpos[0].length;
        }

        for (int i = 0; i < NoP; i++) {
            if(isAliveArray[i]==1) {
                isAliveArray[i] = checkSelfCollision(xpos, ypos, firstMemoryIndex, i, inv[i]);
            }
        }

        //~~~~~~~~~~~~~check to see if the player is within a different players' trail, if so kill player~~~~~~~~//
        for (int i = 0; i < NoP; i++) {
            if(isAliveArray[i]==1) {
                isAliveArray[i] = checkOtherCollision(xpos, ypos, i, inv[i]);
            }
        }

        //~~~~~~~~~~~~~~~~rotate sprite with defined keys~~~~~~~~~~~~~
        for (int i = 0; i < NoP ; i++) {
            rotateArray1 = rotateSprite(sprites[i], keyTimer[i], anticlockwiseKeys[i], clockwiseKeys[i], dir[i], i);
            keyTimer[i] = rotateArray1[0];
            dir[i] = rotateArray1[1];
        }

        //~~~~~~~~~~~~~~~~~~~~~~draw boundaries of the screen~~~~~~~~~~~~~~~~~~~~~~~
        sr.setProjectionMatrix(cam.combined);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(1, 1, 1, 1);
        sr.rect(0, 0,width, height-screenT);
        sr.rect(0, height-(height-screenT),width, height-screenT);
        sr.rect(0, 0,width-screenR, height);
        sr.rect(width-(width-screenR), 0,width-screenR, height);
        sr.end();

        //~~~~~~~~~~~~~~~~~~kill player walking off screen~~~~~~~~~~~~~~~~~~~~~
        for (int i = 0; i < NoP ; i++) {
            if (xpos[i][0] < screenL || xpos[i][0] + sizex > screenR
                    || ypos[i][0] < screenB || ypos[i][0] + sizey > screenT) {
                isAliveArray[i] = 0;
            }
        }

        //~~~~~~~~~~~~~~~~~~end game if all players are dead or press ESC~~~~~~~~~~~~~~~~~~~~~
        if(sumIntArray(isAliveArray)==0 || Gdx.input.isKeyPressed(131)){
            //reset positions and trails
            isAliveArray = new int[NoP];
            keyTimer = new float[NoP];
            xpos = new float[NoP][1];
            ypos = new float[NoP][1];
            dir = new float[NoP];
            for (int i = 0; i < NoP; i++) {
                float[] position1 = initialSpritePosition(width, height);
                isAliveArray[i]=1;
                keyTimer[i]=0;
                xpos[i][0]=position1[0];
                ypos[i][0]=position1[1];
                dir[i]=position1[2];
                inv[i]=false;
                keyPresses[i]=0;
            }

            startTimer=startTimerMax;
            firstMemoryIndex=0;
            initialise=0;
            isPowerUp=0;
            timerPowerUp=timerPowerUpMax;
            screenT=height;
            screenR=width;
            screenB=0;
            screenL=0;

            //change game screen
            parent.changeScreen(Orchestrator.SETUP);
        }

        //~~~~~~~~~~~~reduce key timer~~~~~~~~~~~~
        for (int i = 0; i < NoP; i++) {
            keyTimer[i] -= Gdx.graphics.getDeltaTime();
        }

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(cam.combined);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        sr.dispose();
        batch.dispose();
        stage.dispose();
        font.dispose();
    }

    //-----------
    // FUNCTIONS
    //-----------

    // choose initial direction based on position on screen and draw in correct orientation
    // output the dir information
    public float[] initialSpritePosition(float width, float height) {
        //dir=0,1,2,3 (up,right,down,left)
        float dir;
        float posx=(rand.nextFloat()*0.9f+0.05f)*width;
        float posy=(rand.nextFloat()*0.9f+0.05f)*height;
        float random=rand.nextFloat();

        // get correct orientation
        if(posx<width/2){ // left side of screen
            if(posy<height/2){ // bottom left side of screen
                //can start facing up or right, choose randomly
                if(random<0.5){
                    dir=0f;
                }else{
                    dir=1f;
                }
            }else{//top left side of screen
                if(random<0.5){
                    dir=2f;
                }else{
                    dir=1f;
                }
            }
        }else{//right side of screen
            if(posy<height/2){ // bottom right side of screen
                //can start facing up or left, choose randomly
                if(random<0.5){
                    dir=0f;
                }else{
                    dir=3f;
                }
            }else{//top right side of screen
                if(random<0.5){
                    dir=2f;
                }else{
                    dir=3f;
                }
            }

        }

        return new float[] {posx, posy, dir};
    }

    // if alive draw sprite at position with size and set origin
    // if powered up draw as invinsible
    public void drawSprite(Sprite sprite, float x, float y, float sizex, float sizey, int isAlive, boolean Qinv) {
        sprite.setPosition(x, y);
        pinv.setPosition(x, y);
        sprite.setSize(sizex, sizey);
        pinv.setSize(sizex, sizey);
        sprite.setOrigin(sizex/2,sizey/2);
        pinv.setOrigin(sizex/2,sizey/2);
        if(isAlive==1){
            if(!Qinv) {
                sprite.draw(batch);
            }else{
                pinv.draw(batch);
            }
        }
    }

    //rotate player if keyTimer is zero and also restart keyTimer
    //+1 or -1 to sprite direction if rotate clockwise or counterclockwise
    //if invisible remove a key press
    public float[] rotateSprite(Sprite sprite, float keyTimer, int clockwise, int anticlockwise, float dir, int i) {
        if (keyTimer <= 0) {
            if (Gdx.input.isKeyPressed(clockwise)) {
                sprite.rotate(90);
                if(dir==0){
                    dir=3;
                }else{
                    dir=(dir-1)%4;
                }
                if(keyPresses[i]>0){
                    keyPresses[i]-=1;
                }
                keyTimer = keyTimerMax;
            }
            if (Gdx.input.isKeyPressed(anticlockwise)) {
                sprite.rotate(-90);
                dir=(dir+1)%4;
                if(keyPresses[i]>0){
                    keyPresses[i]-=1;
                }
                keyTimer = keyTimerMax;
            }
        }
        return new float[] {keyTimer, dir};
    }

    // if alive draw sprite at position with size and set origin
    public float[] moveSprite(float xpos_current, float ypos_current, float v, float dir, int isAlive) {
        if(isAlive==1) {
            if (dir == 0) {//move up
                ypos_current = ypos_current + v * Gdx.graphics.getDeltaTime();
            } else if (dir == 1) {//move right
                xpos_current = xpos_current + v * Gdx.graphics.getDeltaTime();
            } else if (dir == 2) {//move down
                ypos_current = ypos_current - v * Gdx.graphics.getDeltaTime();
            } else if (dir == 3) {//move left
                xpos_current = xpos_current - v * Gdx.graphics.getDeltaTime();
            }
        }else{
            xpos_current=-width;
            ypos_current=-height;
        }
        return new float[] {xpos_current, ypos_current};
    }

    // append to second [][] of a float[][]
    public float[][] append2(float[][] OldArray, float[] AppendingArray) {
        float[][] result = new float[NoP][OldArray[0].length+1];
        for (int i = 0; i < OldArray.length; i++) {
            for (int j = 0; j < OldArray[0].length+1; j++) {
                if(j==0){//add new values
                    result[i][j]=AppendingArray[i];
                }else{
                    result[i][j]=OldArray[i][j-1];
                }

            }

        }
        return result;
    }


    // draw texture at all coordinates in trail arrays, except at player position
    public void drawTrail(Texture img, float[] xtrail, float[] ytrail) {
        for (int i = 0; i < xtrail.length ; i++) {
            batch.draw(img,xtrail[i],ytrail[i],sizex,sizey);
        }
    }

    // sum an int array
    public int sumIntArray(int[] array) {
        int total=0;
        for (int i = 0; i < array.length ; i++) {
            total+=array[i];
        }
        return total;
    }

    // check to see if player is inside another players trail, if so, kill the player
    public int checkOtherCollision(float[][] xpos, float[][] ypos, int current_player, boolean inv) {
        int isAlive=1;
        float xtrailLeft, xtrailRight, ytrailLower, ytrailUpper;
        float xpos_current=xpos[current_player][0];
        float ypos_current=ypos[current_player][0];
        if(!inv) {
            for (int i = 0; i < xpos.length; i++) {
                if (i != current_player) { // check trail of all other players
                    for (int j = 0; j < xpos[0].length; j++) {
                        xtrailLeft = xpos[i][j] - sizex / 2;
                        xtrailRight = xpos[i][j] + sizex / 2;
                        ytrailLower = ypos[i][j] - sizey / 2;
                        ytrailUpper = ypos[i][j] + sizey / 2;
                        if ((xpos_current - sizex / 2 <= xtrailRight) && (xtrailLeft <= xpos_current + sizex / 2)
                                && (ypos_current - sizey / 2 <= ytrailUpper) && (ytrailLower <= ypos_current + sizey / 2)) {
                            isAlive = 0;
                            break;
                        }
                    }
                }
            }
        }
        return isAlive;
    }

    // check to see if player is inside their own trail, if so, kill the player
    // this is different because need to ignore first few instances of history
    public int checkSelfCollision(float[][] xpos, float[][] ypos, int firstMemoryIndex, int current_player, boolean inv) {
        int isAlive=1;
        float xtrailLeft, xtrailRight, ytrailLower, ytrailUpper;
        float xpos_current=xpos[current_player][0];
        float ypos_current=ypos[current_player][0];
        if(firstMemoryIndex!=0 && !inv) {
            for (int i = firstMemoryIndex; i < xpos[0].length; i++) {
                xtrailLeft=xpos[current_player][i]-sizex/2;
                xtrailRight=xpos[current_player][i]+sizex/2;
                ytrailLower=ypos[current_player][i]-sizey/2;
                ytrailUpper=ypos[current_player][i]+sizey/2;
                if ((xpos_current-sizex/2 <= xtrailRight) && (xtrailLeft <= xpos_current+sizex/2)
                        && (ypos_current-sizey/2 <= ytrailUpper) && (ytrailLower <= ypos_current+sizey/2)) {
                    isAlive = 0;
                    break;
                }
            }
        }
        return isAlive;
    }

    public boolean checkPowerUpCollision(Rectangle powerupRect, Sprite sprite, boolean inv, int i) {
        Rectangle spriteRect = sprite.getBoundingRectangle();
        boolean isOverlaping = spriteRect.overlaps(powerupRect);
        if(isOverlaping){
            isPowerUp=0; //need new power up
            keyPresses[i]=keyPressesMax;
            return (boolean) true;
        }else{
            return inv;
        }
    }
}