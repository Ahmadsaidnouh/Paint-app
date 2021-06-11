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
import java.awt.geom.Line2D;

public class Line extends Shape {

    public Line(int x1, int y1, Color color, float stroke) {
        super(x1, y1, color, stroke);
        this.setType("Line");
    }

    @Override
    void draw(Graphics g) {
        this.setCornerPt(new Point(this.getX1(), this.getY1()));
//        cornerPt.y = y1;
//        this.helpingPtX = (x2 - x1);
//        this.helpingPtY = (y2 - y1);
        this.setHelpingPtX(this.getX2() - this.getX1());
        this.setHelpingPtY(this.getY2() - this.getY1());
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(this.getStroke()));
        g2.setColor(this.getColor());
        g2.draw(new Line2D.Float((int)this.getCornerPt().getX(), (int)this.getCornerPt().getY(), this.getX2(), this.getY2()));

    }

    void edit(Graphics g, String s) {
        this.setIsEdited(true);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(this.getStroke()));
        g2.setColor(this.getColor());
            g2.draw(new Line2D.Float((int)this.getCornerPt().getX(), (int)this.getCornerPt().getY(), (int)this.getCornerPt().getX() + this.getHelpingPtX(), (int)this.getCornerPt().getY() + this.getHelpingPtY()));
    }

    @Override
    void move(Point current, Point prev) {
        this.getCornerPt().translate((int) (current.getX() - prev.getX()), (int) (current.getY() - prev.getY()));
    }

    boolean contains(float x, float y) {
        int width = this.getHelpingPtX();
        int height = this.getHelpingPtY();
        int x1 = Math.min(this.getCornerPt().x, width + this.getCornerPt().x);
        int x2 = Math.max(this.getCornerPt().x, width + this.getCornerPt().x);
        int y1 = Math.min(this.getCornerPt().y, height + this.getCornerPt().y);
        int y2 = Math.max(this.getCornerPt().y, height + this.getCornerPt().y);

        if ((x >= x1 && x <= (x2)) && (y >= y1 && y <= (y2))) {
            return true;
        }
        return false;
    }

}
