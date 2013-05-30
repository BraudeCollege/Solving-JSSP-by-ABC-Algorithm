/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jsspwabc;

/**
 *
 * @author vikash
 */

import java.util.Scanner;

public class JSSPwABC {

    /**
     * @param args the command line arguments
     */
    
    static ABC abc = new ABC();
    static GA ga = new GA();
    
    public static void main(String[] args) {
        // TODO code application logic here
        
        int iter = 0;
        int run = 0;
        int j = 0;
        double mean = 0;
        int bestSeq[][] = new int[abc.runtime][abc.D];
        
        for( run=0; run<abc.runtime; run++ ) {
            abc.initial();
            abc.MemorizeBestSource();
            
            for( iter=0; iter<abc.maxCycle; iter++ ) {
                abc.SendEmployedBees();
                abc.CalculateProbabilities();
                abc.SendOnlookerBees();
                abc.MemorizeBestSource();
                abc.SendScoutBees();
            }
                        
            //abc.VNS_Procedure();
            
            for( j=0; j<abc.D; j++ ) {
                bestSeq[run][j] = abc.GlobalParams[j];
                System.out.print(abc.GlobalParams[j] + " ");
            }
            
            System.out.println();            
            System.out.println("minMakespan = " + abc.GlobalMin);
            
            //abc.VNS_Procedure();
            //System.out.println("minMakespan = " + abc.GlobalMin);
            
            abc.GlobalMins[run] = abc.GlobalMin;
            mean = mean + abc.GlobalMin;
            
        }
        
        int best = abc.GlobalMin;
        int bt = run-1;
        
        for( run=0; run<abc.runtime; run++ ) {
            if( best>abc.GlobalMins[run] ) {
                best = abc.GlobalMins[run];
                bt = run;
            }
        }
        
        System.out.println();
        System.out.println("Best operation scheduling list : ");
        
        for( j=0; j<abc.D; j++ ) {
            System.out.print(bestSeq[bt][j] + " ");
        }
        
        System.out.println();
        System.out.println(abc.CalculateFunction(bestSeq[bt]));
        
        System.out.println();
        System.out.println();
        System.out.println("MakeSpanTime in Best case = " + best); 
        
        mean /= abc.runtime;
        System.out.println();
        System.out.println("Means of " + abc.runtime + "runs : " + mean);
        
        
        
        JSSP jssp = new JSSP(bestSeq[bt],abc.noj,abc.nom);
        
        GanttChart gc = new GanttChart();
        gc.drawGraph(jssp.jmS, bestSeq[bt], jssp.startT, jssp.endT, abc.noj, abc.nom);
        
        Scanner in = new Scanner(System.in);
        
        System.out.println("\nDo u want run GA also(press 'y' or 'n')? " );
        
        String s;
        s = in.next();
        
        if( s.equalsIgnoreCase("y")) {
                    
            int pre = 3;
            
            for( run=0; run<30; run++ ) {
                ga.initial();
                ga.MemorizeBestSource();
                
                for( iter=0; iter<200; iter++ ) {
                    //System.out.println("iter " + iter);
                    //ga.initial();
                    
                    ga.selectParent();
                    if( ga.DG<pre ) ga.mutation();
                    else ga.crossover();
                   
                    ga.neighbourSearch();
                    ga.MemorizeBestSource();
                }
                
                for( j=0; j<abc.D; j++ ) {
                    bestSeq[run][j] = ga.GlobalParams[j];
                    System.out.print(ga.GlobalParams[j] + " ");
                }
            
                System.out.println();            
                System.out.println("minMakespan = " + ga.GlobalMin);
            
                ga.GlobalMins[run] = ga.GlobalMin;
                mean = mean + ga.GlobalMin;
            
        }
        
        best = ga.GlobalMin;
        bt = run-1;
        
        for( run=0; run<30; run++ ) {
            if( best>ga.GlobalMins[run] ) {
                best = ga.GlobalMins[run];
                bt = run;
            }
        }
        
        System.out.println();
        System.out.println("Best operation scheduling list : ");
        
        for( j=0; j<abc.D; j++ ) {
            System.out.print(bestSeq[bt][j] + " ");
        }
        
        System.out.println();
        System.out.println(abc.CalculateFunction(bestSeq[bt]));
        
        System.out.println();
        System.out.println();
        System.out.println("MakeSpanTime in Best case = " + best); 
        
        mean /= abc.runtime;
        System.out.println();
        System.out.println("Means of " + abc.runtime + "runs : " + mean);
        
        jssp = new JSSP(bestSeq[bt],abc.noj,abc.nom);
        
        gc = new GanttChart();
        gc.drawGraph(jssp.jmS, bestSeq[bt], jssp.startT, jssp.endT, abc.noj, abc.nom);
        
                
        }
        
        else {
            System.out.println("Thanks for your patience");
        }
        
    }
  
}
