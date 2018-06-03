package com.halogengames.poof.dataLoaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ArrayMap;
import com.halogengames.poof.Poof;


/**
 * Created by Rohit on 03-10-2017.
 *
 * All styles and their assets are loaded here
 */

public class PoofAssetManager {
    public AssetManager manager;
    FreeTypeFontLoaderParameter labelFontLoader;
    FreeTypeFontLoaderParameter valueFontLoader;

    //Font Generators
    public FreeTypeFontGenerator labelFontGenerator;
    public FreeTypeFontGenerator valueFontGenerator;
    public FreeTypeFontParameter fontParam;

    //Common Assets
    public Texture commonBG;

    //Main Menu Assets
    public LabelStyle mainMenuTitleStyle;
    public TextButtonStyle mainMenuButtonStyle;

    //help Screen Assets
    public BitmapFont helpTextFont;
    public Animation<TextureRegion> helpAnim;

    //Options Screen Assets
    public LabelStyle optionsTitleStyle;
    public TextButtonStyle optionsButtonStyle;
    public SliderStyle optionsMusicSliderStyle;
    public SliderStyle optionsSoundSliderStyle;

    //Privacy Screen Assets
    public LabelStyle privacyTitleStyle;
    public TextButtonStyle privacyButtonStyle;
    public TextButtonStyle privacyPolicyButtonStyle;

    //Level Screen Assets
    public LabelStyle levelSelectTitleStyle;
    public TextButtonStyle levelSelectButtonStyle;
    public TextButtonStyle levelSelectGreyedButtonStyle;

    //Game Over Screen Assets
    public LabelStyle gameOverLabelStyle;
    public TextButtonStyle gameOverButtonStyle;

    //Pause Screen Assets
    public LabelStyle pauseTitleStyle;
    public TextButtonStyle pauseButtonStyle;

    //Play Screen Assets
    public Drawable playScreenPauseButtonDrawable;

    //Board Assets
    public ArrayMap<String, Texture> tileTextures;
    public ArrayMap<String, Texture> powerTextures;

    //HUD Assets
    public LabelStyle hudLabelStyle;

    public PoofAssetManager(){
        manager = new AssetManager();

        //Todo: load fonts using asset manager as well like in hud
        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, ".TTF", new FreetypeFontLoader(resolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        labelFontLoader = new FreeTypeFontLoaderParameter();
        labelFontLoader.fontFileName = "fonts/rock_solid.TTF";
        labelFontLoader.fontParameters.minFilter = TextureFilter.Linear;
        labelFontLoader.fontParameters.magFilter = TextureFilter.Linear;

        valueFontLoader = new FreeTypeFontLoaderParameter();
        valueFontLoader.fontFileName = "fonts/rock_solid.TTF";
        valueFontLoader.fontParameters.minFilter = TextureFilter.Linear;
        valueFontLoader.fontParameters.magFilter = TextureFilter.Linear;

//        Font Generators
        labelFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/rock_solid.TTF"));
        valueFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/rock_solid.TTF"));

        //font param common settings
        fontParam = new FreeTypeFontParameter();
        fontParam.minFilter = TextureFilter.Linear;
        fontParam.magFilter = TextureFilter.Linear;

        //generate assets
        loadCommonAssets();
        loadMainMenuAssets();
        loadHelpScreenAssets();
        loadOptionsAssets();
        loadPrivacyScreenAssets();
        loadLevelSelectAssets();
        loadGameOverAssets();
        loadPauseScreenAssets();
        loadPlayScreenAssets();
        loadBoardAssets();
        loadHUDAssets();
    }

    public boolean isLoaded(){
        if(manager.update()){
            getAssets();
            return true;
        }
        return false;
    }

    private void getAssets(){
        getCommonAssets();
        //getMainMenuAssets();
        getHelpScreenAssets();
        getOptionsAssets();
//        getPrivacyScreenAssets();
//        getLevelSelectAssets();
//        getGameOverAssets();
//        getPauseScreenAssets();
        getPlayScreenAssets();
        getBoardAssets();
        getHUDAssets();
    }

    private void loadFontGenerators(){
        manager.load("fonts/rock_solid.TTF",FreeTypeFontGenerator.class);
    }

    private void getFontGenerators(){
        labelFontGenerator = manager.get("fonts/rock_solid.TTF",FreeTypeFontGenerator.class);
        valueFontGenerator = manager.get("fonts/rock_solid.TTF",FreeTypeFontGenerator.class);
    }

    private void loadCommonAssets(){
        manager.load("common/bg.png", Texture.class);
    }

    private void getCommonAssets(){
        commonBG = manager.get("common/bg.png", Texture.class);
    }

    private void loadMainMenuAssets(){
        fontParam.color = Color.DARK_GRAY;

        //Title Style
        fontParam.size = (int)(100 * Poof.V_WIDTH/GameData.baseWidth);
        mainMenuTitleStyle = new LabelStyle();
        mainMenuTitleStyle.font = labelFontGenerator.generateFont(fontParam);

        //Button Style
        fontParam.size = (int)(50 * Poof.V_WIDTH/GameData.baseWidth);
        mainMenuButtonStyle = new TextButtonStyle();
        mainMenuButtonStyle.font = labelFontGenerator.generateFont(fontParam);
    }

    private void loadHelpScreenAssets(){
        fontParam.color = Color.DARK_GRAY;
        fontParam.size = (int)(30 * Poof.V_WIDTH/GameData.baseWidth);
        helpTextFont = valueFontGenerator.generateFont(fontParam);

        //load animation assets
        manager.load("manual/animation/slide_anim.txt", TextureAtlas.class);
    }

    private void getHelpScreenAssets(){
        TextureAtlas helpAtlas = new TextureAtlas("manual/animation/slide_anim.txt");
        helpAnim = new Animation<TextureRegion>(0.033f, helpAtlas.findRegions("slide"), Animation.PlayMode.LOOP);
    }

    private void loadOptionsAssets(){
        fontParam.color = Color.DARK_GRAY;

        //Title Style
        fontParam.size = (int)(80 * Poof.V_WIDTH/GameData.baseWidth);
        optionsTitleStyle = new LabelStyle();
        optionsTitleStyle.font = labelFontGenerator.generateFont(fontParam);

        //Button Style
        fontParam.size = (int)(50 * Poof.V_WIDTH/GameData.baseWidth);
        optionsButtonStyle = new TextButtonStyle();
        optionsButtonStyle.font = Poof.labelFontGenerator.generateFont(fontParam);

        //load slider assets
        manager.load("slider/musicKnob.png", Texture.class);
        manager.load("slider/musicBG.png", Texture.class);
        manager.load("slider/soundKnob.png", Texture.class);
        manager.load("slider/soundBG.png", Texture.class);
    }

    private void getOptionsAssets(){
        //Slider Styles
        optionsMusicSliderStyle = new SliderStyle();
        optionsMusicSliderStyle.knob = new TextureRegionDrawable(new TextureRegion(manager.get("slider/musicKnob.png",Texture.class)));
        optionsMusicSliderStyle.background = new TextureRegionDrawable(new TextureRegion(manager.get("slider/musicBG.png",Texture.class)));

        optionsSoundSliderStyle = new SliderStyle();
        optionsSoundSliderStyle.knob = new TextureRegionDrawable(new TextureRegion(manager.get("slider/soundKnob.png",Texture.class)));
        optionsSoundSliderStyle.background = new TextureRegionDrawable(new TextureRegion(manager.get("slider/soundBG.png",Texture.class)));
    }

    private void loadPrivacyScreenAssets(){
        fontParam.color = Color.DARK_GRAY;

        //Title Style
        fontParam.size = (int)(70 * Poof.V_WIDTH/GameData.baseWidth);
        privacyTitleStyle = new LabelStyle();
        privacyTitleStyle.font = labelFontGenerator.generateFont(fontParam);

        //Button Style
        fontParam.size = (int)(50 * Poof.V_WIDTH/GameData.baseWidth);
        privacyButtonStyle = new TextButtonStyle();
        privacyButtonStyle.font = Poof.labelFontGenerator.generateFont(fontParam);

        //Button Style
        fontParam.color = new Color(132f/255f,178f/255f,133f/255f,1);
        fontParam.size = (int)(30 * Poof.V_WIDTH/GameData.baseWidth);
        privacyPolicyButtonStyle = new TextButtonStyle();
        privacyPolicyButtonStyle.font = Poof.labelFontGenerator.generateFont(fontParam);
    }

    private void loadLevelSelectAssets(){
        fontParam.color = Color.DARK_GRAY;

        //Title Style
        fontParam.size = (int)(65 * Poof.V_WIDTH/GameData.baseWidth);
        levelSelectTitleStyle = new LabelStyle();
        levelSelectTitleStyle.font = labelFontGenerator.generateFont(fontParam);

        //Button Style
        fontParam.size = (int)(50 * Poof.V_WIDTH/GameData.baseWidth);
        levelSelectButtonStyle = new TextButtonStyle();
        levelSelectButtonStyle.font = labelFontGenerator.generateFont(fontParam);

        fontParam.color = new Color(Color.LIGHT_GRAY);
        levelSelectGreyedButtonStyle = new TextButtonStyle();
        levelSelectGreyedButtonStyle.font = labelFontGenerator.generateFont(fontParam);
    }

    private void loadGameOverAssets(){
        fontParam.color = Color.DARK_GRAY;

        //Label Font
        fontParam.size = (int)(30 * Poof.V_WIDTH/GameData.baseWidth);
        gameOverLabelStyle = new LabelStyle();
        gameOverLabelStyle.font = Poof.valueFontGenerator.generateFont(fontParam);

        //Button Font
        fontParam.size = (int)(50 * Poof.V_WIDTH/GameData.baseWidth);
        gameOverButtonStyle = new TextButtonStyle();
        gameOverButtonStyle.font = Poof.valueFontGenerator.generateFont(fontParam);
    }

    private void loadPauseScreenAssets(){
        fontParam.color = Color.DARK_GRAY;

        //Title Style
        fontParam.size = (int)(80 * Poof.V_WIDTH/GameData.baseWidth);
        pauseTitleStyle = new LabelStyle();
        pauseTitleStyle.font = labelFontGenerator.generateFont(fontParam);

        //Button Style
        fontParam.size = (int)(50 * Poof.V_WIDTH/GameData.baseWidth);
        pauseButtonStyle = new TextButtonStyle();
        pauseButtonStyle.font = labelFontGenerator.generateFont(fontParam);
    }

    private void loadPlayScreenAssets(){
        manager.load("buttons/pause.png",Texture.class);
    }

    private void getPlayScreenAssets(){
        Texture pauseButtonTex = manager.get("buttons/pause.png",Texture.class);
        pauseButtonTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        playScreenPauseButtonDrawable = new TextureRegionDrawable(new TextureRegion(pauseButtonTex));
    }

    private void loadBoardAssets(){
        //Tile Texturess
        for(int i=0;i<GameData.allTileColors.size;i++){
            String color = GameData.allTileColors.get(i);
            manager.load("tiles/tile_" + color + ".png",Texture.class);
            manager.load("tiles/tile_" + color + "_touched.png",Texture.class);
        }

        //Power Textures
        for (String power:TilePower.powerProbs.keys()){
            if(power != null)
            manager.load("tiles/tile_" + power + ".png",Texture.class);
        }
    }

    private void getBoardAssets(){
        tileTextures = new ArrayMap<String, Texture>();
        for(int i=0;i<GameData.allTileColors.size;i++){
            String color = GameData.allTileColors.get(i);
            tileTextures.put(color+"_tile", manager.get("tiles/tile_" + color + ".png",Texture.class));
            tileTextures.put(color+"_tile_touched", manager.get("tiles/tile_" + color + "_touched.png",Texture.class));
        }

        for (String key:tileTextures.keys()){
            tileTextures.get(key).setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }

        //Power Textures
        powerTextures = new ArrayMap<String, Texture>();
        for (String power:TilePower.powerProbs.keys()){
            if(power != null)
                powerTextures.put(power, manager.get("tiles/tile_" + power + ".png",Texture.class));
        }
        for (String key:powerTextures.keys()){
            powerTextures.get(key).setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }
    }

    private void loadHUDAssets(){
        valueFontLoader.fontParameters.color = Color.DARK_GRAY;
        valueFontLoader.fontParameters.size = (int)(20 * Poof.V_WIDTH/GameData.baseWidth);
        manager.load("hudFont.ttf", BitmapFont.class, valueFontLoader);
    }

    private void getHUDAssets(){
        hudLabelStyle = new LabelStyle();
        hudLabelStyle.font = manager.get("hudFont.ttf",BitmapFont.class);
    }

    public void dispose(){
        System.out.println("Assets disposed");
        manager.dispose();
    }
}
