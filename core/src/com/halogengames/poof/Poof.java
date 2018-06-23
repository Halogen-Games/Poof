package com.halogengames.poof;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.halogengames.poof.AWS.analytics.AWSPinpointInterface;
import com.halogengames.poof.advertisement.AdInterface;
import com.halogengames.poof.dataLoaders.PoofAssetManager;
import com.halogengames.poof.dataLoaders.GameData;
import com.halogengames.poof.dataLoaders.SoundManager;
import com.halogengames.poof.AWS.database.CoreScoreDB;
import com.halogengames.poof.library.BasicShapes;
import com.halogengames.poof.library.WidgetStack;
import com.halogengames.poof.screens.MainMenuScreen;
import com.halogengames.poof.screens.PrivacyScreen;
import com.halogengames.poof.screens.SplashScreen;
import com.halogengames.poof.widgets.Background;

public class Poof extends Game {
	//Todo: Convert static to non static wherever possible
	//virtual screen sizes
	public static final int V_WIDTH = 540;
	public static final int V_HEIGHT = 960;

	//Cam and viewport
	public static OrthographicCamera CAM;
	public static Viewport VIEW_PORT;
	public static ExtendViewport EXTEND_VIEW_PORT;
	public static float BANNER_AD_SIZE;

	//Asset Manager
    public PoofAssetManager assetManager;
    public SoundManager soundManager;

	//Drawing Objects
	public SpriteBatch batch;
	public ShapeRenderer renderer;
	public BasicShapes shaper;

	//drawable lists
    //todo: move widgetStack in screens
    public WidgetStack widgetStack;

	//DB object
    public AdInterface adManager;

    //AWS Objects
    public CoreScoreDB db;
	public AWSPinpointInterface analyticsManager;

	public Background bg;

	public Poof(CoreScoreDB db, AdInterface adManager, AWSPinpointInterface analyticsManager){
        this.db = db;
        this.adManager = adManager;
        this.adManager.setInterstitialRate(4);
        this.analyticsManager = analyticsManager;

        adManager.setGameHandle(this);
	}

    public void showGDPRConsentPageIfNeeded(){
        this.setScreen(new MainMenuScreen(this));
	    if(adManager.isConsentFormNeeded()) {
            System.out.println("Show Consent Page");
            this.setScreen(new PrivacyScreen(this, this.getScreen()));
        }
    }

	@Override
	public void create () {
		batch = new SpriteBatch();
		renderer = new ShapeRenderer();
		shaper = new BasicShapes(this);

		widgetStack = new WidgetStack(this);

		//defining Background
		bg = new Background(this);

		//Camera and viewport
		CAM = new OrthographicCamera(Poof.V_WIDTH, Poof.V_HEIGHT);
		CAM.setToOrtho(false, Poof.V_WIDTH, Poof.V_HEIGHT);
		VIEW_PORT = new FitViewport( Poof.V_WIDTH, Poof.V_HEIGHT, CAM);
		EXTEND_VIEW_PORT = new ExtendViewport(Poof.V_WIDTH, Poof.V_HEIGHT, CAM);
		CAM.position.set( VIEW_PORT.getWorldWidth()/2, VIEW_PORT.getWorldHeight()/2, 0);

		//todo: set this dynamically somehow
        BANNER_AD_SIZE = 150;

        //Init GameData
        GameData.init(this);

		//todo: make asset and sound managers load assets in parallel
        assetManager = new PoofAssetManager();
        soundManager = new SoundManager(assetManager.manager);

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

		widgetStack.draw();

        //Uncomment below for framerate
//		batch.begin();
//        assetManager.helpTextFont.draw(
//                batch,
//                Float.toString( Math.round(1/Gdx.graphics.getDeltaTime())),
//                0,
//                Poof.VIEW_PORT.getWorldHeight()-100
//        );
//		batch.end();
	}

	@Override
	public void dispose () {
		//dispose objects for memory cleanup
		batch.dispose();

		//below will also dispose sound assets
		assetManager.dispose();
	}

	@Override
	public void resize(int width, int height) {
		Poof.VIEW_PORT.update(width,height);
		Poof.EXTEND_VIEW_PORT.update(width,height);
	}
}

