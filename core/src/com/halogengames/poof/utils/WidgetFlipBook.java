//package com.halogengames.poof.utils;
//
//import com.badlogic.gdx.graphics.g2d.Batch;
//import com.badlogic.gdx.scenes.scene2d.ui.Widget;
//
//import java.util.ArrayList;
//
//public class WidgetFlipBook extends Widget {
//    private ArrayList<WidgetPage> pages;
//    private int currentPage;
//
//    public WidgetFlipBook(){
//        pages = new ArrayList<WidgetPage>();
//        currentPage = 0;
//    }
//
//    public void addPage(WidgetPage page){
//        if(pages.size()==0){
//            page.setPrevButtonVisible(false);
//        }else{
//            pages.get(pages.size()-1).setNextButtonVisible(true);
//        }
//        pages.add(page);
//        page.setNextButtonVisible(false);
//        this.getStage().addActor(page);
//    }
//
//    public void showPreviousPage(){
//        if(currentPage == 0){
//            throw new RuntimeException("Already at first page");
//        }
//        currentPage--;
//    }
//
//    public void showNextPage(){
//        if(currentPage == pages.size()-1){
//            throw new RuntimeException("Already at last page");
//        }
//        currentPage++;
//    }
//
//    @Override
//    public void draw(Batch batch, float parentAlpha) {
//        super.draw(batch, parentAlpha);
//        pages.get(currentPage).draw(batch,parentAlpha);
//    }
//}
