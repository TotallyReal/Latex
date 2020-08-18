/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package latex.overbrace;

import javafx.scene.Group;
import latex.Formula;

/**
 *
 * @author eofir
 */
public class OverbraceUP extends Group {

  public Formula topFormula;
  private Overbrace overTop;
  private double len;

  public OverbraceUP(double x, double y, double len, Formula topFormula) {
    this.len = len;
    overTop = new Overbrace(0, 0, len, 0);
    if (topFormula != null) {
      this.topFormula = topFormula;
    } else {
      this.topFormula = new Formula(50, " ");
    }

    this.topFormula.centerAt(len / 2,
            -(overTop.getOverbraceHeight() + this.topFormula.getHeight() / 2));
    this.topFormula.setRect(true);
    super.getChildren().addAll(overTop, topFormula);
    super.setLayoutX(x);
    super.setLayoutY(y);

  }

  public double getOverbraceHeight() {
    return overTop.getOverbraceHeight();
  }

  public void setOverbraceHeight(double height) {
    overTop.setOverbraceHeight(height);
    
    this.topFormula.centerAt(len / 2,
            -(overTop.getOverbraceHeight() + this.topFormula.getHeight() / 2));

  }

}
