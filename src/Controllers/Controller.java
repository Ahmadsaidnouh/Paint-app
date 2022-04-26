package Controllers;

import Views.DrawingPanel;
import Models.Shape;
import Models.ShapeFactory;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author hussein
 */
public class Controller {

//    private String selectedShape = "NONE";
//    private Color color = Color.BLACK;
//    private float size;
//
//    private boolean undoAfterClear = false;
//    private boolean redoAfterUndo = false;
//    private boolean moveChecked = false;
//    private boolean fillChecked = false;
//    private boolean deleteChecked = false;
//    private boolean copyChecked = false;
//    private boolean resizeChecked = false;
//    private boolean drawFilledChecked = false;
//    private boolean drawing = false;
//
//    private Shape tempShape = null;
//    private int index;
//    private Point currentPt = new Point();
//    private Point prevPt = new Point();
////    private boolean isCopied = false;
//
//    private State state = new State();
//    private ArrayList<Shape> shapes = new ArrayList<>();
//    private ArrayList<Shape> copyofState = new ArrayList<>();
//
//    private JButton redoBtn = new JButton("redo");
//    private JButton undoBtn = new JButton("undo");
//    private JButton clearBtn = new JButton("clear");
    
    //////////////////////////////
    private DrawingPanel panel;
    private static Controller instance = null;
    
    private String selectedShape = "NONE";
    private Color color = Color.BLACK;
    private float lineStroke = 1;

    private boolean undoAfterClear = false;
    private boolean redoAfterUndo = false;
    private boolean moveChecked = false;
    private boolean fillChecked = false;
    private boolean deleteChecked = false;
    private boolean copyChecked = false;
    private boolean resizeChecked = false;
    private boolean drawFilledChecked = false;
    private boolean drawing = false;

    private Shape tempShape = null;
    private int index;
    private Point currentPt = new Point();
    private Point prevPt = new Point();

    private State state = new State();
    private ArrayList<Shape> shapes = new ArrayList<>();
    private ArrayList<Shape> copyofState = new ArrayList<>();
    private JButton redoBtn = new JButton();
    private JButton undoBtn = new JButton();
    private JButton clearBtn = new JButton();
    private JButton fillBtn = new JButton();
    private String imgFolder = "src" + File.separator + "Assets" + File.separator + "Images" + File.separator;
    
    
    private Controller(DrawingPanel panel,JButton redoBtn,JButton undoBtn,JButton clearBtn) {
        this.redoBtn = redoBtn;
        this.undoBtn = undoBtn;
        this.clearBtn = clearBtn;
//        this.clearBtn = fillBtn;
        this.panel = panel;
        this.undoBtn.setEnabled(false);
        this.redoBtn.setEnabled(false);
        this.clearBtn.setEnabled(false);
    }
//    private void en()

    // singleton class that only can have one instance
    public static Controller getInstance(DrawingPanel panel, JButton re, JButton un, JButton cl) {
        if (instance == null) {
            instance = new Controller(panel, re, un, cl);
        }
        return instance;
    }

    public void mousePressed(MouseEvent e) {
        if(drawFilledChecked && selectedShape.equals("NONE"))
            return;
        undoAfterClear = false;
        System.out.println(shapes.size() + "<---size");
        if (state.getSizeOfCurrent() > 0) {
            undoBtn.setEnabled(true);
        }
        if (redoAfterUndo) {
            state.clearedStateClear();
            redoAfterUndo = false;
        }
        if (moveChecked) {
            index = Helper.findTheIndex(e.getX(), e.getY(), shapes);
            if (index == -1) {
                return;
            }
            tempShape = ShapeFactory.getCopyOfShape(shapes.get(index), tempShape);
            tempShape.setIsEdited(true);
            prevPt = e.getPoint();
        } else if (fillChecked) {
            index = Helper.findTheIndex(e.getX(), e.getY(), shapes);
        } else if (deleteChecked) {
            index = Helper.findTheIndex(e.getX(), e.getY(), shapes);
            if (index == -1) {
                return;
            }
            shapes.remove(index);
        } else if (copyChecked) {
            
            index = Helper.findTheIndex(e.getX(), e.getY(), shapes);
            if (index == -1) {
                return;
            }
            tempShape = ShapeFactory.getCopyOfShape(shapes.get(index), tempShape);
            tempShape.setIsEdited(true);
            shapes.add(tempShape);
            prevPt = e.getPoint();
        } else if (resizeChecked) {
            
            index = Helper.findTheIndex(e.getX(), e.getY(), shapes);
            if (index == -1) {
                return;
            }
            shapes.get(index).setIsEdited(false);
            shapes.get(index).setX1(shapes.get(index).getCornerPt().x);
            shapes.get(index).setY1(shapes.get(index).getCornerPt().y);
        } else if (drawFilledChecked) {
            System.out.println("hi");
            shapes.add(ShapeFactory.newShape(selectedShape, e.getPoint(), color, lineStroke, drawFilledChecked));
        } else if(drawing){
            if (selectedShape.equals("NONE")) {
                return;
            }
            shapes.add(ShapeFactory.newShape(selectedShape, e.getPoint(), color, lineStroke));
        }
    }

    public void mouseReleased(MouseEvent e) {
        if(drawFilledChecked && selectedShape.equals("NONE"))
            return;
        if (moveChecked) {
            if (index == -1) {
                return;
            }
            shapes.add(shapes.get(index));
            shapes.remove(index);
            if (shapes.size() > 0) {
                copyofState = Helper.getCopyOfCurrnetState(shapes);
                state.addState(copyofState);
                panel.repaint();
            }
        } else if (fillChecked) {
            if (index == -1) {
                return;
            }
            if ((shapes.get(index).isIsFilled() && shapes.get(index).getColor().equals(color)) || shapes.get(index).getType().equals("Line")) {

            } else {
                shapes.get(index).setColor(color);
                shapes.get(index).setIsFilled(true);
                shapes.add(ShapeFactory.getCopyOfShape(shapes.get(index), null));
                shapes.remove(shapes.get(index));
                if (shapes.size() > 0) {
                    copyofState = Helper.getCopyOfCurrnetState(shapes);
                    state.addState(copyofState);
                    panel.repaint();
                }
            }
        } else if (deleteChecked) {
            if (shapes.size() > -1) {
                copyofState = Helper.getCopyOfCurrnetState(shapes);
                state.addState(copyofState);
                panel.repaint();
            } else {
                panel.repaint();
            }
        } else if (copyChecked) {
            if (shapes.size() > 0) {
                copyofState = Helper.getCopyOfCurrnetState(shapes);
                state.addState(copyofState);
                panel.repaint();
            }
        } else if (resizeChecked) {
            if (shapes.size() > 0) {
                copyofState = Helper.getCopyOfCurrnetState(shapes);
                state.addState(copyofState);
                panel.repaint();
            }
        } else if (drawFilledChecked) {
            int lastElement = shapes.size() - 1;
            shapes.get(lastElement).setSecondPos(e.getX(), e.getY());
            shapes.get(lastElement).setIsEdited(true);

            if (shapes.size() > 0) {
                copyofState = Helper.getCopyOfCurrnetState(shapes);
                state.addState(copyofState);
                panel.repaint();
            }
        } else if (drawing) {
            if (selectedShape.equals("NONE")) {
                return;
            }
            int lastElement = shapes.size() - 1;
            shapes.get(lastElement).setSecondPos(e.getX(), e.getY());

            if (shapes.size() > 0) {
                copyofState = Helper.getCopyOfCurrnetState(shapes);
                state.addState(copyofState);
                panel.repaint();
            }
        }
        if (state.getSizeOfCurrent() > 0) {
            undoBtn.setEnabled(true);
        }
        if (shapes.size() > 0) {
            clearBtn.setEnabled(true);
        }

        if (state.getSizeOfCleared() > 0) {
            redoBtn.setEnabled(true);
        } else {
            state.clearedStateClear();
            redoBtn.setEnabled(false);
        }
    }

    public void mouseDragged(MouseEvent e) {
        if(drawFilledChecked && selectedShape.equals("NONE"))
            return;
        if (moveChecked) {
                    if (index == -1) {
                        return;
                    }
                    currentPt = e.getPoint();
                    shapes.get(index).move(currentPt, prevPt);
                    prevPt = currentPt;
                    panel.repaint();
                } else if (fillChecked) {

                } else if (deleteChecked) {

                } else if (copyChecked) {
                    if (index == -1) {
                        return;
                    }
                    currentPt = e.getPoint();
                    shapes.get(shapes.size() - 1).move(currentPt, prevPt);
                    prevPt = currentPt;
                    panel.repaint();
                } else if (resizeChecked) {
                    if (index == -1) {
                        return;
                    }
                    shapes.get(index).setSecondPos(e.getX(), e.getY());
                    panel.repaint();
                } else if (drawFilledChecked) {
                    if (shapes.size() > 0) {
                        System.out.println(shapes.size());
                        int lastElement = shapes.size() - 1;
                        shapes.get(lastElement).setSecondPos(e.getX(), e.getY());
                        panel.repaint();
                    }
                } else if(drawing){
                    if (selectedShape.equals("NONE")) {
                        return;
                    }
                    if (shapes.size() > 0) {
                        int lastElement = shapes.size() - 1;
                        shapes.get(lastElement).setSecondPos(e.getX(), e.getY());
                        panel.repaint();
                    }
                }

    }

    public void redo() {
        if (redoAfterUndo) {

                    System.out.println("||||||||||||||||||||||||||||||||||||||||||");
                    ArrayList<Shape> t = Helper.getCopyOfCurrnetState(state.redoState());
                    if (t != null) {
                        shapes = t;
                        System.out.println("redo t != null --->" + shapes.size());
                        
                        undoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCurrent()));
                        redoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCleared()));
                        clearBtn.setEnabled(Helper.isBtnEnabled(shapes.size()));
                        panel.repaint();
                    } else {
                        System.out.println("redo t != null --->" + shapes.size());
                        state.clearedStateClear();
                        redoBtn.setEnabled(false);
                        
                        undoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCurrent()));
                        clearBtn.setEnabled(Helper.isBtnEnabled(shapes.size()));
                        panel.repaint();
                    }
                } else {
                    state.clearedStateClear();
                    redoBtn.setEnabled(false);
                    
                    undoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCurrent()));
                    clearBtn.setEnabled(Helper.isBtnEnabled(shapes.size()));
                    panel.repaint();
                }
    }

    public void undo() {
        if (undoAfterClear) {
            ArrayList<Shape> t = Helper.getCopyOfCurrnetState(state.getLastElement());
            state.myStateClear();
            if (t != null) {
                shapes = t;

                undoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCurrent()));
                redoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCleared()));
                clearBtn.setEnabled(Helper.isBtnEnabled(shapes.size()));
                panel.repaint();
            } else {
                shapes.clear();
                state.myStateClear();
                undoBtn.setEnabled(false);

                redoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCleared()));
                clearBtn.setEnabled(Helper.isBtnEnabled(shapes.size()));
                panel.repaint();
            }
        } else {
            redoAfterUndo = true;
//                    redoBtn.setEnabled(true);
            System.out.println("||||||||||||||||||||||||||||||||||||||||||");
            ArrayList<Shape> t = Helper.getCopyOfCurrnetState(state.undoState());
            if (t != null) {
                shapes = t;

                undoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCurrent()));
                redoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCleared()));
                clearBtn.setEnabled(Helper.isBtnEnabled(shapes.size()));
                panel.repaint();
            } else {
                shapes.clear();
                state.myStateClear();

                undoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCurrent()));
                redoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCleared()));
                clearBtn.setEnabled(Helper.isBtnEnabled(shapes.size()));
                panel.repaint();
            }
        }
    }

    public void clear() {
        undoAfterClear = true;
        state.clearedStateClear();
        shapes.clear();
        undoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCurrent()));
        redoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCleared()));
        clearBtn.setEnabled(Helper.isBtnEnabled(shapes.size()));
        panel.repaint();

    }

    public void drawOval() {
        if (drawFilledChecked) {
            resetMode();
            drawFilledChecked = true;
            selectedShape = "oval";
            drawing = false;
        } else {
            resetMode();
            selectedShape = "oval";
            drawing = true;
        }
        cursorInsert();
    }

    public void drawCircle() {
        if (drawFilledChecked) {
            resetMode();
            drawFilledChecked = true;
            selectedShape = "circ";
            drawing = false;
        } else {
            resetMode();
            selectedShape = "circ";
            drawing = true;
        }
        cursorInsert();
    }

    public void drawLine() {
        if (drawFilledChecked) {
            resetMode();
            drawFilledChecked = true;
            selectedShape = "Line";
            drawing = false;
        } else {
            resetMode();
            selectedShape = "Line";
            drawing = true;
        }
        cursorInsert();
    }

    public void drawRect() {
        if (drawFilledChecked) {
            resetMode();
            drawFilledChecked = true;
            selectedShape = "Rect";
            drawing = false;
        } else {
            resetMode();
            selectedShape = "Rect";
            drawing = true;
        }
        cursorInsert();
    }

    public void drawSquare() {
        if (drawFilledChecked) {
            resetMode();
            drawFilledChecked = true;
            selectedShape = "squ";
            drawing = false;
        } else {
            resetMode();
            selectedShape = "squ";
            drawing = true;
        }
        cursorInsert();
    }

    public void drawTriangle() {
        if (drawFilledChecked) {
            resetMode();
            drawFilledChecked = true;
            selectedShape = "trig";
            drawing = false;
        } else {
            resetMode();
            selectedShape = "trig";
            drawing = true;
        }
        cursorInsert();
    }

    public void colorPicker(JPanel colorPanel) {
        Color prevColor = color;
        color = JColorChooser.showDialog(null, "Pick a color", Color.BLACK);
        try {
            System.out.println(color.toString());
        } catch (Exception ex) {
            color = prevColor;
            System.out.println("exception");
        }
        colorPanel.setBackground(color);
        colorPanel.repaint();
    }

    public void move() {
        resetCursor();
        resetMode();
        moveChecked = true;
        setCustomCursor("outline_open_with_black_24dp.png");
    }

    public void fill() {
        resetCursor();
        resetMode();
        fillChecked = true;
        setCustomCursor("outline_format_color_fill_black_24dp.png");
    }

    public void drawFill() {
        resetCursor();
        if(!selectedShape.equals("NONE"))
        {
            drawFilledChecked = true;
            switch (selectedShape)
            {
                case ("trig"):
                    drawTriangle();
                    break;
                case ("squ"):
                    drawSquare();
                    break;
                case ("Rect"):
                    drawRect();
                    break;
                case ("Line"):
                    drawLine();
                    break;
                case ("circ"):
                    drawCircle();
                    break;
                default:
                    break;
            }
        }
        else
        {
        resetMode();
        drawFilledChecked = true;
            
        }
        
    }

    public void delete() {
        resetCursor();
        resetMode();
        deleteChecked = true;
        setCustomCursor("outline_clear_black_24dp.png");
    }

    public void copy() {
        resetCursor();
        resetMode();
        copyChecked = true;
    }

    public void resize() {
        resetCursor();
        resetMode();
        resizeChecked = true;
    }

    public void resetMode() {
        copyChecked = false;
        deleteChecked = false;
        moveChecked = false;
        fillChecked = false;
        resizeChecked = false;
        drawFilledChecked = false;
        drawing = false;
        selectedShape = "NONE";
    }
//    public void resetFeatures() {
//        copyChecked = false;
//        deleteChecked = false;
//        moveChecked = false;
//        fillChecked = false;
//        resizeChecked = false;
//        drawFilledChecked = false;
//        drawing = false;
//    }

    public void cursorInsert() {
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }

    public void resetCursor() {
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    public void setCustomCursor(String filename) {
        String currentDirectory = System.getProperty("user.dir");
        Image image = Toolkit.getDefaultToolkit().createImage(currentDirectory + File.separator + imgFolder + filename);
        Cursor customCursor = Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(0, 0), filename);
        panel.setCursor(customCursor);
    }

    // Getters and Setters
    public boolean isMoveChecked() {
        return moveChecked;
    }

    public void setMoveChecked(boolean moveChecked) {
        this.moveChecked = moveChecked;
    }

    public boolean isFillChecked() {
        return fillChecked;
    }

    public void setFillChecked(boolean fillChecked) {
        this.fillChecked = fillChecked;
    }

    public ArrayList<Shape> getShapes() {
        return shapes;
    }

    public float getLineStroke() {
        return lineStroke;
    }

    public void setLineStroke(float lineStroke) {
        this.lineStroke = lineStroke;
    }

    public boolean resizedChecked()
    {
        return this.resizeChecked;
    }
    
    public void setFalseDrawFilled()
    {
        drawFilledChecked = false;
    }
    
//    public b
}
