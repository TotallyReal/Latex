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
public class OverbraceRight extends Group{
  
  Overbrace right;
  Formula formula;
  double len;
  
  public OverbraceRight(double x, double y, double len, Formula formula){
    this.len = len;
    right = new Overbrace(0, 0, 0, len);
    if (formula != null) {
      this.formula = formula;
    } else {
      this.formula = new Formula(50, " ");
    }

    this.formula.centerAt(formula.getWidth()/2+right.getOverbraceHeight(),
              right.getOverbraceWidth()/2);
    this.formula.setRect(true);
    super.getChildren().addAll(right, formula);
    super.setLayoutX(x);
    super.setLayoutY(y);
    
  }
  
}
