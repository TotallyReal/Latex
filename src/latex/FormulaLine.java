/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package latex;

import staticmethods.MFX;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;
//import presentation.Slide;

/**
 *
 * @author eofir
 */
public class FormulaLine extends Group implements FormulaType{

    private List<Formula> frms;
    Formula[] frm;
    int N;
    private double width;
    private double height;
    private double defSize;
    private Color defColor;
    private double padRatio = 0.37;
    private String formulaText = null;

    
    /**
     * Creates a new FormulaLine with the given default font size .
     * 
     * @param fontSize The default font size.
     */
    public FormulaLine(double fontSize){
        defSize = Math.max(fontSize,2);
        frms = new ArrayList<>(5);
        width = 0;
        height = 0;
    }

    /**
     * Creates a new FormulaLine with the given default font size and the given 
     * formulas added one after the other.
     * 
     * @param fontSize The default font size.
     * @param formulas The initial formulas
     */
    public FormulaLine(double fontSize, String... formulas) {
        this(fontSize);
        if (formulas == null || formulas.length==0)
            return;
        
        formulaText = String.join("", formulas);
        N = formulas.length;
        //this.size = size;
        frms = new ArrayList<>(5);
        frm = new Formula[formulas.length];
        ObservableList<Node> children = this.getChildren();
        width = 0;
        height = 0;
        for (int i = 0; i < formulas.length; i++) {
            frm[i] = new Formula(fontSize, formulas[i]);
            frm[i].setLayoutX(width);

            width += frm[i].getWidth();
            width -= (fontSize * padRatio);
            if (frm[i].getHeight() > height) {
                height = frm[i].getHeight();
            }
        }
        children.addAll(frm);

    }
    
    /**
     * Add another formula with the given string to the end of this line.
     * 
     * The size and color of the formula are the default font size and color
     * of this formula line.
     * 
     * @param formula 
     * @return  
     */
    public Formula addFormula(String formula){
        if (formula==null)
            return null;
        Formula frm = new Formula(defSize, formula);
        
        return frm;
    }
    
    public void addFormula(Formula formula){
        
    }

    public FormulaLine(String[] formulas, double[] sizeArr, boolean showHere) {
        N = formulas.length;
        if (sizeArr.length < N) {
            sizeArr = new double[N];
            for (double temp : sizeArr) {
                temp = 100;
            }
        }
        this.defSize = 100;
        frm = new Formula[formulas.length];
        ObservableList<Node> children = this.getChildren();
        width = 0;
        height = 0;
        for (int i = 0; i < formulas.length; i++) {
            frm[i] = new Formula(formulas[i], sizeArr[i], showHere);
            frm[i].setLayoutX(width);
            width += frm[i].getWidth();
            width -= (sizeArr[i] * padRatio);
            if (frm[i].getHeight() > height) {
                height = frm[i].getHeight();
            }
            children.add(frm[i]);
        }
    }

    public FormulaLine(String[] formulas, double[] sizeArr) {
        this(formulas, sizeArr, false);
    }

    public FormulaLine(String string, int size) {
        this(size,toStringArray(string));
    }

    public int formulaCount(){
        return frms.size();
    }
    
    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    public Transition reverse(double seconds) {
        ParallelTransition pt = new ParallelTransition();
        ObservableList<Animation> kids = pt.getChildren();
        TranslateTransition tt[] = new TranslateTransition[N];
        Duration time = Duration.seconds(seconds);
        double absoluteX = 0;
        for (int i = 0; i < tt.length; i++) {
            tt[i] = new TranslateTransition(time, frm[N - 1 - i]);
            tt[i].setToX(absoluteX - frm[N - 1 - i].getLayoutX());
            absoluteX += frm[N - 1 - i].getWidth();
            absoluteX -= (defSize * padRatio);
            //tt[i].setToX(0);
            //tt[i].play();
            kids.add(tt[i]);
        }
        //pt.play();
        return pt;
    }

    @Override
    public double getRightX() {
        return this.getLayoutX() + this.getWidth();
    }

//    public FadeTransition fadeDigit(int index) {
//        return (new FadeTransition(SECOND, frm[index]));
//    }

    /**
     * Use setFormulaOpacity instead.
     * @param opacity 
     */
    public void setOpacityElements(double opacity) {
        setFormulaOpacity(opacity);
    }

    public Formula getFormulaAt(int index) {
        return frm[index];
    }

    public static String[] toStringArray(String s) {
        String arr[] = new String[s.length()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = s.charAt(i) + "";
        }
        return arr;
    }

    @Override
    public String getFormulaString() {
        return formulaText;
    }

    public int length() {
        return frm.length;
    }

    public Transition fadeInParallel(int... indices) {
        return fadeInParallel(MFX.SECOND_1, indices);
    }

    public Transition fadeInParallel(Duration time, int... indices) {
        //int DOWN_SIDE = staticmethods.MFX.DOWN_SIDE;
        return  MFX.fadeInParallel(getFormulasAt(indices));
//        ParallelTransition pt = new ParallelTransition();
//        ObservableList<Animation> kids = pt.getChildren();
//        FadeTransition fade;
//        for (int i = 0; i < indices.length; i++) {
//            fade = new FadeTransition(time, getFormulaAt(indices[i]));
//            fade.setFromValue(0);
//            fade.setToValue(1);
//            kids.add(fade);
//        }
//        return pt;
    }

    @Override
    public void setColor(Color color) {

        for (Formula frm1 : frm) {
            frm1.setColor(color);
        }
    }

    public double xCoord(int i) {
        return frm[i].getLayoutX();
    }

    public double yCoord(int i) {
        return frm[i].getLayoutY();
    }

    @Override
    public void writeAt(double x, double y) {
        setLayoutX(x);
        setLayoutY(y);
    }
    
    
    public void centerAt(double x, double y) {
        writeAt(x-getWidth()/2, y+getHeight()/2);        
    }

    public Formula[] getFormulasAt(int... indices) {
        if (indices ==null)
            return new Formula[]{};
        LinkedList<Node> list = new LinkedList();
        for (int i = 0; i < indices.length; i++) {
            if (valid(indices[i]))
                list.add(getFormulaAt(indices[i]));            
        }
        Formula nodes[] = new Formula[list.size()];
        return list.toArray(nodes);
    }
    
    public Formula[] getAllFormulas(){
        Formula nodes[] = new Formula[frm.length];
        System.arraycopy(frm, 0, nodes, 0, nodes.length);
        return nodes;
    }
    
    private boolean valid(int index){
        return (0<=index && index<frm.length);
    }
    
    @Override
    public void setFormulaOpacity(double opacity){
        for (Formula current : frm) {
            current.setOpacity(opacity);
        }
    }

    
    Line strike = null;

    public Line addStrikeThrough() {
        if (strike == null) {
            strike = new Line(0, -getHeight() / 2, getWidth(), -getHeight() / 2);
            getChildren().add(strike);
        }
        return strike;
    }

    public Line getStrikeThrough() {
        return strike;
    }
    
}
