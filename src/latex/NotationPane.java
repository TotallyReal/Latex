/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package latex;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;

/**
 *
 * @author eofir
 */
public class NotationPane extends Group {

    protected int size;
    protected double diff;
    private int line = 0;

    public NotationPane(int size) {
        if (size > 0) {
            this.size = size;
        } else {
            this.size = 30;
        }
        diff = size * 1.8;
    }

    /**
     * Add the formulas one after the other.
     *
     * After adding the formulas, it jumps to the next line. Returns the line
     * number where the formulas were written.
     *
     * @param frms
     * @return
     */
    public int addFormula(FormulaType... frms) {
        addFormula(line, frms);
        line++;
        return line - 1;
    }

    public void addFormula(int line, FormulaType... frms) {
        addFormula(line, 0, frms);
    }

    public void addFormula(int line, double startX, FormulaType... frms) {
        ObservableList<Node> kids = getChildren();
        if (frms == null) {
            return;
        }
        if (frms.length == 0) {
            return;
        }
        double x = startX;
        for (FormulaType frm : frms) {
            frm.writeAt(x, line * diff);
            if (frm instanceof Node) {
                kids.add((Node) frm);
            }
            x = frm.getRightX();
        }
    }

    public int addFormulaRight(FormulaType frm, double length) {
        ObservableList<Node> kids = getChildren();
        if (frm instanceof Node) {
            kids.add((Node) frm);
        }
        frm.writeAt(length - frm.getWidth(), line * diff);
        line++;
        return line;
    }

    public int addFormulaCentered(FormulaType frm, double length) {
        ObservableList<Node> kids = getChildren();
        if (frm instanceof Node) {
            kids.add((Node) frm);
        }
        frm.writeAt((length - frm.getWidth()) / 2, line * diff);
        line++;
        return line;
    }

    public int addAfter(FormulaType after, FormulaType... frms) {
        if (!validFormula(after)) {
            return -1;
        }
        int line = getLine(after);
        addFormula(line, after.getRightX(), frms);
        return line;
    }

    /**
     * Returns true if frm is a node formula which was added to the
     * NotationPane.
     *
     * @param frm
     * @return
     */
    public boolean validFormula(FormulaType frm) {
        if (!(frm instanceof Node)) {
            return false;
        }
        Node node = (Node) frm;
        if (node.getParent() != this) {
            return false;
        }
        return true;
    }

    /**
     * only pass valid formulas, PLEASE!
     *
     * @param frm
     * @return
     */
    public int getLine(FormulaType frm) {
        System.out.println("ONLY valid formulas");
        Node node = (Node) frm;
        return (int) (node.getLayoutY() / diff);
    }

    public int addFormulaStacked(FormulaType... frms) {
        NotationPane.this.addFormulaStacked(line, frms);
        line++;
        return line - 1;
    }

    public void addFormulaStacked(int line, FormulaType... frms) {
        ObservableList<Node> kids = getChildren();
        for (FormulaType frm : frms) {
            frm.writeAt(0, line * diff);
            if (frm instanceof Node) {
                kids.add((Node) frm);
            }
        }
    }

    public void skipLine() {
        line++;
    }

    public void setOpacityElements(double opacity) {
        if (opacity < 0 || opacity > 1) {
            return;
        }
        getChildren().forEach((node) -> {
            if (node instanceof FormulaType) {
                ((FormulaType) node).setFormulaOpacity(opacity);
            } else {
                node.setOpacity(opacity);
            }
        });
    }

    public void setColorFormulas(Color color) {
        if (color == null) {
            return;
        }
        getChildren().forEach((node) -> {
            if (node instanceof FormulaType) {
                ((FormulaType) node).setColor(color);
            }
        });
    }

    public double getDiff() {
        return diff;
    }
    
    public void setDiff(double diff){
        if (diff>=0){
            this.diff=diff;
        }
    }

    public void dropLine(FormulaType... frms) {
        for (FormulaType frm : frms) {
            if (frm instanceof Node) {
                Node node = ((Node) frm);
                node.setLayoutY(node.getLayoutY() + diff);
            }
        }
    }

    public double getLength() {
        return getBoundsInLocal().getWidth();
    }

    public void centerText() {
        ObservableList<Node> kids = getChildren();
        for (Node node : kids) {
            if (node instanceof FormulaType){
                FormulaType frm = (FormulaType) node;
                node.setLayoutX(-frm.getWidth()/2);
            }
        }
    }

}
