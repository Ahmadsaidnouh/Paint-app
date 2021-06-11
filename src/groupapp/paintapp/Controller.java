package groupapp.paintapp;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JColorChooser;

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

    private DrawingPanel_1 panel;
    private static Controller instance = null;
    private String selectedShape = "NONE";
    private Color color = Color.BLACK;
    private float size;

    private boolean undoAfterClear = false;
    private boolean redoAfterUndo = false;
    private boolean moveChecked = false;
    private boolean fillChecked = false;
    private boolean deleteChecked = false;
    private boolean copyChecked = false;
    private boolean resizeChecked = false;
    private boolean drawFilledChecked = false;

    Shape tempShape = null;
    int index;
    Point currentPt = new Point();
    Point prevPt = new Point();
    boolean isCopied = false;

    private State state = new State();
    private ArrayList<Shape> shapes = new ArrayList<>();
    private ArrayList<Shape> copyofState = new ArrayList<>();

//    private JButton redoBtn = new JButton("redo");
//    private JButton undoBtn = new JButton("undo");
//    private JButton clearBtn = new JButton("clear");
    private Controller(DrawingPanel_1 panel) {
//        undoBtn.setEnabled(false);
//        redoBtn.setEnabled(false);
//        clearBtn.setEnabled(false);
        this.panel = panel;
    }

    // singleton class that only can have one instance
    public static Controller getInstance(DrawingPanel_1 panel) {
        if (instance == null) {
            instance = new Controller(panel);
        }
        return instance;
    }

    public void mousePressed(MouseEvent e) {
        undoAfterClear = false;

        if (state.getSizeOfCurrent() > 0) {
//            undoBtn.setEnabled(true);
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
            shapes.add(ShapeFactory.newShape(selectedShape, e.getPoint(), color, size, drawFilledChecked));
        } else {
            if (selectedShape.equals("NONE")) {
                return;
            }
            shapes.add(ShapeFactory.newShape(selectedShape, e.getPoint(), color, size));
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (moveChecked) {
            if (index == -1) {
                return;
            }
            shapes.add(shapes.get(index));
            shapes.remove(index);

            copyofState = Helper.getCopyOfCurrnetState(shapes);
            state.addState(copyofState);
            panel.repaint();
        } else if (fillChecked) {
            if (index == -1) {
                return;
            }
            shapes.get(index).setColor(color);
            shapes.get(index).setIsFilled(true);
            shapes.add(ShapeFactory.getCopyOfShape(shapes.get(index), null));
            shapes.add(shapes.get(index));
            shapes.remove(shapes.get(index));

            copyofState = Helper.getCopyOfCurrnetState(shapes);
            state.addState(copyofState);
            panel.repaint();
        } else if (deleteChecked) {
            copyofState = Helper.getCopyOfCurrnetState(shapes);
            state.addState(copyofState);
            panel.repaint();
        } else if (copyChecked) {
            copyofState = Helper.getCopyOfCurrnetState(shapes);
            state.addState(copyofState);
            panel.repaint();
        } else if (resizeChecked) {
            copyofState = Helper.getCopyOfCurrnetState(shapes);
            state.addState(copyofState);
            panel.repaint();
        } else if (drawFilledChecked) {
            int lastElement = shapes.size() - 1;
            shapes.get(lastElement).setSecondPos(e.getX(), e.getY());
            shapes.get(lastElement).setIsEdited(true);

            copyofState = Helper.getCopyOfCurrnetState(shapes);
            state.addState(copyofState);
            panel.repaint();
        } else {
            if (selectedShape.equals("NONE")) {
                return;
            }
            int lastElement = shapes.size() - 1;
            shapes.get(lastElement).setSecondPos(e.getX(), e.getY());

            copyofState = Helper.getCopyOfCurrnetState(shapes);
            state.addState(copyofState);
            panel.repaint();
        }

//        undoBtn.setEnabled(true);
//        clearBtn.setEnabled(true);
//        if (state.getSizeOfCleared() > 0) {
//            redoBtn.setEnabled(true);
//        } else {
//            state.clearedStateClear();
//            redoBtn.setEnabled(false);
//        }
    }

    public void mouseDragged(MouseEvent e) {
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
                int lastElement = shapes.size() - 1;
                shapes.get(lastElement).setSecondPos(e.getX(), e.getY());
                panel.repaint();
            }
        } else {
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

//                undoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCurrent()));
//                redoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCleared()));
//                clearBtn.setEnabled(Helper.isBtnEnabled(shapes.size()));
                panel.repaint();
            } else {
                System.out.println("redo t != null --->" + shapes.size());
                state.clearedStateClear();
//                redoBtn.setEnabled(false);
//
//                undoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCurrent()));
//                clearBtn.setEnabled(Helper.isBtnEnabled(shapes.size()));
                panel.repaint();
            }
        } else {
            state.clearedStateClear();
//            redoBtn.setEnabled(false);

//            undoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCurrent()));
//            clearBtn.setEnabled(Helper.isBtnEnabled(shapes.size()));
            panel.repaint();
        }
    }

    public void undo() {
        if (undoAfterClear) {
            ArrayList<Shape> t = Helper.getCopyOfCurrnetState(state.getLastElement());
            state.myStateClear();
            if (t != null) {
                shapes = t;

//                undoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCurrent()));
//                redoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCleared()));
//                clearBtn.setEnabled(Helper.isBtnEnabled(shapes.size()));
                panel.repaint();
            } else {
                shapes.clear();
                state.myStateClear();
//                undoBtn.setEnabled(false);

//                redoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCleared()));
//                clearBtn.setEnabled(Helper.isBtnEnabled(shapes.size()));
                panel.repaint();
            }
        } else {
            redoAfterUndo = true;
//                    redoBtn.setEnabled(true);
            System.out.println("||||||||||||||||||||||||||||||||||||||||||");
            ArrayList<Shape> t = Helper.getCopyOfCurrnetState(state.undoState());
            if (t != null) {
                shapes = t;

//                undoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCurrent()));
//                redoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCleared()));
//                clearBtn.setEnabled(Helper.isBtnEnabled(shapes.size()));
                panel.repaint();
            } else {
                shapes.clear();
                state.myStateClear();

//                undoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCurrent()));
//                redoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCleared()));
//                clearBtn.setEnabled(Helper.isBtnEnabled(shapes.size()));
                panel.repaint();
            }
        }
    }

    public void clear() {
        undoAfterClear = true;
        state.clearedStateClear();
        shapes.clear();
//        undoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCurrent()));
//        redoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCleared()));
//        clearBtn.setEnabled(Helper.isBtnEnabled(shapes.size()));
        panel.repaint();

    }

    public void drawOval() {
        selectedShape = "oval";
    }

    public void drawCircle() {
        selectedShape = "circ";
    }

    public void drawLine() {
        selectedShape = "Line";
    }

    public void drawRect() {
        selectedShape = "Rect";
    }

    public void colorPicker() {
        // TODO FIX COLOR PICKER
        Color prevColor = color;
        color = JColorChooser.showDialog(null, "Pick a color", Color.BLACK);
        try {
            System.out.println(color.toString());
        } catch (Exception ex) {
            color = prevColor;
            System.out.println("exception");
        }
//        colordisplay.setBackground(color);
    }

    public void drawSquare() {
        selectedShape = "squ";
    }

    public void drawTriangle() {
        selectedShape = "trig";
    }

    /*JButton redoBtn = new JButton("redo");
		redoBtn.addActionListener(new ActionListener() {
                        @Override
			public void actionPerformed(ActionEvent e) {
                            System.out.println("||||||||||||||||||||||||||||||||||||||||||");
                            ArrayList<Shape> t = Helper.getCopyOfCurrnetState(state.redoState());
                            if(t != null)
                            {
                                shapes = t;
                                System.out.println("redo t != null --->" + shapes.size());
                                panel.repaint();
                            }
                            else
                            {
                                System.out.println("redo t != null --->" + shapes.size());
                                panel.repaint();
                            }
			}
		});
		redoBtn.setBounds(46, 576, 85, 21);
//                redoBtn.setEnabled(false);
		panel.add(redoBtn);

		JButton undoBtn = new JButton("undo");
		undoBtn.addActionListener(new ActionListener() {
                        @Override
			public void actionPerformed(ActionEvent e) {
                            if(undoAfterClear)
                            {
                                ArrayList<Shape> t = Helper.getCopyOfCurrnetState(state.getLastElement());
                                state.myStateClear();
                                if(t != null)
                                {
                                    shapes = t;
                                    panel.repaint();
                                }
                                else
                                {
                                    shapes.clear();
                                    panel.repaint();
                                }
                            }
                            else
                            {
                                System.out.println("||||||||||||||||||||||||||||||||||||||||||");
                                ArrayList<Shape> t = Helper.getCopyOfCurrnetState(state.undoState());
                                if (t != null) {
                                    shapes = t;
                                    panel.repaint();
                                } else {
                                    shapes.clear();
                                    panel.repaint();
                                }
                            }
			}
		});
		undoBtn.setBounds(46, 545, 85, 21);
		panel.add(undoBtn);

		JButton clearBtn = new JButton("clear");
		clearBtn.addActionListener(new ActionListener() {
                        @Override
			public void actionPerformed(ActionEvent e) {
                                undoAfterClear = true;
                                state.clearedStateClear();
                                shapes.clear();
				panel.repaint();
			}
		});
		clearBtn.setFont(new Font("Tahoma", Font.PLAIN, 15));
		clearBtn.setBounds(46, 607, 85, 21);
		panel.add(clearBtn);*/
//    public void move {
//            if (moveCheckBox.isSelected()) {
//                moveChecked = true;
//                fillChecked = false;
//                deleteChecked = false;
//                copyChecked = false;
//                resizeChecked = false;
//                drawFilledChecked = false;
//                drawFilledCheckBox.setSelected(false);
//                resizeCheckBox.setSelected(false);
//                fillCheckBox.setSelected(false);
//                deleteCheckBox.setSelected(false);
//                copyCheckBox.setSelected(false);
//            } else {
//                moveChecked = false;
//            }
//        }
//    }
//    public void fill() {
//                if (fillCheckBox.isSelected()) {
//                fillChecked = true;
//                moveChecked = false;
//                deleteChecked = false;
//                copyChecked = false;
//                resizeChecked = false;
//                drawFilledChecked = false;
//                drawFilledCheckBox.setSelected(false);
//                resizeCheckBox.setSelected(false);
//                moveCheckBox.setSelected(false);
//                deleteCheckBox.setSelected(false);
//                copyCheckBox.setSelected(false);
//            } else {
//                fillChecked = false;
//            }
//        }
//    }
//    public void delete(){
//                if (deleteCheckBox.isSelected()) {
//                deleteChecked = true;
//                moveChecked = false;
//                fillChecked = false;
//                copyChecked = false;
//                resizeChecked = false;
//                drawFilledChecked = false;
//                drawFilledCheckBox.setSelected(false);
//                resizeCheckBox.setSelected(false);
//                moveCheckBox.setSelected(false);
//                fillCheckBox.setSelected(false);
//                copyCheckBox.setSelected(false);
//            } else {
//                deleteChecked = false;
//            }
//        }
//    }
//
//    public void copy() {
//                if (copyCheckBox.isSelected()) {
//                copyChecked = true;
//                deleteChecked = false;
//                moveChecked = false;
//                fillChecked = false;
//                resizeChecked = false;
//                drawFilledChecked = false;
//                drawFilledCheckBox.setSelected(false);
//                resizeCheckBox.setSelected(false);
//                moveCheckBox.setSelected(false);
//                fillCheckBox.setSelected(false);
//                deleteCheckBox.setSelected(false);
//            } else {
//                copyChecked = false;
//            }
//        }
//    }
//    public void resize() {
//                if (resizeCheckBox.isSelected()) {
//                resizeChecked = true;
//                copyChecked = false;
//                deleteChecked = false;
//                moveChecked = false;
//                fillChecked = false;
//                drawFilledChecked = false;
//                drawFilledCheckBox.setSelected(false);
//                moveCheckBox.setSelected(false);
//                fillCheckBox.setSelected(false);
//                deleteCheckBox.setSelected(false);
//                copyCheckBox.setSelected(false);
//            } else {
//                resizeChecked = false;
//            }
//        }
//    }
//   public void drawFill() {
//                if (drawFilledCheckBox.isSelected()) {
//                drawFilledChecked = true;
//                resizeChecked = false;
//                copyChecked = false;
//                deleteChecked = false;
//                moveChecked = false;
//                fillChecked = false;
//                moveCheckBox.setSelected(false);
//                fillCheckBox.setSelected(false);
//                deleteCheckBox.setSelected(false);
//                copyCheckBox.setSelected(false);
//                resizeCheckBox.setSelected(false);
//            } else {
//                drawFilledChecked = false;
//            }
//        }
//    }
//@Override
//        public void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        for (Shape shape : shapes) {
//            if (moveChecked || fillChecked) {
//                shape.edit(g, "");
//            } else {
//                if (shape.isIsEdited()) {
//                    shape.edit(g, "");
//                } else {
//                    shape.draw(g);
//                }
//
//            }
//        }
//    }
//
//    @Override
//        public void actionPerformed(ActionEvent e) {
//        selectedShape = e.getActionCommand();
//        System.out.printf("%s \n", selectedShape);
//    }
}
