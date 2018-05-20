package com.halogengames.poof.library;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.halogengames.poof.Poof;

/**
 * Created by Rohit on 20-05-2018.
 */

public class BasicShapes{

    private ShapeRenderer renderer;
    private float lineWidth;

    public BasicShapes(Poof game){
        this.renderer = game.renderer;
    }

    public void setLineWidth(float width){
        lineWidth = width;
    }

    public void drawLine(float x1, float y1, float x2, float y2){
        float angle = (float)(Math.atan2(y2-y1,x2-x1)*180/Math.PI);
        float lineLen = (float)Math.sqrt((y2-y1)*(y2-y1) + (x2-x1)*(x2-x1));
        renderer.rect(x1,y1-lineWidth/2,0,lineWidth/2, lineLen,lineWidth,1,1,angle);

        renderer.circle(x1,y1,lineWidth/2);
        renderer.circle(x2,y2,lineWidth/2);
    }

    public void drawRoundRectFilled(float x, float y, float width, float height, float rad){
        renderer.rect(x,y+rad,rad,height-2*rad);
        renderer.rect(x+rad,y+height-rad,width-2*rad, rad);
        renderer.rect(x+width-rad,y+rad,rad,height-2*rad);
        renderer.rect(x+rad,y,width-2*rad,rad);
        renderer.rect(x+rad,y+rad,width-2*rad,height-2*rad);

        renderer.arc(x+rad,y+rad,rad,180,90,10);
        renderer.arc(x+width-rad,y+rad,rad,270,90,10);
        renderer.arc(x+rad,y+height-rad,rad,90,90,10);
        renderer.arc(x+width-rad,y+height-rad,rad,0,90,10);
    }

    public void drawLineArc(float x, float y, float rad, float start, float degrees){
        this.drawLineArc(x,y,rad,start,degrees,(int)(degrees/22.5));
    }

    public void drawLineArc(float x, float y, float rad, float start, float degrees, int segments){
        start = start*(float)Math.PI/180;
        degrees = degrees*(float)Math.PI/180;
        float curX = x + rad*(float)Math.cos(start);
        float curY = y + rad*(float)Math.sin(start);

        for(int i=1; i<=segments; i++){
            float newX = x + rad*(float)Math.cos(start+i*degrees/segments);
            float newY = y + rad*(float)Math.sin(start+i*degrees/segments);
            drawLine(curX,curY,newX,newY);
            curX = newX;
            curY = newY;
        }

    }

    public void drawRoundRectLine(float x, float y, float width, float height, float rad){
        drawLine(x,y+rad,x,y+height-rad);
        drawLine(x+rad,y+height,x+width-rad,y+height);
        drawLine(x+width,y+rad,x+width,y+height-rad);
        drawLine(x+rad,y,x+width-rad,y);

        drawLineArc(x+rad,y+rad,rad,180,90,10);
        drawLineArc(x+width-rad,y+rad,rad,270,90,10);
        drawLineArc(x+rad,y+height-rad,rad,90,90,10);
        drawLineArc(x+width-rad,y+height-rad,rad,0,90,10);
    }
}
