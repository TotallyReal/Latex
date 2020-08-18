package latex;

import java.io.File;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import org.jfree.fx.FXGraphics2D;
import org.scilab.forge.jlatexmath.*;

/**
 *
 * @author eofir
 */
public class Formula extends Group implements FormulaType {

  static { //load fonts
    File folder = new File("C:\\Users\\eofir\\Downloads\\jlatexmath-master\\jlatexmath\\target\\classes\\org\\scilab\\forge\\jlatexmath\\fonts");
    String path = "/org/scilab/forge/jlatexmath/fonts/";
    loadDir(folder, path);
  }

  private static void loadDir(File folder, String path) {
    File[] listOfFiles = folder.listFiles();

    File temp;
    String name;

    for (int i = 0; i < listOfFiles.length; i++) {
      temp = listOfFiles[i];
      name = temp.getName();
      if (temp.isFile() && temp.getAbsolutePath().endsWith(".ttf")) {
        //System.out.println("File " + listOfFiles[i].getName());
        javafx.scene.text.Font.loadFont(Formula.class.getResourceAsStream(path + name), 1);
      } else if (listOfFiles[i].isDirectory()) {
        //System.out.println("Directory " + listOfFiles[i].getName());
        loadDir(temp, path + name + "/");
      }
    }
  }

  /**
   * *
   * Return width of a character
   *
   * @param size The size of a character for latex
   * @return
   */
  public static double charWidth(double size) {
    return 0.5 * size;
  }

  public static double pad(double size) {
    return 0.18 * size;
  }

  String formula;
  double size;
  FXGraphics2D g2;
  String poss[] = {"=", "-", "+", "*"};
  private TeXIcon icon;
  Canvas canvas;
  private ObjectProperty<Paint> colorProp = new SimpleObjectProperty<>(Color.BLACK);
  public static java.awt.Color defColor = java.awt.Color.BLACK;
  private java.awt.Color color = defColor;

  public Formula(double fontSize, String formula, Color formulaColor) {
    color = defColor;
    setColor(formulaColor);
    size = Math.max(fontSize, 2);
    setFormula(formula);
  }

  private boolean rect = false;

  public Formula(String formula, double size, boolean rect) {
    color = defColor;
    //super(500, 150);
    this.size = size;
    setFormula(formula);
    colorProp.addListener(evt -> setColor((Color) colorProp.get()));
    this.rect = rect;
//        this.formula = formula;
//        TeXFormula texFormula = new TeXFormula(formula);
//        icon = texFormula.createTeXIcon(TeXConstants.STYLE_DISPLAY, (float) size);
//
//        //this.setWidth(icon.getIconWidth() + 1);
//        int height = icon.getIconHeight();
//        canvas = new Canvas(icon.getIconWidth() + 1, height + 1);
//        //this.setHeight(height + 1);
//        g2 = new FXGraphics2D(canvas.getGraphicsContext2D()); ///????
//        icon.paintIcon(null, g2, 0, 0);
//        getChildren().add(canvas);
//        canvas.setTranslateY(-height + icon.getTrueIconDepth());
//        System.out.println("size=" + size + ", formula="+formula+", height="+ height+", width="+icon.getIconWidth());
  }

  public Formula(String formula, double size) {
    this(formula, size, false);
  }

  public Formula(double size, String formula) {
    this(formula, size, false);
  }

  final public void setFormula(String formula) {
    this.formula = formula;
    redraw();
  }

  private void redraw() {
    TeXFormula texFormula = new TeXFormula(formula);
    icon = texFormula.createTeXIcon(TeXConstants.STYLE_DISPLAY, (float) size);

    //this.setWidth(icon.getIconWidth() + 1);
    int height = icon.getIconHeight();
    if (canvas != null) {
      getChildren().remove(canvas);
    }
    canvas = new Canvas(icon.getIconWidth() + 1, height + 1);
    //this.setHeight(height + 1);
    g2 = new FXGraphics2D(canvas.getGraphicsContext2D()); ///????
    icon.setForeground(color);
    icon.paintIcon(null, g2, 0, 0);
    if (rect) {
      g2.drawRect(1, 1, icon.getIconWidth(), height);
    }
    getChildren().add(canvas);
    canvas.setTranslateY(-height + icon.getTrueIconDepth());

  }

  public void setRect(boolean rect) {
    this.rect = rect;
    redraw();
  }

  /**
   * Change javafx.scene.paint.Color to java.awt.Color.
   *
   * @param color The color in javafx.scene.paint.Color
   * @return The color in java.awt.Color
   */
  private static java.awt.Color toAWT(Color color) {
    return new java.awt.Color((float) color.getRed(),
            (float) color.getGreen(),
            (float) color.getBlue(),
            (float) color.getOpacity());
  }

  @Override
  public void setColor(Color formulaColor) {
    if (formulaColor == null) {
      return;
    }
    color = toAWT(formulaColor);
    icon.setForeground(color);
    icon.paintIcon(null, g2, 0, 0);
  }

  public Animation strokeTransition(Color color) {

    Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(1000), new KeyValue(colorProperty(), color))
    );

    return timeline;
  }

  public ObjectProperty<Paint> colorProperty() {
    return colorProp;
  }

  @Override
  public double getRightX() {
    return this.getLayoutX() + this.getWidth();
  }

  @Override
  public String getFormulaString() {
    return new String(formula);
  }

  public void belowMiddle(Formula frm) {
    double x = frm.getLayoutX() + frm.getWidth() / 2;
    setLayoutX(x - getWidth() / 2);
    setLayoutY(frm.getLayoutY() + frm.getHeight());
  }

  public static final int LEFT = 0;
  public static final int CENTER_X = 1;
  public static final int RIGHT = 2;

  public void align(int alignTo) {
    switch (alignTo) {
      case LEFT:
        setTranslateX(0);
        break;
      case CENTER_X:
        setTranslateX(-getWidth() / 2);
        break;
      case RIGHT:
        setTranslateX(-getWidth());
        break;
    }
  }

  public void leftOfCenter(Formula frm) {
    setLayoutX(frm.getLayoutX() - getWidth());
    setLayoutY(frm.getLayoutY() + (frm.getHeight() - getHeight()) / 2);
  }

  public void rightOfCenter(Formula frm) {
    setLayoutX(frm.getLayoutX() + frm.getWidth());
    setLayoutY(frm.getLayoutY() + (frm.getHeight() - getHeight()) / 2);
  }

  public void rightOfCenter(FormulaLine seq) {
    setLayoutX(seq.getLayoutX() + seq.getWidth());
    setLayoutY(seq.getLayoutY() + (seq.getHeight() - getHeight()) / 2);
  }

  public void centerAt(double x, double y) {
    writeAt(x - getWidth() / 2, y + getHeight() / 2 - icon.getTrueIconDepth());
  }

  public void printStats() {
    System.out.println("for " + formula + " : base line=" + icon.getBaseLine() + ", height=" + icon.getIconHeight()
            + ", depth = " + icon.getTrueIconDepth() + ", shift=" + icon.getBox().getShift());
    //System.out.println("insets = "+icon.getInsets().toString()+"\n-----------------");
  }

  public int getDepth() {
    return icon.getIconDepth();
  }

  @Override
  public double getWidth() {
    return canvas.getWidth();
    //return icon.getIconWidth();
  }

  @Override
  public double getHeight() {
    return canvas.getHeight();
//        return icon.getIconHeight();
  }

  @Override
  public void writeAt(double x, double y) {
    setLayoutX(x);
    setLayoutY(y);
  }

  @Override
  public void setFormulaOpacity(double opacity) {
    this.setOpacity(opacity);
  }

  Line strike = null;

  public Line addStrikeThrough() {
    if (strike == null) {
      strike = new Line(0, -getHeight() / 2, getWidth(), -getHeight() / 2);
    }
    return strike;
  }

  public Line getStrikeThrough() {
    return strike;
  }

}
