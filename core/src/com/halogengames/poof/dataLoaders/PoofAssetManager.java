package com.halogengames.poof.dataLoaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ArrayMap;
import com.halogengames.poof.Poof;

import java.util.ArrayList;


/**
 * Created by Rohit on 03-10-2017.
 *
 * All styles and their assets are loaded here
 */

public class PoofAssetManager {
    public AssetManager manager;
    FreeTypeFontLoaderParameter labelFontLoader;
    FreeTypeFontLoaderParameter valueFontLoader;

    //Skin
    public Skin shopSkin;
    public Skin UISkin;
    private Color fontColor = Color.DARK_GRAY;

    //Font Generators
    public FreeTypeFontGenerator labelFontGenerator;
    public FreeTypeFontGenerator valueFontGenerator;
    public FreeTypeFontParameter fontParam;

    //Common Assets
    public ArrayList<Texture> bgShapes;
    private ArrayList<String> bgShapesList;
    public Texture gradientBg;

    public float buttWidth = Poof.V_WIDTH*3/8;
    public float buttHeight = Poof.V_HEIGHT/15;
    public float dialogButtHeight = Poof.V_HEIGHT/12;
    public float buttPadding = buttHeight/4;

    private int cornerRad = 15 + 5;

    //Main Menu Assets
    public Texture mainMenuTitleTex;
    public Drawable mainMenuPlayButtonDrawable;
    public Drawable mainMenuHelpButtonDrawable;
    public Drawable mainMenuFameButtonDrawable;
    public Drawable mainMenuShopButtonDrawable;
    public Drawable mainMenuOptionsButtonDrawable;

    //help Screen Assets
    public BitmapFont helpTextFont;
    public Animation<TextureRegion> helpAnim;

    //Game Mode Select Title Screen
    public Texture modeSelectTitleTex;

    //Options Screen Assets
    public Texture optionsTitleTex;
    public SliderStyle optionsMusicSliderStyle;
    public SliderStyle optionsSoundSliderStyle;

    //Privacy Screen Assets
    public LabelStyle privacyTitleStyle;
    public TextButtonStyle privacyButtonStyle;
    public TextButtonStyle privacyPolicyButtonStyle;

    //Game Over Screen Assets
    public LabelStyle gameOverLabelStyle;
    public TextButtonStyle gameOverButtonStyle;

    //Pause Screen Assets
    public Texture pauseTitleTex;

    //Play Screen Assets
    public Drawable playScreenPauseButtonDrawable;
    public ArrayMap<String,Animation<TextureRegion>> spriteAnims;

    //Board Assets
    public ArrayMap<String, Texture> tileTextures;
    public ArrayMap<String, Texture> powerTextures;

    //HUD Assets
    public LabelStyle hudLabelStyle;

    public PoofAssetManager(){
        manager = new AssetManager();

        UISkin = new Skin();

        spriteAnims = new ArrayMap<String, Animation<TextureRegion>>();

        //Todo: load fonts using asset manager as well like in hud
        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, ".TTF", new FreetypeFontLoader(resolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        labelFontLoader = new FreeTypeFontLoaderParameter();
        labelFontLoader.fontFileName = "fonts/arialRoundedMTBold.ttf";
        labelFontLoader.fontParameters.minFilter = TextureFilter.Linear;
        labelFontLoader.fontParameters.magFilter = TextureFilter.Linear;

        valueFontLoader = new FreeTypeFontLoaderParameter();
        valueFontLoader.fontFileName = "fonts/arialRoundedMTBold.ttf";
        valueFontLoader.fontParameters.minFilter = TextureFilter.Linear;
        valueFontLoader.fontParameters.magFilter = TextureFilter.Linear;

//        Font Generators
        labelFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/arialRoundedMTBold.ttf"));
        valueFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/arialRoundedMTBold.ttf"));

        //font param common settings
        fontParam = new FreeTypeFontParameter();
        fontParam.minFilter = TextureFilter.Linear;
        fontParam.magFilter = TextureFilter.Linear;

        //generate assets
        loadCommonAssets();
        loadMainMenuAssets();
        loadShopAssets();
        loadHelpScreenAssets();
        loadOptionsAssets();
        loadPrivacyScreenAssets();
        loadModeSelectAssets();
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
        getMainMenuAssets();
        getShopAssets();
        getHelpScreenAssets();
        getOptionsAssets();
//        getPrivacyScreenAssets();
        getModeSelectAssets();
//        getGameOverAssets();
        getPauseScreenAssets();
        getPlayScreenAssets();
        getBoardAssets();
        getHUDAssets();
    }

    private void loadCommonAssets(){
        bgShapes = new ArrayList<Texture>();
        bgShapesList = new ArrayList<String>();

        bgShapesList.add("circle");
        bgShapesList.add("square");
        bgShapesList.add("triangle");
        bgShapesList.add("star");

        for(int i=0;i<bgShapesList.size();i++){
            manager.load("common/bg_"+bgShapesList.get(i)+".png", Texture.class);
        }

        manager.load("common/blue_bg.png", Texture.class);

        //UI Skin Assets

        manager.load("common/blank_np_bg.png", Texture.class);
        manager.load("buttons/yesButt.png", Texture.class);
        manager.load("buttons/noButt.png", Texture.class);
        manager.load("buttons/okButt.png", Texture.class);
        manager.load("buttons/backButt.png", Texture.class);
    }

    private void getCommonAssets(){
        for(int i=0;i<bgShapesList.size();i++){
            bgShapes.add(manager.get("common/bg_"+bgShapesList.get(i)+".png", Texture.class));
            bgShapes.get(i).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        gradientBg = manager.get("common/blue_bg.png", Texture.class);

        //Set UI Skin
        fontParam.color = Color.BLACK;
        fontParam.size = 20;

        Texture tex = manager.get("common/blank_np_bg.png", Texture.class);
        tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Drawable npDrawable = new NinePatchDrawable(new NinePatch(tex,cornerRad,cornerRad,cornerRad,cornerRad));
        UISkin.add("blankNinePatchBG", npDrawable);

        ImageButton.ImageButtonStyle yesButt = new ImageButton.ImageButtonStyle();
        tex = manager.get("buttons/yesButt.png", Texture.class);
        tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        yesButt.up = new TextureRegionDrawable(new TextureRegion(tex));
        UISkin.add("yesButton", yesButt);

        ImageButton.ImageButtonStyle noButt = new ImageButton.ImageButtonStyle();
        tex = manager.get("buttons/noButt.png", Texture.class);
        tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        noButt.up = new TextureRegionDrawable(new TextureRegion(tex));
        UISkin.add("noButton", noButt);

        ImageButton.ImageButtonStyle okButt = new ImageButton.ImageButtonStyle();
        tex = manager.get("buttons/okButt.png", Texture.class);
        tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        okButt.up = new TextureRegionDrawable(new TextureRegion(tex));
        UISkin.add("okButton", okButt);

        ImageButton.ImageButtonStyle backButt = new ImageButton.ImageButtonStyle();
        tex = manager.get("buttons/backButt.png", Texture.class);
        tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        backButt.up = new TextureRegionDrawable(new TextureRegion(tex));
        UISkin.add("backButton", backButt);
    }

    private void loadMainMenuAssets(){
        manager.load("titles/poofTitle.png", Texture.class);
        manager.load("buttons/play.png", Texture.class);
        manager.load("buttons/fame.png", Texture.class);
        manager.load("buttons/options.png", Texture.class);
        manager.load("buttons/help.png", Texture.class);
        manager.load("buttons/shop.png", Texture.class);
    }

    private void getMainMenuAssets(){
        mainMenuTitleTex = manager.get("titles/poofTitle.png", Texture.class);
        mainMenuTitleTex.setFilter(TextureFilter.Linear,TextureFilter.Linear);

        Texture tex = manager.get("buttons/play.png", Texture.class);
        tex.setFilter(TextureFilter.Linear,TextureFilter.Linear);
        mainMenuPlayButtonDrawable = new TextureRegionDrawable(new TextureRegion(tex));

        tex = manager.get("buttons/help.png", Texture.class);
        tex.setFilter(TextureFilter.Linear,TextureFilter.Linear);
        mainMenuHelpButtonDrawable = new TextureRegionDrawable(new TextureRegion(tex));

        tex = manager.get("buttons/fame.png", Texture.class);
        tex.setFilter(TextureFilter.Linear,TextureFilter.Linear);
        mainMenuFameButtonDrawable = new TextureRegionDrawable(new TextureRegion(tex));

        tex = manager.get("buttons/options.png", Texture.class);
        tex.setFilter(TextureFilter.Linear,TextureFilter.Linear);
        mainMenuOptionsButtonDrawable = new TextureRegionDrawable(new TextureRegion(tex));

        tex = manager.get("buttons/shop.png", Texture.class);
        tex.setFilter(TextureFilter.Linear,TextureFilter.Linear);
        mainMenuShopButtonDrawable = new TextureRegionDrawable(new TextureRegion(tex));
    }

    private void loadShopAssets(){
        manager.load("shop/shopTitle.png", Texture.class);

        manager.load("shop/upgradeBG.png", Texture.class);

        manager.load("shop/upgrade_icon.png", Texture.class);

        manager.load("shop/sliderBG.png", Texture.class);
        manager.load("shop/sliderBef.png", Texture.class);

        manager.load("shop/coin.png", Texture.class);

        manager.load("shop/buyButtBG.png", Texture.class);
    }

    private void getShopAssets(){
        shopSkin = new Skin();

        //set shop title
        Texture shopTitleTex = manager.get("shop/shopTitle.png", Texture.class);
        shopTitleTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        shopSkin.add("shopTitleTex", shopTitleTex);

        //set bg
        NinePatch bg = new NinePatch(manager.get("shop/upgradeBG.png", Texture.class),40,40,40,40);
        shopSkin.add("upgradeBG",bg);

        //set progress bar style
        SliderStyle upgradeSliderStyle = new SliderStyle();
        upgradeSliderStyle.background = new NinePatchDrawable(new NinePatch(manager.get("shop/sliderBG.png", Texture.class),9,9,9,9));
        upgradeSliderStyle.knobBefore = new NinePatchDrawable(new NinePatch(manager.get("shop/sliderBef.png", Texture.class),4,4,4,4));
        shopSkin.add("upgradeSliderStyle", upgradeSliderStyle);

        //setting font
        fontParam.color = Color.BLACK;
        fontParam.size = 20;

        //set buy button style
        ImageTextButton.ImageTextButtonStyle buyButtStyle = new ImageTextButton.ImageTextButtonStyle();
        buyButtStyle.up = new NinePatchDrawable(new NinePatch(manager.get("shop/buyButtBG.png", Texture.class),11,11,11,11));
        buyButtStyle.imageUp = new TextureRegionDrawable(new TextureRegion(manager.get("shop/coin.png", Texture.class)));
        buyButtStyle.font = valueFontGenerator.generateFont(fontParam);
        shopSkin.add("purchaseButtonStyle",buyButtStyle);

        //set description style
        Label.LabelStyle labSt = new Label.LabelStyle();
        labSt.font = valueFontGenerator.generateFont(fontParam);
        shopSkin.add("descriptionStyle",labSt);

        //set bomb icon
        Texture bombUpgradeIcon = manager.get("shop/upgrade_icon.png", Texture.class);
        shopSkin.add("bombUpgradeIcon",bombUpgradeIcon);
    }

    private void loadHelpScreenAssets(){
        fontParam.color = fontColor;
        fontParam.size = (int)(30 * Poof.V_WIDTH/GameData.baseWidth);
        helpTextFont = valueFontGenerator.generateFont(fontParam);

        //load animation assets
        manager.load("manual/animation/slide_anim.txt", TextureAtlas.class);
    }

    private void getHelpScreenAssets(){
        TextureAtlas helpAtlas = manager.get("manual/animation/slide_anim.txt", TextureAtlas.class);
        helpAnim = new Animation<TextureRegion>(0.033f, helpAtlas.findRegions("Slide"), Animation.PlayMode.LOOP);
    }

    private void loadOptionsAssets(){
        manager.load("titles/optionsTitle.png", Texture.class);

        //load slider assets
        manager.load("options/musicKnob.png", Texture.class);
        manager.load("options/musicBG.png", Texture.class);
        manager.load("options/soundKnob.png", Texture.class);
        manager.load("options/soundBG.png", Texture.class);

        manager.load("buttons/privacyButt.png", Texture.class);
    }

    private void getOptionsAssets(){
        optionsTitleTex = manager.get("titles/optionsTitle.png", Texture.class);
        optionsTitleTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        //Slider Styles
        optionsMusicSliderStyle = new SliderStyle();
        Texture tex = manager.get("options/musicKnob.png",Texture.class);
        tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        optionsMusicSliderStyle.knob = new TextureRegionDrawable(new TextureRegion(tex));

        tex = manager.get("options/musicBG.png",Texture.class);
        tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        optionsMusicSliderStyle.background = new TextureRegionDrawable(new TextureRegion(tex));

        optionsSoundSliderStyle = new SliderStyle();
        tex = manager.get("options/soundKnob.png",Texture.class);
        tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        optionsSoundSliderStyle.knob = new TextureRegionDrawable(new TextureRegion(tex));
        tex = manager.get("options/soundBG.png",Texture.class);
        tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        optionsSoundSliderStyle.background = new TextureRegionDrawable(new TextureRegion(tex));

        ImageButton.ImageButtonStyle privacyButt = new ImageButton.ImageButtonStyle();
        privacyButt.up = new TextureRegionDrawable(new TextureRegion(manager.get("buttons/privacyButt.png", Texture.class)));
        UISkin.add("privacyButton", privacyButt);
    }

    private void loadPrivacyScreenAssets(){
        fontParam.color = fontColor;

        //Title Style
        fontParam.size = (int)(70 * Poof.V_WIDTH/GameData.baseWidth);
        privacyTitleStyle = new LabelStyle();
        privacyTitleStyle.font = labelFontGenerator.generateFont(fontParam);

        //Button Style
        fontParam.size = (int)(50 * Poof.V_WIDTH/GameData.baseWidth);
        privacyButtonStyle = new TextButtonStyle();
        privacyButtonStyle.font = labelFontGenerator.generateFont(fontParam);

        //Button Style
        fontParam.color = new Color(132f/255f,178f/255f,133f/255f,1);
        fontParam.size = (int)(30 * Poof.V_WIDTH/GameData.baseWidth);
        privacyPolicyButtonStyle = new TextButtonStyle();
        privacyPolicyButtonStyle.font = labelFontGenerator.generateFont(fontParam);
    }

    private void loadModeSelectAssets(){
        manager.load("titles/gameModeTitle.png", Texture.class);

        manager.load("buttons/timedButt.png", Texture.class);
        manager.load("buttons/relaxedButt.png", Texture.class);
    }

    private void getModeSelectAssets(){
        modeSelectTitleTex = manager.get("titles/gameModeTitle.png", Texture.class);
        modeSelectTitleTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        ImageButton.ImageButtonStyle timedButt = new ImageButton.ImageButtonStyle();
        Texture tex = manager.get("buttons/timedButt.png", Texture.class);
        tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        timedButt.up = new TextureRegionDrawable(new TextureRegion(tex));
        UISkin.add("timedButton", timedButt);

        ImageButton.ImageButtonStyle relaxedButt = new ImageButton.ImageButtonStyle();
        tex = manager.get("buttons/relaxedButt.png", Texture.class);
        tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        relaxedButt.up = new TextureRegionDrawable(new TextureRegion(tex));
        UISkin.add("relaxedButton", relaxedButt);
    }

    private void loadGameOverAssets(){
        fontParam.color = fontColor;

        //Label Font
        fontParam.size = (int)(60 * Poof.V_WIDTH/GameData.baseWidth);
        gameOverLabelStyle = new LabelStyle();
        gameOverLabelStyle.font = valueFontGenerator.generateFont(fontParam);

        //Button Font
        fontParam.size = (int)(50 * Poof.V_WIDTH/GameData.baseWidth);
        gameOverButtonStyle = new TextButtonStyle();
        gameOverButtonStyle.font = valueFontGenerator.generateFont(fontParam);
    }

    private void loadPauseScreenAssets(){
        manager.load("titles/pauseTitle.png", Texture.class);

        manager.load("buttons/resumeButt.png", Texture.class);
        manager.load("buttons/restartButt.png", Texture.class);
        manager.load("buttons/optionsButt.png", Texture.class);
        manager.load("buttons/mainMenuButt.png", Texture.class);
    }

    private void getPauseScreenAssets(){
        pauseTitleTex = manager.get("titles/pauseTitle.png", Texture.class);
        pauseTitleTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        ImageButton.ImageButtonStyle resumeButt = new ImageButton.ImageButtonStyle();
        Texture tex = manager.get("buttons/resumeButt.png", Texture.class);
        tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        resumeButt.up = new TextureRegionDrawable(new TextureRegion(tex));
        UISkin.add("resumeButton", resumeButt);

        ImageButton.ImageButtonStyle restartButt = new ImageButton.ImageButtonStyle();
        tex = manager.get("buttons/restartButt.png", Texture.class);
        tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        restartButt.up = new TextureRegionDrawable(new TextureRegion(tex));
        UISkin.add("restartButton", restartButt);

        ImageButton.ImageButtonStyle optionsButt = new ImageButton.ImageButtonStyle();
        tex = manager.get("buttons/optionsButt.png", Texture.class);
        tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        optionsButt.up = new TextureRegionDrawable(new TextureRegion(tex));
        UISkin.add("optionsButton", optionsButt);

        ImageButton.ImageButtonStyle mainMenuButt = new ImageButton.ImageButtonStyle();
        tex = manager.get("buttons/mainMenuButt.png", Texture.class);
        tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        mainMenuButt.up = new TextureRegionDrawable(new TextureRegion(tex));
        UISkin.add("mainMenuButton", mainMenuButt);
    }

    private void loadPlayScreenAssets(){
        manager.load("buttons/pause.png",Texture.class);

        //load animation assets
        manager.load("animation/sprites/explosion.txt", TextureAtlas.class);
    }

    private void getPlayScreenAssets(){
        Texture pauseButtonTex = manager.get("buttons/pause.png",Texture.class);
        pauseButtonTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        playScreenPauseButtonDrawable = new TextureRegionDrawable(new TextureRegion(pauseButtonTex));

        //load animations
        TextureAtlas tempAtlas = manager.get("animation/sprites/explosion.txt", TextureAtlas.class);
        Animation<TextureRegion> explodeAnim = new Animation<TextureRegion>(0.3f/15.0f, tempAtlas.findRegions("explosion"), Animation.PlayMode.LOOP);
        spriteAnims.put("explosion", explodeAnim);
    }

    private void loadBoardAssets(){
        //Tile Textures
        for(int i = 0; i<GameData.tileColors.size; i++){
            String color = GameData.tileColors.get(i);
            manager.load("tiles/tile_" + color + ".png",Texture.class);
            manager.load("tiles/tile_" + color + "_touched.png",Texture.class);
        }

        //Power Textures
        for (String power:TilePower.getPossiblePowersList()){
            if(power != null)
            manager.load("tiles/tile_" + power + ".png",Texture.class);
        }
    }

    private void getBoardAssets(){
        tileTextures = new ArrayMap<String, Texture>();
        for(int i = 0; i<GameData.tileColors.size; i++){
            String color = GameData.tileColors.get(i);
            Texture tex = manager.get("tiles/tile_" + color + ".png",Texture.class);
            tex.setFilter(TextureFilter.Linear,TextureFilter.Linear);
            tileTextures.put(color+"_tile", tex);

            tex = manager.get("tiles/tile_" + color + "_touched.png",Texture.class);
            tex.setFilter(TextureFilter.Linear,TextureFilter.Linear);
            tileTextures.put(color+"_tile_touched", tex);
        }

        for (String key:tileTextures.keys()){
            tileTextures.get(key).setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }

        //Power Textures
        powerTextures = new ArrayMap<String, Texture>();
        for (String power:TilePower.getPossiblePowersList()){
            if(power != null) {
                Texture tex = manager.get("tiles/tile_" + power + ".png", Texture.class);
                tex.setFilter(TextureFilter.Linear,TextureFilter.Linear);
                powerTextures.put(power, tex);
            }
        }
        for (String key:powerTextures.keys()){
            powerTextures.get(key).setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }
    }

    private void loadHUDAssets(){
        valueFontLoader.fontParameters.color = fontColor;
        valueFontLoader.fontParameters.size = (int)(20 * Poof.V_WIDTH/GameData.baseWidth);
        manager.load("hudFont.ttf", BitmapFont.class, valueFontLoader);
    }

    private void getHUDAssets(){
        hudLabelStyle = new LabelStyle();
        hudLabelStyle.font = manager.get("hudFont.ttf",BitmapFont.class);
    }

    //Fixme: Memory leak
    public void dispose(){
        System.out.println("Assets disposed");
        labelFontGenerator.dispose();
        valueFontGenerator.dispose();
        manager.dispose();
    }
}
