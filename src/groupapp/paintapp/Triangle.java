/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groupapp.paintapp;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

public class Triangle extends Shape {

    public Triangle(int x1, int y1, Color color, float storke) {
        super(x1, y1, color, storke);
        this.setType("Triangle");
    }
    public Triangle(int x1, int y1, Color color, float storke, boolean drawFilled) {
        super(x1, y1, color, storke, drawFilled);
        this.setType("Triangle");
    }
    
    @Override
    void draw(Graphics g) {
        Point a = new Point(this.getX1() + (this.getX2() - this.getX1()) / 2, this.getY1());
        Point b = new Point(this.getX1(), this.getY1() + Math.abs(this.getY1() - this.getY2()));
        Point c = new Point(this.getX2(), this.getY2());
        this.setCornerPt(new Point(/*this.getX1()*/Math.min(this.getX1(), this.getX2()), this.getY1()));
        this.setHelpingPtX(Math.abs(this.getX1() - this.getX2()));
        this.setHelpingPtY(Math.abs(this.getY1() - this.getY2()));

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(this.getStroke()));
        if (this.isIsFilled()) 
        {
            int[] xPts = {a.x, b.x, c.x};
            int[] yPts = {a.y, b.y, c.y};
            g2.fillPolygon(xPts, yPts, 3);
        }
        else
        {
            g2.drawLine(a.x, a.y, b.x, b.y);
            g2.drawLine(b.x, b.y, c.x, c.y);
            g2.drawLine(c.x, c.y, a.x, a.y);
            
        }
    }

    @Override
    void edit(Graphics g, String s) {
        this.setIsEdited(true);
        Point a1 = new Point(this.getCornerPt().x + this.getHelpingPtX() / 2, this.getCornerPt().y);
        Point b1 = new Point(this.getCornerPt().x, this.getCornerPt().y + this.getHelpingPtY());
        Point c1 = new Point(this.getCornerPt().x + this.getHelpingPtX(), this.getCornerPt().y + this.getHelpingPtY());

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(this.getStroke()));
        g2.setColor(this.getColor());
            if (this.isIsFilled()) 
            {
                int[] xPts = {a1.x, b1.x, c1.x};
                int[] yPts = {a1.y, b1.y, c1.y};
                g2.fillPolygon(xPts, yPts, 3);
            }
            else
            {
                g2.drawLine(a1.x, a1.y, b1.x, b1.y);
                g2.drawLine(b1.x, b1.y, c1.x, c1.y);
                g2.drawLine(c1.x, c1.y, a1.x, a1.y);
            }
    }

    @Override
    void move(Point current, Point prev) {
        this.getCornerPt().translate((int) (current.getX() - prev.getX()), (int) (current.getY() - prev.getY()));
    }

    @Override
    boolean contains(float x, float y) {
        int x1 = (int) this.getCornerPt().x;
        int y1 = (int) this.getCornerPt().y;
        int width = this.getHelpingPtX();
        int height = this.getHelpingPtY();
        if ((x >= x1 && x <= (x1 + width)) && (y >= y1 && y <= (y1 + height))) {
            return true;
        }
        return false;
    }
}
