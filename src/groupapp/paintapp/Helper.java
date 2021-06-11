/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groupapp.paintapp;

import java.util.ArrayList;

/**
 *
 * @author Ahmad Nouh
 */
public class Helper {

    public static ArrayList<Shape> getCopyOfCurrnetState(ArrayList<Shape> b) {
        ArrayList<Shape> n = new ArrayList<>();
        Shape t = null;
        if (b != null) {
            for (Shape s : b) {
                t = ShapeFactory.getCopyOfShape(s, t);
                n.add(t);

            }
            return n;
        } else {
            return null;
        }

    }

    public static int findTheIndex(int x, int y, ArrayList<Shape> shapes) {
        for (int i = 0; i < shapes.size(); i++) {
            if (shapes.get(i).contains(x, y)) {
                return i;
            }
        }
        return -1;
    }

    public static boolean isBtnEnabled(int size) {
        if (size > 0) {
            return true;
        } else {
            return false;
        }
    }

}
