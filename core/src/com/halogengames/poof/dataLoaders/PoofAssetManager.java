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
    private Color fontColor = Color.BLACK;

    //Font Generators
    public FreeTypeFontGenerator labelFontGenerator;
    public FreeTypeFontGenerator valueFontGenerator;
    public FreeTypeFontParameter fontParam;

    //Common Assets
    public ArrayList<Texture> bgShapes;
    private ArrayList<String> bgShapesList;
    public Texture gradientBg;

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
    public ArrayMap<String,Animation<TextureRegion>> spriteAnims;

    //Board Assets
    public ArrayMap<String, Texture> tileTextures;
    public ArrayMap<String, Texture> powerTextures;

    //HUD Assets
    public LabelStyle hudLabelStyle;

    public PoofAssetManager(){
        manager = new AssetManager();

        spriteAnims = new ArrayMap<String, Animation<TextureRegion>>();

        //Todo: load fonts using asset manager as well like in hud
        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, ".TTF", new FreetypeFontLoader(resolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        labelFontLoader = new FreeTypeFontLoaderParameter();
        labelFontLoader.fontFileName = "fonts/comic_sans_ms.ttf";
        labelFontLoader.fontParameters.minFilter = TextureFilter.Linear;
        labelFontLoader.fontParameters.magFilter = TextureFilter.Linear;

        valueFontLoader = new FreeTypeFontLoaderParameter();
        valueFontLoader.fontFileName = "fonts/comic_sans_ms.ttf";
        valueFontLoader.fontParameters.minFilter = TextureFilter.Linear;
        valueFontLoader.fontParameters.magFilter = TextureFilter.Linear;

//        Font Generators
        labelFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/comic_sans_ms.ttf"));
        valueFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/comic_sans_ms.ttf"));

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
        getMainMenuAssets();
        getShopAssets();
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
    }

    private void getCommonAssets(){
        for(int i=0;i<bgShapesList.size();i++){
            bgShapes.add(manager.get("common/bg_"+bgShapesList.get(i)+".png", Texture.class));
            bgShapes.get(i).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        gradientBg = manager.get("common/blue_bg.png", Texture.class);
    }

    private void loadMainMenuAssets(){
        manager.load("main_menu/title.png", Texture.class);
        manager.load("main_menu/play.png", Texture.class);
        manager.load("main_menu/fame.png", Texture.class);
        manager.load("main_menu/options.png", Texture.class);
        manager.load("main_menu/help.png", Texture.class);
        manager.load("main_menu/shop.png", Texture.class);
    }

    private void getMainMenuAssets(){
        mainMenuTitleTex = manager.get("main_menu/title.png", Texture.class);
        mainMenuTitleTex.setFilter(TextureFilter.Linear,TextureFilter.Linear);

        Texture tex = manager.get("main_menu/play.png", Texture.class);
        tex.setFilter(TextureFilter.Linear,TextureFilter.Linear);
        mainMenuPlayButtonDrawable = new TextureRegionDrawable(new TextureRegion(tex));

        tex = manager.get("main_menu/help.png", Texture.class);
        tex.setFilter(TextureFilter.Linear,TextureFilter.Linear);
        mainMenuHelpButtonDrawable = new TextureRegionDrawable(new TextureRegion(tex));

        tex = manager.get("main_menu/fame.png", Texture.class);
        tex.setFilter(TextureFilter.Linear,TextureFilter.Linear);
        mainMenuFameButtonDrawable = new TextureRegionDrawable(new TextureRegion(tex));

        tex = manager.get("main_menu/options.png", Texture.class);
        tex.setFilter(TextureFilter.Linear,TextureFilter.Linear);
        mainMenuOptionsButtonDrawable = new TextureRegionDrawable(new TextureRegion(tex));

        tex = manager.get("main_menu/shop.png", Texture.class);
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
        helpAnim = new Animation<TextureRegion>(0.033f, helpAtlas.findRegions("slide"), Animation.PlayMode.LOOP);
    }

    private void loadOptionsAssets(){
        fontParam.color = fontColor;

        //Title Style
        fontParam.size = (int)(80 * Poof.V_WIDTH/GameData.baseWidth);
        optionsTitleStyle = new LabelStyle();
        optionsTitleStyle.font = labelFontGenerator.generateFont(fontParam);

        //Button Style
        fontParam.size = (int)(50 * Poof.V_WIDTH/GameData.baseWidth);
        optionsButtonStyle = new TextButtonStyle();
        optionsButtonStyle.font = labelFontGenerator.generateFont(fontParam);

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

    private void loadLevelSelectAssets(){
        fontParam.color = fontColor;

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
        fontParam.color = fontColor;

        //Label Font
        fontParam.size = (int)(30 * Poof.V_WIDTH/GameData.baseWidth);
        gameOverLabelStyle = new LabelStyle();
        gameOverLabelStyle.font = valueFontGenerator.generateFont(fontParam);

        //Button Font
        fontParam.size = (int)(50 * Poof.V_WIDTH/GameData.baseWidth);
        gameOverButtonStyle = new TextButtonStyle();
        gameOverButtonStyle.font = valueFontGenerator.generateFont(fontParam);
    }

    private void loadPauseScreenAssets(){
        fontParam.color = fontColor;

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
        //Tile Texturess
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
            tileTextures.put(color+"_tile", manager.get("tiles/tile_" + color + ".png",Texture.class));
            tileTextures.put(color+"_tile_touched", manager.get("tiles/tile_" + color + "_touched.png",Texture.class));
        }

        for (String key:tileTextures.keys()){
            tileTextures.get(key).setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }

        //Power Textures
        powerTextures = new ArrayMap<String, Texture>();
        for (String power:TilePower.getPossiblePowersList()){
            if(power != null)
                powerTextures.put(power, manager.get("tiles/tile_" + power + ".png",Texture.class));
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
