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
import java.awt.Component;  
import java.awt.Dimension;  
import java.awt.Graphics;  
import java.awt.event.WindowAdapter;  
import java.awt.event.WindowEvent;  
import java.awt.image.BufferedImage;  
import java.io.File;  
import java.io.IOException;  
import java.nio.file.Path;  
import java.nio.file.Paths;  
  
import javax.imageio.ImageIO;  
import javax.swing.JFrame;  
  
/** 
 * This class demonstrates how to load an Image from an external file 
 * http://docs.oracle.com/javase/tutorial/2d/images/loadimage.html 
 */  
public class LoadImageApp extends Component {  
            
    BufferedImage img;  
    Manager em= new Manager();
    public void paint(Graphics g) {  
        g.drawImage(img, 0, 0, null);  
    }  
  
    public LoadImageApp() {  
        
        try {  
             System.out.println(em.getDiretorio("cadastro.png"));
           img = ImageIO.read(new File(getClass().getResource("cadastro.png").toString()));  
             
       } catch (IOException e) {  
           e.printStackTrace();  
       }  
  
    }  
  
    public Dimension getPreferredSize() {  
        if (img == null) {  
             return new Dimension(100,100);  
        } else {  
           return new Dimension(img.getWidth(null), img.getHeight(null));  
       }  
    }  
  
    public static void main(String[] args) {  
  
        JFrame f = new JFrame("Load Image Sample");  
              
        f.addWindowListener(new WindowAdapter(){  
                public void windowClosing(WindowEvent e) {  
                    System.exit(0);  
                }  
            });  
  
        f.add(new LoadImageApp());  
        f.pack();  
        f.setVisible(true);  
    }  
} 
