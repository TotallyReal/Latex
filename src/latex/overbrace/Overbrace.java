/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package latex.overbrace;

import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import latex.Formula;

/**
 *
 * @author eofir
 */
public class Overbrace extends Formula{
  
  private double d;
  private Scale scale;
  
  public Overbrace(double x1, double y1, double x2, double y2){
    super("",50,true);
    //super(50,"");
    d = Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
    String formString ="\\overbrace{";
    for (int i = 0; i < d; i+=20) {
      formString+="\\; ";      
    }
    formString+="}";
    super.setFormula(formString);
    /*Formula formula = new Formula(
            "\\overbrace{\\; \\; \\; \\; \\; \\; \\; \\; \\; \\; \\; \\; \\; \\;}",
    50, true);*/
    double width = super.getWidth();
    scale = new Scale(d/width,0.5* d/width,0,0);
    Rotate rotate = new Rotate(Math.atan2(y2-y1, x2-x1)*180/Math.PI);
    getTransforms().addAll(rotate, scale);
    super.setLayoutX(x1);
    super.setLayoutY(y1);
  }
  
  public void setOverbraceHeight(double height){
    scale.setY(1);
    scale.setY(height/getHeight());
  }
  
  public double getOverbraceHeight(){
    return super.getHeight()*scale.getY();
  }

  public double getOverbraceWidth() {
    return super.getWidth()*scale.getX();
  }
  
}
