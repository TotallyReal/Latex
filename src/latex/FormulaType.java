/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package latex;

import javafx.scene.paint.Color;

/**
 *
 * @author eofir
 */
public interface FormulaType {
    
    public void setColor(Color color);
    
    
    public double getRightX();
    
    public String getFormulaString();
    
    public double getWidth();
    
    public double getHeight();
    
    public void writeAt(double x, double y);
    
    public void setFormulaOpacity(double opacity);
    
}
