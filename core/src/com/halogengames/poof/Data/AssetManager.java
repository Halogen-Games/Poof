package com.halogengames.poof.Data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
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

public class AssetManager {
    //Font Generators
    private static FreeTypeFontGenerator labelFontGenerator;
    private static FreeTypeFontGenerator valueFontGenerator;
    private static FreeTypeFontParameter fontParam;

    //Common Assets
    public static Texture commonBG;

    //Main Menu Assets
    public static LabelStyle mainMenuTitleStyle;
    public static TextButtonStyle mainMenuButtonStyle;
    public static Texture mainMenuButtonDockTex;
    public static Drawable mainMenuPlayButtonDrawable;
    public static Drawable mainMenuOptionsButtonDrawable;
    public static Drawable mainMenuUpgradeButtonDrawable;
    public static Drawable mainMenuShopButtonDrawable;
    public static Drawable mainMenuExitButtonDrawable;

    //Options Screen Assets
    public static LabelStyle optionsTitleStyle;
    public static TextButtonStyle optionsButtonStyle;
    public static SliderStyle optionsMusicSliderStyle;
    public static SliderStyle optionsSoundSliderStyle;

    //Game Over Screen Assets
    public static LabelStyle gameOverLabelStyle;
    public static TextButtonStyle gameOverButtonStyle;

    //Pause Screen Assets
    public static LabelStyle pauseTitleStyle;
    public static TextButtonStyle pauseButtonStyle;

    //Play Screen Assets
    public static Drawable playScreenPauseButtonDrawable;

    //Board Assets
    public static Texture boardBG;
    public static ArrayMap<String, Texture> tileTextures;
    public static ArrayMap<String, Texture> powerTextures;

    //HUD Assets
    public static LabelStyle hudLabelStyle;

    //Screen Handles

    public static void init(){
        //Font Generators
        labelFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/rock_solid.TTF"));
        valueFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/rock_solid.TTF"));

        //font param common settings
        fontParam = new FreeTypeFontParameter();
        fontParam.minFilter = TextureFilter.Linear;
        fontParam.magFilter = TextureFilter.Linear;

        //generate assets
        generateCommonAssets();
        generateMainMenuAssets();
        generateOptionsAssets();
        generateGameOverAssets();
        generatePauseScreenAssets();
        generatePlayScreenAssets();
        generateBoardAssets();
        generateHUDAssets();
    }

    private static void generateCommonAssets(){
        commonBG = new Texture("common/bg.png");
    }

    private static void generateMainMenuAssets(){
        fontParam.color = Color.DARK_GRAY;

        //Title Style
        fontParam.size = (int)(100 * Poof.V_WIDTH/GameData.baseWidth);
        mainMenuTitleStyle = new LabelStyle();
        mainMenuTitleStyle.font = labelFontGenerator.generateFont(fontParam);

        //Button Style
        fontParam.size = (int)(50 * Poof.V_WIDTH/GameData.baseWidth);
        mainMenuButtonStyle = new TextButtonStyle();
        mainMenuButtonStyle.font = labelFontGenerator.generateFont(fontParam);

        //textures
        mainMenuButtonDockTex = new Texture("main_menu/button_dock.png");

        //Drawables
        Texture playTex = new Texture("main_menu/play_button.png");
        playTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        mainMenuPlayButtonDrawable = new TextureRegionDrawable(new TextureRegion(playTex));

        Texture optionsTex = new Texture("main_menu/options_button.png");
        optionsTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        mainMenuOptionsButtonDrawable = new TextureRegionDrawable(new TextureRegion(optionsTex));

        Texture exitTex = new Texture("main_menu/exit_button.png");
        exitTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        mainMenuExitButtonDrawable = new TextureRegionDrawable(new TextureRegion(exitTex));

        Texture shopTex = new Texture("main_menu/shop_button.png");
        shopTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        mainMenuShopButtonDrawable = new TextureRegionDrawable(new TextureRegion(shopTex));

        Texture upgradeTex = new Texture("main_menu/upgrade_button.png");
        upgradeTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        mainMenuUpgradeButtonDrawable = new TextureRegionDrawable(new TextureRegion(upgradeTex));
    }

    private static void generateOptionsAssets(){
        fontParam.color = Color.DARK_GRAY;

        //Title Style
        fontParam.size = (int)(80 * Poof.V_WIDTH/GameData.baseWidth);
        optionsTitleStyle = new LabelStyle();
        optionsTitleStyle.font = labelFontGenerator.generateFont(fontParam);

        //Button Style
        fontParam.size = (int)(50 * Poof.V_WIDTH/GameData.baseWidth);
        optionsButtonStyle = new TextButtonStyle();
        optionsButtonStyle.font = Poof.labelFontGenerator.generateFont(fontParam);

        //Slider Styles
        optionsMusicSliderStyle = new SliderStyle();
        optionsMusicSliderStyle.knob = new TextureRegionDrawable(new TextureRegion(new Texture("slider/musicKnob.png")));
        optionsMusicSliderStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture("slider/musicBG.png")));

        optionsSoundSliderStyle = new SliderStyle();
        optionsSoundSliderStyle.knob = new TextureRegionDrawable(new TextureRegion(new Texture("slider/soundKnob.png")));
        optionsSoundSliderStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture("slider/soundBG.png")));
    }

    private static void generateGameOverAssets(){
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

    private static void generatePauseScreenAssets(){
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

    private static void generatePlayScreenAssets(){
        Texture pauseButtonTex = new Texture("buttons/pause.png");
        pauseButtonTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        playScreenPauseButtonDrawable = new TextureRegionDrawable(new TextureRegion(pauseButtonTex));
    }

    private static void generateBoardAssets(){
        boardBG = new Texture("common/board_bg.png");

        boardBG.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        //Tile Texs
        tileTextures = new ArrayMap<String, Texture>();
        for(int i=0;i<GameData.allTileColors.size;i++){
            String color = GameData.allTileColors.get(i);

            tileTextures.put(color+"_tile", new Texture("tiles/tile_" + color + ".png"));
            tileTextures.put(color+"_tile_touched", new Texture("tiles/tile_" + color + "_touched.png"));
        }

        for (String key:tileTextures.keys()){
            tileTextures.get(key).setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }

        //Power Texs
        powerTextures = new ArrayMap<String, Texture>();
        for (String power:TilePower.powerProbs.keys()){
            if(power != null)
            powerTextures.put(power, new Texture("tiles/tile_" + power + ".png"));
        }
        for (String key:powerTextures.keys()){
            powerTextures.get(key).setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }
    }

    private static void generateHUDAssets(){
        fontParam.color = Color.DARK_GRAY;

        //Label Font
        fontParam.size = (int)(20 * Poof.V_WIDTH/GameData.baseWidth);
        hudLabelStyle = new LabelStyle();
        hudLabelStyle.font = Poof.valueFontGenerator.generateFont(fontParam);
    }

    public static void dispose(){
        System.out.println("Assets disposed");
        for(String col:tileTextures.keys()){
            tileTextures.get(col).dispose();
        }

        mainMenuButtonDockTex.dispose();
    }
}
