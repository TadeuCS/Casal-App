/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Util;

/**
 *
 * @author Tadeu
 */
/* FixedLengthDocument.java */  
  
import javax.swing.text.AttributeSet;  
import javax.swing.text.BadLocationException;  
  
public class IntegerDocument extends FixedLengthDocument {  
    public IntegerDocument(int maxlen) {  
        super(maxlen);  
    }  
  
    @Override  
    public void insertString(int offset, String str, AttributeSet attr)  
            throws BadLocationException {  
        if (str == null)  
            return;  
          
        try {  
            Integer.parseInt(str);  
        } catch (Exception e) {  
            return;  
        }  
          
        super.insertString(offset, str, attr);  
    }  
} 
