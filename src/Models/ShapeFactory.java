/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Ahmad Nouh
 */
public class ShapeFactory {
    
    public static Shape newShape(String type, Point e, Color color, float size, boolean drawFilledChecked)
    {
        switch (type) {
            case ("Line"):
                 return new Line((int)e.getX(), (int)e.getY(), color, size);
            case ("Rect"):
                return new Rectangle((int)e.getX(), (int)e.getY(), color, size, drawFilledChecked);
            case ("circ"):
                return new Circle((int)e.getX(), (int)e.getY(), color, size, drawFilledChecked);
            case ("oval"):
                return new Oval((int)e.getX(), (int)e.getY(), color, size, drawFilledChecked);
            case ("squ"):
                return new Square((int)e.getX(), (int)e.getY(), color, size, drawFilledChecked);
            case ("trig"):
                return new Triangle((int)e.getX(), (int)e.getY(), color, size, drawFilledChecked);
            default :
                return null;
        }
    }
    public static Shape newShape(String type, Point e, Color color, float size)
    {
        switch (type) {
            case ("Line"):
                 return new Line((int)e.getX(), (int)e.getY(), color, size);
            case ("Rect"):
                return new Rectangle((int)e.getX(), (int)e.getY(), color, size);
            case ("circ"):
                return new Circle((int)e.getX(), (int)e.getY(), color, size);
            case ("oval"):
                return new Oval((int)e.getX(), (int)e.getY(), color, size);
            case ("squ"):
                return new Square((int)e.getX(), (int)e.getY(), color, size);
            case ("trig"):
                return new Triangle((int)e.getX(), (int)e.getY(), color, size);
            default :
                return null;
        }
    }
    public static Shape getCopyOfShape(Shape s, Shape copy) {
//        String type = s.type;
        int x1 = s.getX1();
        int y1 = s.getY1();
        int x2 = s.getX2();
        int y2 = s.getY2();
        Color color = s.getColor();
        float stroke = s.getStroke();
        int cornerPtX = s.getCornerPt().x;
        int cornerPtY = s.getCornerPt().y;
        int helpingPtX = s.getHelpingPtX();
        int helpingPtY = s.getHelpingPtY();
        boolean isEdited = s.isIsEdited();
        boolean isFilled = s.isIsFilled();

        String t = s.getType();
        Shape newS = null;
        switch (t) {
            case ("Circle"):
                newS = new Circle(x1, y1, color, stroke);

                /*newS.x2 = x2;
                newS.y2 = y2;

                newS.cornerPt.x = cornerPtX;
                newS.cornerPt.y = cornerPtY;
                newS.helpingPtX = helpingPtX;
                newS.helpingPtY = helpingPtY;
                newS.isEdited = isEdited;
                newS.isFilled = isFilled;*/
                newS.setX2(x2);
                newS.setY2(y2);
                
                newS.setCornerPt(new Point(cornerPtX, cornerPtY));
                newS.setHelpingPtX(helpingPtX);
                newS.setHelpingPtY(helpingPtY);
                newS.setIsFilled(isFilled);
                newS.setIsEdited(isEdited);
                break;
            case ("Line"):
                newS = new Line(x1, y1, color, stroke);
                
                newS.setX2(x2);
                newS.setY2(y2);
                
                newS.setCornerPt(new Point(cornerPtX, cornerPtY));
                newS.setHelpingPtX(helpingPtX);
                newS.setHelpingPtY(helpingPtY);
                newS.setIsFilled(isFilled);
                newS.setIsEdited(isEdited);
                break;
            case ("Oval"):
                newS = new Oval(x1, y1, color, stroke);

                newS.setX2(x2);
                newS.setY2(y2);
                
                newS.setCornerPt(new Point(cornerPtX, cornerPtY));
                newS.setHelpingPtX(helpingPtX);
                newS.setHelpingPtY(helpingPtY);
                newS.setIsFilled(isFilled);
                newS.setIsEdited(isEdited);
                break;
            case ("Rectangle"):
                newS = new Rectangle(x1, y1, color, stroke);

                newS.setX2(x2);
                newS.setY2(y2);
                
                newS.setCornerPt(new Point(cornerPtX, cornerPtY));
                newS.setHelpingPtX(helpingPtX);
                newS.setHelpingPtY(helpingPtY);
                newS.setIsFilled(isFilled);
                newS.setIsEdited(isEdited);
                break;
            case ("Square"):
                newS = new Square(x1, y1, color, stroke);

                newS.setX2(x2);
                newS.setY2(y2);
                
                newS.setCornerPt(new Point(cornerPtX, cornerPtY));
                newS.setHelpingPtX(helpingPtX);
                newS.setHelpingPtY(helpingPtY);
                newS.setIsFilled(isFilled);
                newS.setIsEdited(isEdited);
                break;
            case ("Triangle"):
                newS = new Triangle(x1, y1, color, stroke);

                newS.setX2(x2);
                newS.setY2(y2);
                
                newS.setCornerPt(new Point(cornerPtX, cornerPtY));
                newS.setHelpingPtX(helpingPtX);
                newS.setHelpingPtY(helpingPtY);
                newS.setIsFilled(isFilled);
                newS.setIsEdited(isEdited);
                break;
        }
        return newS;
    }
    
    public static ArrayList<Shape> getCopyOfCurrnetState(ArrayList<Shape> b)
        {
            ArrayList<Shape> n = new ArrayList<>();
            Shape t = null;
            if(b != null)
            {
                for(Shape s : b)
                {
                    t = getCopyOfShape(s, t);
                    n.add(t);
                
                }
                return n;
            }
            else
                return null;
            
        }
    
}
