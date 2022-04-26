/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.Shape;
import java.util.ArrayList;

/**
 *
 * @author Ahmad Nouh
 */
public class State {

    private ArrayList<ArrayList<Shape>> currentState = new ArrayList<>();
    private ArrayList<ArrayList<Shape>> clearedState = new ArrayList<>();

    public void addState(ArrayList<Shape> s) {
        System.out.println("add--->");
        currentState.add(s);
    }

    public ArrayList<Shape> redoState() {
        if (clearedState.size() > 1) {
            System.out.println("enter case 1 redo with " + currentState.size() + " " + clearedState.size());
            currentState.add(clearedState.get(clearedState.size() - 1));
            clearedState.remove(clearedState.get(clearedState.size() - 1));
            System.out.println("exit case 1 redo with " + currentState.size() + " " + clearedState.size());
            return currentState.get(currentState.size() - 1);
        } else if (clearedState.size() == 1) {
            System.out.println("enter case 2 redo with " + currentState.size() + " " + clearedState.size());
            currentState.add(clearedState.get(clearedState.size() - 1));
            clearedState.remove(clearedState.get(clearedState.size() - 1));
            System.out.println("exiting case 2 redo with " + currentState.size() + " " + clearedState.size());
            return currentState.get(currentState.size() - 1);
        } else {
            System.out.println("enter case 3 redo with  " + currentState.size() + " " + clearedState.size());
            System.out.println("exiting case 3 redo with \"null\" " + currentState.size() + " " + clearedState.size());
            return null;
        }
    }

    public ArrayList<Shape> undoState() {
        if (currentState.size() > 1) {
            System.out.println("enter case 1 undo with " + currentState.size() + " " + clearedState.size());
            clearedState.add(currentState.get(currentState.size() - 1));
            currentState.remove(currentState.get(currentState.size() - 1));
            System.out.println("exiting case 1 undo with " + currentState.size() + " " + clearedState.size());
            return currentState.get(currentState.size() - 1);
        } else if (currentState.size() == 1) {
            System.out.println("enter case 2 undo with " + currentState.size() + " " + clearedState.size());
            clearedState.add(currentState.get(currentState.size() - 1));
            currentState.remove(currentState.get(currentState.size() - 1));
            System.out.println("exiting case 2 undo with \"null\" " + currentState.size() + " " + clearedState.size());

            return null;
        } else {
            System.out.println("enter case 3 undo with  " + currentState.size() + " " + clearedState.size());
            System.out.println("exiting case 3 undo with \"null\" " + currentState.size() + " " + clearedState.size());
            return null;
        }
    }

    public void clearedStateClear() {
        this.clearedState.clear();
    }

    public void myStateClear() {
        this.currentState.clear();
    }

    public ArrayList<Shape> getLastElement() {
        if (currentState.size() > 0) {
            return currentState.get(currentState.size() - 1);
        } else {
            return null;
        }
    }

    public int getSizeOfCurrent() {
        return currentState.size();
    }

    public int getSizeOfCleared() {
        return clearedState.size();
    }

}
