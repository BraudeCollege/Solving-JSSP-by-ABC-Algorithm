/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jsspwabc;

/**
 *
 * @author vikash
 */

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import javax.swing.*;

public class GanttChart extends JPanel{
    
    static int[][] startT;         //Start time of each job on each machine
    static int[][] endT;           //End time of each job on each machine
    static int[][] jmS;            //Job machine sequence matrix
    static int[] seq;              //Operation scheduling list
    static int[] jom;              //Job on machine
    static int[][] jN;             //Job name
    static int[] jp;               //Jop working procedure
    static int nj;                 //No. of job
    static int nm;                 //No. of machine
    static int d;            
    final int PAD = 50 ;
 
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth();
        int h = getHeight();
        int i,j;
        
        //Making background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, w, h);
        
        g.setColor(Color.WHITE);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw ordinate.
        g2.draw(new Line2D.Double(PAD, PAD, PAD, h-PAD));
        
        // Draw abcissa.
        g2.draw(new Line2D.Double(PAD, h-PAD, w, h-PAD));
        
        // Draw labels.
        Font font = g2.getFont();
        FontRenderContext frc = g2.getFontRenderContext();
        LineMetrics lm = font.getLineMetrics("0", frc);
        float sh = lm.getAscent() + lm.getDescent();
        
        // Ordinate label.
        String s = "Machines";
        float sy = PAD + ((h - 2*PAD) - s.length()*sh)/2 + lm.getAscent();
        for( i=0; i<s.length(); i++ ) {
            String letter = String.valueOf(s.charAt(i));
            float sw = (float)font.getStringBounds(letter, frc).getWidth();
            float sx = (PAD - sw)/2 - 20;
            g2.drawString(letter, sx, sy);
            sy += sh;
        }
        
        // Abcissa label.
        s = " Time ";
        sy = h - PAD + (PAD - sh)/2 + lm.getAscent() + 18;
        float sw = (float)font.getStringBounds(s, frc).getWidth();
        float sx = (w - sw)/2;
        g2.drawString(s, sx, sy);
        
        double xInc = (double)(w - 2*PAD)/getMax();
        double scale = (double)(h - 5*PAD)/nm;
        //int nop[] = new int[nj];
        
        for( i=0; i<nm; i++ ) {
            s = "M" + (i+1);
            float x = (PAD-sw)/2 + 20;
            float y = (float)(h - PAD - scale*( (i) + 5 ) + 8);
            g2.drawString(s, x, y);
        }
        
        // Draw lines.
        
        
        //System.out.println("line " + d);
        
        Font f;
        
        for( i=0; i<nm; i++ ) {
            for( j=0; j<nj; j++ ) {
                g2.setPaint(Color.green);
                /*double x1 = PAD + xInc*startT[jmS[seq[j]][nop[seq[j]]]][seq[j]];
                double y1 = h - PAD - scale*( jmS[seq[j]][nop[seq[j]]] + 5 );
                double x2 = PAD + xInc*endT[jmS[seq[j]][nop[seq[j]]]][seq[j]];
                double y2 = y1;
                
                 * 
                 */
                double x1 = PAD + xInc*startT[i][j];
                double y1 = h - PAD - scale*( (i) + 5 ) + 6;
                double x2 = PAD + xInc*endT[i][j];
                double y2 = y1;


                //System.out.println(x1+ " "+ y1 + " " + x2 + " " +y2);

                g2.draw(new Line2D.Double(x1, y1, x2, y2));

                //jN[jmS[seq[j]][nop[seq[j]]]][jom[jmS[seq[i]][nop[seq[i]]]]] = seq[j];
                
                f = new Font("serif",Font.LAYOUT_LEFT_TO_RIGHT, 13);	
                g2.setFont(f);
                
                g2.setPaint(Color.white);
                s = "J" + (jN[i][j]+1);

                //System.out.println(seq[j] + " " +jmS[seq[j]][nop[seq[j]]] + " " + startT[jmS[seq[j]][nop[seq[j]]]][seq[j]]);
                
                
                float x = (float)(x2+x1)/2;
                sy = (float)y2;
                g2.drawString(s, x, sy);

                sy = h - PAD + (PAD - sh)/2 + lm.getAscent() - 12;
                //s = "" + endT[jmS[seq[j]][nop[seq[j]]]][seq[j]];
                s = "" + endT[i][j];
                
                f = new Font("serif",Font.LAYOUT_LEFT_TO_RIGHT, 8);	
                g2.setFont(f);
                sw = (float)font.getStringBounds(s, frc).getWidth();
                x = (float)x2 - (sw)/2;
                g2.drawString(s, x, sy);

                g2.setPaint(Color.red);
                g2.fill(new Ellipse2D.Double(x1-1, y1-1, 4, 3));
                g2.fill(new Ellipse2D.Double(x2-1, y2-1, 4, 4));

                //jom[jmS[seq[i]][nop[seq[i]]]]++;
                //nop[seq[j]]++;
            }
            
        }
        
          
    }

    private int getMax() {
        int max = -Integer.MAX_VALUE;
        int i,j;
        for( i=0; i<nm; i++ ) {
            for( j=0; j<nj; j++ )
                if(endT[i][j] > max)
                    max = endT[i][j];
        }
        return max;
    }

    public void drawGraph(int jms[][], int bstS[], int st[][], int en[][], int noj, int nom )
    {
        int i,j;
        startT = new int[nom][noj];
        endT = new int[nom][noj];
        jmS = new int[noj][nom];
        nj = noj;
        nm = nom;
        
        //System.out.println("line " + nm + " " + nj);
        
        for( i=0; i<nom; i++ ) {
            for( j=0; j<noj; j++ ) {
                startT[i][j] =  st[i][j];
                endT[i][j] = en[i][j];
                
                
                //System.out.println(startT[i][j] + " " + endT[i][j] + " " + jmS[i][j]);
                
            }
            
        }
        
        for( i=0; i<noj; i++ ) 
            for( j=0; j<nom; j++ )
                jmS[i][j] = jms[i][j];
        
        d = nom*noj;
        
        //System.out.println("d " + d);
        
        seq = new int[d];
        jN = new int[nm][nj];
        jp = new int[nj];
        jom = new int[nm];
        
        for( i=0; i<d; i++ ) {
            seq[i] = bstS[i];
         //   System.out.println(ar[i] + " ");
            jN[jmS[seq[i]][jp[seq[i]]]][jom[jmS[seq[i]][jp[seq[i]]]]] = seq[i];
            jom[jmS[seq[i]][jp[seq[i]]]]++;
            jp[seq[i]]++;
        }

        JFrame f = new JFrame();
        //f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new GanttChart());
 
        f.setSize(1200,500);
        f.setLocation(0,240);
        f.setVisible(true);
    }
}