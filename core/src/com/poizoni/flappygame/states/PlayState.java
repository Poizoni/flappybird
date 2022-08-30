package com.poizoni.flappygame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.poizoni.flappygame.FlappyBird;
import com.poizoni.flappygame.sprites.Bird;
import com.poizoni.flappygame.sprites.Tube;

public class PlayState extends State {
    private static final int TUBE_SPACING = 125;
    private static final int TUBE_COUNT = 4;
    private static final int GROUND_Y_OFFSET = -50;

    private Bird bird;
    private Texture bg;
    private Texture ground;
    private Texture gameoverImg;
    private Vector2 groundPos1, groundPos2;
    private Array<Tube> tubes;

    private boolean gameover;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        bird = new Bird(50, 300);
        cam.setToOrtho(false, FlappyBird.WIDTH / 2, FlappyBird.HEIGHT / 2);
        bg = new Texture("bg.png");
        ground = new Texture("ground.png");
        gameoverImg = new Texture("gameover.png");

        tubes = new Array<Tube>();
        for(int i=1 ; i<=TUBE_COUNT ; i++) {
            tubes.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)));
        }
        groundPos1 = new Vector2(cam.position.x - cam.viewportWidth / 2, GROUND_Y_OFFSET);
        groundPos2 = new Vector2((cam.position.x - cam.viewportWidth / 2) + ground.getWidth(), GROUND_Y_OFFSET);
        gameover = false;
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()) {
            if(gameover)
                gsm.set(new PlayState(gsm));
            else
                bird.jump();
        }
    }

    @Override
    public void update(float delta) {
        handleInput();
        updateGround();
        bird.update(delta);
        cam.position.x = bird.getPosition().x + 80;
        for(int i= 0 ; i<tubes.size ; i++) {
            Tube tube = tubes.get(i);

            // if tube is off left of screen
            if(cam.position.x - (cam.viewportWidth / 2) > tube.getPosTopTube().x + tube.getTopTube().getWidth()) {
                tube.reposition(tube.getPosBotTube().x +((Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT));
            }
            if(tube.collides(bird.getBounds())) {
                bird.colliding = true;
                gameover = true;
            }
        }
        if(bird.getPosition().y <= ground.getHeight() + GROUND_Y_OFFSET) {
            bird.colliding = true;
            gameover = true;
        }
        cam.update();
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        batch.draw(bg, cam.position.x - (cam.viewportWidth / 2), 0);
        batch.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y);
        for(Tube tube : tubes) {
            batch.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            batch.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
        }
        batch.draw(ground, groundPos1.x, groundPos1.y);
        batch.draw(ground, groundPos2.x, groundPos2.y);
        if(gameover)
            batch.draw(gameoverImg, cam.position.x - gameoverImg.getWidth() / 2, cam.position.y);
        batch.end();
    }

    private void updateGround() {
        if(cam.position.x - (cam.viewportWidth / 2) > groundPos1.x + ground.getWidth())
            groundPos1.add(ground.getWidth() * 2, 0);
        if(cam.position.x - (cam.viewportWidth / 2) > groundPos2.x + ground.getWidth())
            groundPos2.add(ground.getWidth() * 2, 0);
    }

    @Override
    public void dispose() {
        bg.dispose();
        bird.dispose();
        for(Tube tube : tubes)
            tube.dispose();
        ground.dispose();
        gameoverImg.dispose();
        // System.out.println("Play State disposed");
    }
}
