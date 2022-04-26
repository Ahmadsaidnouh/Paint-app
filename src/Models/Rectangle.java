/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

public class Rectangle extends Shape {

    public Rectangle(int x1, int y1, Color color, float storke) {
        super(x1, y1, color, storke);
        this.setType("Rectangle");
    }

    public Rectangle(int x1, int y1, Color color, float storke, boolean drawFilled) {
        super(x1, y1, color, storke, drawFilled);
        this.setType("Rectangle");
    }

    @Override
    public void draw(Graphics g) {
        this.setCornerPt(new Point(Math.min(this.getX1(), this.getX2()), Math.min(this.getY1(), this.getY2())));
//        cornerPt.x = ;
//        cornerPt.y = ;
        int width = Math.abs(this.getX2() - this.getX1());
        int height = Math.abs(this.getY2() - this.getY1());

        this.setHelpingPtX(width);
        this.setHelpingPtY(height);

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(this.getStroke()));
        g2.setColor(this.getColor());
        if (this.isIsFilled()) {
            g2.fillRect(this.getCornerPt().x, this.getCornerPt().y, width, height);
        } else {
            g2.drawRect(this.getCornerPt().x, this.getCornerPt().y, width, height);
        }
    }

    public void edit(Graphics g, String s) {
        this.setIsEdited(true);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(this.getStroke()));
        g2.setColor(this.getColor());
        if (this.isIsFilled()) {
            g2.fillRect(this.getCornerPt().x, this.getCornerPt().y, this.getHelpingPtX(), this.getHelpingPtY());
        } else {
            g2.drawRect(this.getCornerPt().x, this.getCornerPt().y, this.getHelpingPtX(), this.getHelpingPtY());
        }
    }

    @Override
    public void move(Point current, Point prev) {
        this.getCornerPt().translate((int) (current.getX() - prev.getX()), (int) (current.getY() - prev.getY()));
    }

    public boolean contains(float x, float y) {
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
