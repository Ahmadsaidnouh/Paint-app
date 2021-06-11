/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groupapp.paintapp;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public abstract class Shape {
	private String type;
        private int x1;
	private int y1;
	private int x2;
	private int y2;
	private Color color;
	private float stroke;
        private Point cornerPt;
        private int helpingPtX;
        private int helpingPtY;
        private boolean isEdited;
        private boolean isFilled;

    public String getType() {
        return type;
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }

    public Color getColor() {
        return color;
    }

    public float getStroke() {
        return stroke;
    }

    public Point getCornerPt() {
        return cornerPt;
    }

    public int getHelpingPtX() {
        return helpingPtX;
    }

    public int getHelpingPtY() {
        return helpingPtY;
    }

    public boolean isIsEdited() {
        return isEdited;
    }

    public boolean isIsFilled() {
        return isFilled;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setStroke(float stroke) {
        this.stroke = stroke;
    }

    public void setCornerPt(Point cornerPt) {
        this.cornerPt = cornerPt;
    }

    public void setHelpingPtX(int helpingPtX) {
        this.helpingPtX = helpingPtX;
    }

    public void setHelpingPtY(int helpingPtY) {
        this.helpingPtY = helpingPtY;
    }

    public void setIsEdited(boolean isEdited) {
        this.isEdited = isEdited;
    }

    public void setIsFilled(boolean isFilled) {
        this.isFilled = isFilled;
    }
        
       
	
	
	
	// constructor
	public Shape(int x1, int y1, Color color, float stroke) {
                
		this.cornerPt = new Point(x1, y1);
		this.x1 = x1;
		this.y1 = y1;
		
		this.x2 = x1;
		this.y2 = y1;
		
		this.color = color;
		this.stroke = stroke;
                
                this.isEdited = false;
	}
	
        public Shape(int x1, int y1, Color color, float stroke, boolean drawFilled) {
                this.isFilled = true;
		this.cornerPt = new Point(x1, y1);
		this.x1 = x1;
		this.y1 = y1;
		
		this.x2 = x1;
		this.y2 = y1;
		
		this.color = color;
		this.stroke = stroke;
                
                this.isEdited = false;
	}
        
	// set second position
	public void setSecondPos(int x2, int y2) {
		this.x2 = x2;
		this.y2 = y2;
	}

	 abstract void draw(Graphics g);
	 abstract void edit(Graphics g,String a);
	 abstract void move(Point current, Point prev);
         abstract boolean contains(float x, float y);
}
