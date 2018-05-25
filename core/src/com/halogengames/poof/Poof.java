package com.halogengames.poof;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.halogengames.poof.dataLoaders.AssetManager;
import com.halogengames.poof.dataLoaders.GameData;
import com.halogengames.poof.dataLoaders.SoundManager;
import com.halogengames.poof.dataLoaders.TilePower;
import com.halogengames.poof.database.CoreDB;
import com.halogengames.poof.library.BasicShapes;
import com.halogengames.poof.screens.SplashScreen;
import com.halogengames.poof.widgets.Background;

public class Poof extends Game {
	//virtual screen sizes
	public static final int V_WIDTH = 540;
	public static final int V_HEIGHT = 960;

	//Cam and viewport
	public static OrthographicCamera CAM;
	public static Viewport VIEW_PORT;

	//Fonts
	public static FreeTypeFontGenerator labelFontGenerator;
	public static FreeTypeFontGenerator valueFontGenerator;

	//Drawing Objects
	public SpriteBatch batch;
	public ShapeRenderer renderer;
	public BasicShapes shaper;

	//DB object
    public CoreDB db;

	//todo: why is this here and not in asset manager
	public static Background bg;

	public Poof(CoreDB db){
        this.db = db;
	}

	@Override
	public void create () {
		//TableCreator creator = new TableCreator();

		batch = new SpriteBatch();
		renderer = new ShapeRenderer();
		shaper = new BasicShapes(this);

		//defining Background
		bg = new Background(this);

		//Camera and viewport
		CAM = new OrthographicCamera(Poof.V_WIDTH, Poof.V_HEIGHT);
		CAM.setToOrtho(false, Poof.V_WIDTH, Poof.V_HEIGHT);
		VIEW_PORT = new FitViewport( Poof.V_WIDTH, Poof.V_HEIGHT, CAM);
		CAM.position.set( VIEW_PORT.getWorldWidth()/2, VIEW_PORT.getWorldHeight()/2, 0);

		//Font
		labelFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/rock_solid.TTF"));
		valueFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/rock_solid.TTF"));

		//Init GameData
		TilePower.init();
		GameData.init();
		AssetManager.init();
		SoundManager.init();

		//Catch the back nav button
		Gdx.input.setCatchBackKey(true);

		//using "this" so that the main menu screen can use sprite batch
		setScreen(new SplashScreen(this));
	}

//	@Override
	public void render () {
		//Game class delegates to the current screen hence using super
		//since the func only uses super call, no need for this func as in absence of Poof.render, super.render will get called in place if exists
		super.render();
	}

	@Override
	public void dispose () {
		//dispose objects for memory cleanup
		batch.dispose();
		SoundManager.dispose();
	}

	@Override
	public void resize(int width, int height) {
		Poof.VIEW_PORT.update( width, height);
	}
}

