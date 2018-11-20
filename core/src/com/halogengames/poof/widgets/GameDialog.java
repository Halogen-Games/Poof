package com.halogengames.poof.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.halogengames.poof.Poof;

import java.util.concurrent.Callable;
//Todo: incomplete class
public class GameDialog extends Widget {

    private Poof game;
    private String text;
    private Stage stage;
    private Table table;

    private float gutter;
    private float buttonTopPad;

    private float textHeight;

    public GameDialog(String text, Poof game){
        this.game = game;
        this.text = text;

        this.game.widgetStack.push(this);

        //table and stage
        stage = new Stage(Poof.VIEW_PORT, game.batch);
        table = new Table();
        table.bottom();

        setWidth(Poof.V_WIDTH*0.8f);

        gutter = this.getWidth()*0.05f;
        buttonTopPad = gutter*2;

        GlyphLayout layout = new GlyphLayout();
        layout.setText(game.assetManager.helpTextFont, text,new Color(Color.GRAY), this.getWidth() - 2*gutter,Align.center, true);
        textHeight = layout.height;

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);

        this.setHeight(textHeight + 2*gutter + buttonTopPad);
        this.sizeChanged();
    }

    public void addButton(String label, final Callable listener){
        TextButton button = new TextButton(label, this.game.assetManager.levelSelectButtonStyle);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.soundManager.playButtonTap();
                game.widgetStack.pop();
                try{
                    listener.call();
                }catch(Exception e){
                    throw new RuntimeException("Dialogue button func error");
                }
            }
        });
        table.add(button).expandX().padTop(buttonTopPad).padBottom(gutter);
        this.setHeight(textHeight + 2*gutter + buttonTopPad + button.getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.game.renderer.setProjectionMatrix(Poof.CAM.combined);
        this.game.batch.setProjectionMatrix(Poof.CAM.combined);

        //Draw bg
        this.game.renderer.begin(ShapeRenderer.ShapeType.Filled);

        this.game.shaper.setLineWidth(5);
        this.game.renderer.setColor(new Color(Color.BLACK));
        this.game.shaper.drawRoundRectLine(this.getX(),this.getY(),this.getWidth(),this.getHeight(),10);

        this.game.renderer.setColor(new Color(Color.WHITE));
        this.game.shaper.drawRoundRectFilled(this.getX(),this.getY(),this.getWidth(),this.getHeight(),10);

        this.game.renderer.end();

        //draw text
        game.batch.begin();
        this.game.assetManager.helpTextFont.draw(
                game.batch,
                text,
                this.getX() + gutter,
                this.getY() - gutter + this.getHeight(),
                this.getWidth() - 2*gutter,
                Align.center,
                true
        );
        game.batch.end();

        //draw buttons
        stage.draw();
    }

    @Override
    protected void sizeChanged() {
        this.setPosition(Poof.VIEW_PORT.getWorldWidth()/2 - this.getWidth()/2, Poof.VIEW_PORT.getWorldHeight()/2 - this.getHeight()/2);
        table.setWidth(this.getWidth());
        table.setPosition(this.getX(),this.getY());
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
    }

    private void dispose(){
        System.out.println("Dialog Disposed");
        stage.dispose();
    }
}
