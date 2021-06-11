/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groupapp.paintapp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

public class DrawingPanel extends JPanel implements ActionListener {

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

    private JButton redoBtn = new JButton("redo");
    private JButton undoBtn = new JButton("undo");
    private JButton clearBtn = new JButton("clear");

    public DrawingPanel() {
        setBackground(Color.WHITE);
        undoBtn.setEnabled(false);
        redoBtn.setEnabled(false);
        clearBtn.setEnabled(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                undoAfterClear = false;

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
                    shapes.add(ShapeFactory.newShape(selectedShape, e.getPoint(), color, size, drawFilledChecked));
                } else {
                    if (selectedShape.equals("NONE")) {
                        return;
                    }
                    shapes.add(ShapeFactory.newShape(selectedShape, e.getPoint(), color, size));
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (moveChecked) {
                    if (index == -1) {
                        return;
                    }
                    shapes.add(shapes.get(index));
                    shapes.remove(index);

                    copyofState = Helper.getCopyOfCurrnetState(shapes);
                    state.addState(copyofState);
                    repaint();
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
                    repaint();
                } else if (deleteChecked) {
                    copyofState = Helper.getCopyOfCurrnetState(shapes);
                    state.addState(copyofState);
                    repaint();
                } else if (copyChecked) {
                    copyofState = Helper.getCopyOfCurrnetState(shapes);
                    state.addState(copyofState);
                    repaint();
                } else if (resizeChecked) {
                    copyofState = Helper.getCopyOfCurrnetState(shapes);
                    state.addState(copyofState);
                    repaint();
                } else if (drawFilledChecked) {
                    int lastElement = shapes.size() - 1;
                    shapes.get(lastElement).setSecondPos(e.getX(), e.getY());
                    shapes.get(lastElement).setIsEdited(true);

                    copyofState = Helper.getCopyOfCurrnetState(shapes);
                    state.addState(copyofState);
                    repaint();
                } else {
                    if (selectedShape.equals("NONE")) {
                        return;
                    }
                    int lastElement = shapes.size() - 1;
                    shapes.get(lastElement).setSecondPos(e.getX(), e.getY());

                    copyofState = Helper.getCopyOfCurrnetState(shapes);
                    state.addState(copyofState);
                    repaint();
                }

                undoBtn.setEnabled(true);
                clearBtn.setEnabled(true);

                if (state.getSizeOfCleared() > 0) {
                    redoBtn.setEnabled(true);
                } else {
                    state.clearedStateClear();
                    redoBtn.setEnabled(false);
                }

            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (moveChecked) {
                    if (index == -1) {
                        return;
                    }
                    currentPt = e.getPoint();
                    shapes.get(index).move(currentPt, prevPt);
                    prevPt = currentPt;
                    repaint();
                } else if (fillChecked) {

                } else if (deleteChecked) {

                } else if (copyChecked) {
                    if (index == -1) {
                        return;
                    }
                    currentPt = e.getPoint();
                    shapes.get(shapes.size() - 1).move(currentPt, prevPt);
                    prevPt = currentPt;
                    repaint();
                } else if (resizeChecked) {
                    if (index == -1) {
                        return;
                    }
                    shapes.get(index).setSecondPos(e.getX(), e.getY());
                    repaint();
                } else if (drawFilledChecked) {
                    if (shapes.size() > 0) {
                        int lastElement = shapes.size() - 1;
                        shapes.get(lastElement).setSecondPos(e.getX(), e.getY());
                        repaint();
                    }
                } else {
                    if (selectedShape.equals("NONE")) {
                        return;
                    }
                    if (shapes.size() > 0) {
                        int lastElement = shapes.size() - 1;
                        shapes.get(lastElement).setSecondPos(e.getX(), e.getY());
                        repaint();
                    }
                }

            }
        });

        setSize(new Dimension(1262, 600/*680*/));
        setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBounds(/*500*/1089, 0/*32*/, 163/*100*/, 700/*500*/);
        add(panel);
        panel.setLayout(null);

//                JButton redoBtn = new JButton("redo");
        redoBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (redoAfterUndo) {

                    System.out.println("||||||||||||||||||||||||||||||||||||||||||");
                    ArrayList<Shape> t = Helper.getCopyOfCurrnetState(state.redoState());
                    if (t != null) {
                        shapes = t;
                        System.out.println("redo t != null --->" + shapes.size());

                        undoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCurrent()));
                        redoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCleared()));
                        clearBtn.setEnabled(Helper.isBtnEnabled(shapes.size()));
                        repaint();
                    } else {
                        System.out.println("redo t != null --->" + shapes.size());
                        state.clearedStateClear();
                        redoBtn.setEnabled(false);

                        undoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCurrent()));
                        clearBtn.setEnabled(Helper.isBtnEnabled(shapes.size()));
                        repaint();
                    }
                } else {
                    state.clearedStateClear();
                    redoBtn.setEnabled(false);

                    undoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCurrent()));
                    clearBtn.setEnabled(Helper.isBtnEnabled(shapes.size()));
                    repaint();
                }
            }
        });
        redoBtn.setBounds(46, 576, 85, 21);
//                redoBtn.setEnabled(false);
        panel.add(redoBtn);

//		JButton undoBtn = new JButton("undo");
        undoBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoAfterClear) {
                    ArrayList<Shape> t = Helper.getCopyOfCurrnetState(state.getLastElement());
                    state.myStateClear();
                    if (t != null) {
                        shapes = t;

                        undoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCurrent()));
                        redoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCleared()));
                        clearBtn.setEnabled(Helper.isBtnEnabled(shapes.size()));
                        repaint();
                    } else {
                        shapes.clear();
                        state.myStateClear();
                        undoBtn.setEnabled(false);

                        redoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCleared()));
                        clearBtn.setEnabled(Helper.isBtnEnabled(shapes.size()));
                        repaint();
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
                        repaint();
                    } else {
                        shapes.clear();
                        state.myStateClear();

                        undoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCurrent()));
                        redoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCleared()));
                        clearBtn.setEnabled(Helper.isBtnEnabled(shapes.size()));
                        repaint();
                    }
                }
            }
        });
        undoBtn.setBounds(46, 545, 85, 21);
        panel.add(undoBtn);

//		JButton clearBtn = new JButton("clear");
        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undoAfterClear = true;
                state.clearedStateClear();
                shapes.clear();
                undoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCurrent()));
                redoBtn.setEnabled(Helper.isBtnEnabled(state.getSizeOfCleared()));
                clearBtn.setEnabled(Helper.isBtnEnabled(shapes.size()));
                repaint();
            }
        });
        clearBtn.setFont(new Font("Tahoma", Font.PLAIN, 15));
        clearBtn.setBounds(46, 607, 85, 21);
        panel.add(clearBtn);

        JRadioButton lineBtn = new JRadioButton("");
        JRadioButton circleBtn = new JRadioButton("");
        JRadioButton ovalBtn = new JRadioButton("");
        JRadioButton recBtn = new JRadioButton("");
        JRadioButton squareBtn = new JRadioButton("");
        JRadioButton trigBtn = new JRadioButton("");
        ovalBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ovalBtn.isSelected()) {
                    selectedShape = "oval";
                    squareBtn.setSelected(false);
                    recBtn.setSelected(false);
                    circleBtn.setSelected(false);
                    lineBtn.setSelected(false);
                    trigBtn.setSelected(false);
                }
            }
        });
        circleBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (circleBtn.isSelected()) {
                    selectedShape = "circ";
                    squareBtn.setSelected(false);
                    recBtn.setSelected(false);
                    ovalBtn.setSelected(false);
                    lineBtn.setSelected(false);
                    trigBtn.setSelected(false);
                }
            }
        });

        lineBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (lineBtn.isSelected()) {
                    selectedShape = "Line";
                    squareBtn.setSelected(false);
                    recBtn.setSelected(false);
                    ovalBtn.setSelected(false);
                    circleBtn.setSelected(false);
                    trigBtn.setSelected(false);
                }
            }
        });
        lineBtn.setBounds(6, 245, 70, 70);
        lineBtn.setHorizontalAlignment(SwingConstants.CENTER);
        lineBtn.setBackground(Color.LIGHT_GRAY);
        panel.add(lineBtn);
//		lineBtn.setIcon(new ImageIcon(this.getClass().getResource("line.gif")));

        // ---------------------------------- circle button
        // ----------------------------------
        circleBtn.setBounds(6, 317, 70, 70);
        circleBtn.setHorizontalAlignment(SwingConstants.CENTER);
        circleBtn.setBackground(Color.LIGHT_GRAY);
        panel.add(circleBtn);
//		lineBtn.setIcon(new ImageIcon(this.getClass().getResource("/tenor.gif")));

        ovalBtn.setBounds(87, 317, 70, 70);
        ovalBtn.setHorizontalAlignment(SwingConstants.CENTER);
        ovalBtn.setBackground(Color.LIGHT_GRAY);
        panel.add(ovalBtn);
//		lineBtn.setIcon(new ImageIcon(this.getClass().getResource("/tenor.gif")));

        recBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (recBtn.isSelected()) {
                    selectedShape = "Rect";
                    squareBtn.setSelected(false);
                    ovalBtn.setSelected(false);
                    circleBtn.setSelected(false);
                    lineBtn.setSelected(false);
                    trigBtn.setSelected(false);
                }
            }
        });
        recBtn.setBounds(87, 245, 70, 70);
        recBtn.setHorizontalAlignment(SwingConstants.CENTER);
        recBtn.setBackground(Color.LIGHT_GRAY);
        panel.add(recBtn);
//		recBtn.setIcon(new ImageIcon(this.getClass().getResource("rect.gif")));

        JPanel colordisplay = new JPanel();
        colordisplay.setBackground(color);
        colordisplay.setBounds(6, 10, 151, 95);
        panel.add(colordisplay);

        JButton btnNewButton_1 = new JButton("colors");
        btnNewButton_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color prevColor = color;
                color = JColorChooser.showDialog(null, "Pick a color", Color.BLACK);
                try {
                    System.out.println(color.toString());
                } catch (Exception ex) {
                    color = prevColor;
                    System.out.println("exception");
                }
                colordisplay.setBackground(color);
            }
        });
        btnNewButton_1.setFont(new Font("Traditional Arabic", Font.PLAIN, 15));
        btnNewButton_1.setBounds(6, 115, 85, 21);
        panel.add(btnNewButton_1);

        squareBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (squareBtn.isSelected()) {
                    selectedShape = "squ";
                    recBtn.setSelected(false);
                    ovalBtn.setSelected(false);
                    circleBtn.setSelected(false);
                    lineBtn.setSelected(false);
                    trigBtn.setSelected(false);
                }

            }
        });
        squareBtn.setHorizontalAlignment(SwingConstants.CENTER);
        squareBtn.setBackground(Color.LIGHT_GRAY);
        squareBtn.setBounds(6, 389, 70, 70);
        panel.add(squareBtn);

        JLabel lblNewLabel_1 = new JLabel("size:");
        lblNewLabel_1.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        lblNewLabel_1.setBounds(16, 154, 30, 21);
        panel.add(lblNewLabel_1);

        trigBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (trigBtn.isSelected()) {
                    selectedShape = "trig";
                    recBtn.setSelected(false);
                    ovalBtn.setSelected(false);
                    circleBtn.setSelected(false);
                    lineBtn.setSelected(false);
                    squareBtn.setSelected(false);
                }
            }
        });
        trigBtn.setHorizontalAlignment(SwingConstants.CENTER);
        trigBtn.setBackground(Color.LIGHT_GRAY);
        trigBtn.setBounds(87, 389, 70, 70);
        panel.add(trigBtn);

        JComboBox comboBox = new JComboBox();
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                size = comboBox.getSelectedIndex();
            }
        });
        comboBox.setModel(new DefaultComboBoxModel(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"}));
        comboBox.setBounds(56, 155, 50, 21);
        panel.add(comboBox);

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
                                repaint();
                            }
                            else
                            {
                                System.out.println("redo t != null --->" + shapes.size());
                                repaint();
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
                                    repaint();
                                }
                                else
                                {
                                    shapes.clear();
                                    repaint();
                                }
                            }
                            else
                            {
                                System.out.println("||||||||||||||||||||||||||||||||||||||||||");
                                ArrayList<Shape> t = Helper.getCopyOfCurrnetState(state.undoState());
                                if (t != null) {
                                    shapes = t;
                                    repaint();
                                } else {
                                    shapes.clear();
                                    repaint();
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
				repaint();
			}
		});
		clearBtn.setFont(new Font("Tahoma", Font.PLAIN, 15));
		clearBtn.setBounds(46, 607, 85, 21);
		panel.add(clearBtn);*/
        JCheckBox moveCheckBox = new JCheckBox("Move");
        JCheckBox fillCheckBox = new JCheckBox("Fill");
        JCheckBox deleteCheckBox = new JCheckBox("Delete");
        JCheckBox copyCheckBox = new JCheckBox("Copy");
        JCheckBox resizeCheckBox = new JCheckBox("Resize");
        JCheckBox drawFilledCheckBox = new JCheckBox("DrawFilled");

        moveCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (moveCheckBox.isSelected()) {
                    moveChecked = true;
                    fillChecked = false;
                    deleteChecked = false;
                    copyChecked = false;
                    resizeChecked = false;
                    drawFilledChecked = false;
                    drawFilledCheckBox.setSelected(false);
                    resizeCheckBox.setSelected(false);
                    fillCheckBox.setSelected(false);
                    deleteCheckBox.setSelected(false);
                    copyCheckBox.setSelected(false);
                } else {
                    moveChecked = false;
                }
            }
        });

        moveCheckBox.setBounds(46, 460/*514*/, 85, 21);
        panel.add(moveCheckBox);

        fillCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fillCheckBox.isSelected()) {
                    fillChecked = true;
                    moveChecked = false;
                    deleteChecked = false;
                    copyChecked = false;
                    resizeChecked = false;
                    drawFilledChecked = false;
                    drawFilledCheckBox.setSelected(false);
                    resizeCheckBox.setSelected(false);
                    moveCheckBox.setSelected(false);
                    deleteCheckBox.setSelected(false);
                    copyCheckBox.setSelected(false);
                } else {
                    fillChecked = false;
                }
            }
        });
        fillCheckBox.setBounds(46, 485/*514*/, 85, 21);
        panel.add(fillCheckBox);

        deleteCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (deleteCheckBox.isSelected()) {
                    deleteChecked = true;
                    moveChecked = false;
                    fillChecked = false;
                    copyChecked = false;
                    resizeChecked = false;
                    drawFilledChecked = false;
                    drawFilledCheckBox.setSelected(false);
                    resizeCheckBox.setSelected(false);
                    moveCheckBox.setSelected(false);
                    fillCheckBox.setSelected(false);
                    copyCheckBox.setSelected(false);
                } else {
                    deleteChecked = false;
                }
            }
        });
        deleteCheckBox.setBounds(46, 510/*514*/, 85, 21);
        panel.add(deleteCheckBox);

        copyCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (copyCheckBox.isSelected()) {
                    copyChecked = true;
                    deleteChecked = false;
                    moveChecked = false;
                    fillChecked = false;
                    resizeChecked = false;
                    drawFilledChecked = false;
                    drawFilledCheckBox.setSelected(false);
                    resizeCheckBox.setSelected(false);
                    moveCheckBox.setSelected(false);
                    fillCheckBox.setSelected(false);
                    deleteCheckBox.setSelected(false);
                } else {
                    copyChecked = false;
                }
            }
        });
        copyCheckBox.setBounds(46, 630/*514*/, 85, 21);
        panel.add(copyCheckBox);

        resizeCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (resizeCheckBox.isSelected()) {
                    resizeChecked = true;
                    copyChecked = false;
                    deleteChecked = false;
                    moveChecked = false;
                    fillChecked = false;
                    drawFilledChecked = false;
                    drawFilledCheckBox.setSelected(false);
                    moveCheckBox.setSelected(false);
                    fillCheckBox.setSelected(false);
                    deleteCheckBox.setSelected(false);
                    copyCheckBox.setSelected(false);
                } else {
                    resizeChecked = false;
                }
            }
        });
        resizeCheckBox.setBounds(46, 660/*514*/, 85, 21);
        panel.add(resizeCheckBox);

        drawFilledCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (drawFilledCheckBox.isSelected()) {
                    drawFilledChecked = true;
                    resizeChecked = false;
                    copyChecked = false;
                    deleteChecked = false;
                    moveChecked = false;
                    fillChecked = false;
                    moveCheckBox.setSelected(false);
                    fillCheckBox.setSelected(false);
                    deleteCheckBox.setSelected(false);
                    copyCheckBox.setSelected(false);
                    resizeCheckBox.setSelected(false);
                } else {
                    drawFilledChecked = false;
                }
            }
        });
        drawFilledCheckBox.setBounds(46, 220/*514*/, 85, 21);
        panel.add(drawFilledCheckBox);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBounds(0, 0, 1262, 22);
        add(menuBar);

        JMenu mnNewMenu = new JMenu("File");
        menuBar.add(mnNewMenu);

        JMenuItem mntmNewMenuItem = new JMenuItem("New Project");
        mnNewMenu.add(mntmNewMenuItem);

        JMenuItem mntmNewMenuItem_1 = new JMenuItem("Save");
        mnNewMenu.add(mntmNewMenuItem_1);

        JMenu mnNewMenu_2 = new JMenu("View");
        menuBar.add(mnNewMenu_2);

        JLabel lblNewLabel = new JLabel("View Mode");
        mnNewMenu_2.add(lblNewLabel);

        JCheckBoxMenuItem chckbxmntmNewCheckItem = new JCheckBoxMenuItem("Dark");
        mnNewMenu_2.add(chckbxmntmNewCheckItem);

        JCheckBoxMenuItem chckbxmntmNewCheckItem_1 = new JCheckBoxMenuItem("Light");
        mnNewMenu_2.add(chckbxmntmNewCheckItem_1);

        JMenu mnNewMenu_1 = new JMenu("Help");
        menuBar.add(mnNewMenu_1);

        JMenuItem mntmNewMenuItem_2 = new JMenuItem("About");
        mnNewMenu_1.add(mntmNewMenuItem_2);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Shape shape : shapes) {
            if (moveChecked || fillChecked) {
                shape.edit(g, "");
            } else {
                if (shape.isIsEdited()) {
                    shape.edit(g, "");
                } else {
                    shape.draw(g);
                }

            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        selectedShape = e.getActionCommand();
        System.out.printf("%s \n", selectedShape);
    }
}
