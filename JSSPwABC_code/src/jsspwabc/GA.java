/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jsspwabc;

/**
 *
 * @author vikash
 */
public class GA {
    ABC abc = new ABC();
    
    double pr;
    int r1;
    int r2;
    
    int par1[] = new int[abc.D];
    int par2[] = new int[abc.D];
    int sol1[] = new int[abc.D];
    int sol2[] = new int[abc.D];
    int child[] = new int[abc.D];
    
    int f[] = new int[abc.SN];
    int fitness[] = new int[abc.SN];
    int trial[] = new int[abc.SN];
    
    int DG;
    int fitnessPar1;
    int fitnessPar2;
    
    int repC = 200;         //No. of repetition of crossover process 
    int repM = 100;         //No. of repetition of mutation process
    
    // Parameter for memorizing the best source
    int GlobalParams[] = new int[abc.D];
    int GlobalMin;
    int GlobalMins[] = new int[30];
    
    int pre = (abc.nom*abc.noj)/2 - abc.noj;
    
    //Initialization of sources
    void initial() {
        
        //System.out.println(abc.D);
        abc.initial();
        //System.out.println(abc.D);
               
        int i;
        
        for( i=0; i<abc.SN; i++ ) {
            fitness[i] = abc.CalculateFunction(abc.Foods[i]);
        }
        
        GlobalMin = fitness[0];
        
        for( i=0; i<abc.D; i++ ) 
            GlobalParams[i] = abc.Foods[0][i];
    }
    
    //Selecting random 2 parents and apply on them mutation/crossover
    void selectParent() {
                
        pr = ((double) Math.random()*32767 / ((double)(32767)+(double)(1)) );
        r1 = (int)(pr*abc.SN);
        
        pr = ((double) Math.random()*32767 / ((double)(32767)+(double)(1)) );
        r2 = (int)(pr*abc.SN);
        
        while( r2==r1 ) {
                pr = ((double) Math.random()*32767 / ((double)(32767)+(double)(1)) );
                r2 = (int)(pr*abc.SN);
        }
        
        int i;
        
        //int inc[] = new int[10];
        
        for( i=0; i<abc.D; i++ ) {
            par1[i] = abc.Foods[r1][i];
            //inc[par1[i]]++;
        }
        
        for( i=0; i<abc.D; i++ ) {
            par2[i] = abc.Foods[r2][i];
            //inc[par2[i]]++;
        }
        
        //for( i=0;i<10;i++ )
          //  System.out.print(inc[i] + " ");
        //System.out.println();
        
        DG = DGdistance(par1,par2);
        fitnessPar1 = abc.CalculateFunction(par1);
        fitnessPar2 = abc.CalculateFunction(par2);
        
    }
    
    //Distance between two sources(operation sequence list)S1 & S2
    //DGdistance = no. of differences in the processing order of operations of S1 & S2
    int DGdistance(int x[], int y[]) {
        int i;
        int k = 0;
        
        for( i=0; i<abc.D; i++ ) 
            if( x[i]!=y[i] ) k++;
        
        return k;
    }
    
    //Crossover
    void crossover() {
        
        int ngh[][] = new int[abc.SN][abc.D];
        int j,i,k;
        int rep = 0;
        
        for( i=0; i<abc.D; i++ ) {
            sol1[i] = par1[i];
            sol2[i] = par1[i];
        }
        
        while( rep<repC ) {             //Repeat some given no. repC
            k=0;
            int fit1,fit2;
            
            fit1 = abc.CalculateFunction(sol1);
            
            for( i=0; i<abc.SN; i++ ) {
                
                if( DGdistance(sol1,abc.Foods[i])==1 ) {
                    for( j=0; j<abc.D; j++ ) {
                        ngh[k][j] = abc.Foods[i][j];
                        
                    }
                    k++;
                }
            }
                
            
            Distance Dis[] = new Distance[k];
            
            for( i=0; i<k; i++ ) {
                int d = DGdistance(ngh[i],par2);
                Dis[i] = new Distance(d,i);
            }
            
            qsort(Dis);   //Sort the distances in asceneding order
            
            //System.out.println(k);
            
            boolean flag = false;
            i = 0;
            
            while( !flag && k!=0 ) {
                fit2 = abc.CalculateFunction(ngh[Dis[i].ind]);
                
                if( fit2 <= fit1 ) {
                    for( j=0; j<abc.D; j++ )
                        sol1[j] = ngh[Dis[i].ind][j];
                    flag = true;
                }
                else {
                    pr = ((double) Math.random()*32767 / ((double)(32767)+(double)(1)) );
                    if( pr>=0.5 ) {
                        
                        for( j=0; j<abc.D; j++ )
                            sol1[j] = ngh[Dis[i].ind][j];
                        flag = true;
                    }
                    else {
                        i++;
                        i %= k;
                    }
                }
            }
            
            fit1 = abc.CalculateFunction(sol1);
            fit2 = abc.CalculateFunction(sol2);
            
            //int inc[] = new int[10];
            
            if( fit1<=fit2 ) {
                for( j=0; j<abc.D; j++ ) { 
                    sol2[j] = sol1[j];
                    //inc[sol2[j]]++;
                }
            }
            
            //for( j=0;j<10;j++)
              //  System.out.print(inc[j] + " ");
            //System.out.println();
            rep++;
        }
        
        //int inc[] = new int[10];
        
        for( j=0; j<abc.D; j++ ) {
            child[j] = sol2[j];
            //inc[child[j]]++;
          //  System.out.print(child[j] );
        }
        //System.out.println("cross = " + abc.CalculateFunction(child));
        //System.out.println();
        
        //for( i=0;i<10;i++ )
            //System.out.print(inc[i] + " ");
        //System.out.println();
    }
    
    //Mutation process
    void mutation() {
        
        int rep = 0;
        int k,i,j;
        int ngh[][] = new int[abc.SN][abc.D];
        
        for( i=0; i<abc.D; i++ ) { 
            sol1[i] = par1[i];
            sol2[i] = par1[i];
        }
        
        while( rep<repM ) {
            k=0;
            int fit1,fit2;
            
            fit1 = abc.CalculateFunction(sol1);
            
            for( i=0; i<abc.SN; i++ ) {
                if( DGdistance(sol1,abc.Foods[i])==1 ) {
                    for( j=0; j<abc.D; j++ )
                        ngh[k][j] = abc.Foods[i][j];
                    k++;
                }
            }
            
            Distance Dis1[] = new Distance[k];
                        
            for( i=0; i<k; i++ ) {
                int d = DGdistance(ngh[i],par1);
                Dis1[i] = new Distance(d,i);
                
            }
            
            qsort1(Dis1);        //Sort the distences in desceneding order
            
            /*Distance Dis[] = new Distance[k];    //for storing sorted value in descending order
            
            System.out.println(k);
            
            for( i=0; i<k; i++ ) {
                System.out.print(i + " ");
                Dis[i].dis = Dis1[i].dis;
                Dis[i].ind = Dis1[i].ind;
                //System.out.println("ind=" + Dis[j].ind);
            }
            */
            boolean flag = false;
            i = 0;
            
            while( !flag && k!=0 ) {
                fit2 = abc.CalculateFunction(ngh[Dis1[i].ind]);
                
                if( fit2 <= fit1 ) {
                    for( j=0; j<abc.D; j++ )
                        sol1[j] = ngh[Dis1[i].ind][j];
                    flag = true;
                }
                else {
                    pr = ((double) Math.random()*32767 / ((double)(32767)+(double)(1)) );
                    if( pr>=0.5 ) {
                        for( j=0; j<abc.D; j++ )
                            sol1[j] = ngh[Dis1[i].ind][j];
                        flag = true;
                    }
                    else {
                        i++;
                        i %= k;
                    }
                }
            }
            
            fit1 = abc.CalculateFunction(sol1);
            fit2 = abc.CalculateFunction(sol2);
            
            if( fit1<=fit2 ) 
                for( j=0; j<abc.D; j++ ) 
                    sol2[j] = sol1[j];
            
            rep++;
         
        }
                
        for( j=0; j<abc.D; j++ )
            child[j] = sol2[j];
        
        //System.out.println("mut = " + abc.CalculateFunction(child));
        //System.out.println();
    }
    
    void neighbourSearch() {
        fitnessPar1 = abc.CalculateFunction(child);
        
        int i,j;
        /*int k = 0;
        int fit = fitnessPar1;
        int fl = Integer.MAX_VALUE;
       
        for( i=0; i<abc.SN; i++ ) {
            int l = Math.abs( fitnessPar1-fitness[i] ); 
            if( l<fl )  {
                k = i;
                fl = l;
                fit = fitness[i];
            }
        }
        */
        /*if( fit>fitnessPar1 ) {
            k=0;
            fit = fitnessPar1;
        }
        */
        
        //int inc[] = new int[10];
        
        /*if( k!=0 && fit<=fitnessPar1) {
            for( j=0; j<abc.D; j++ ) {
                child[j] = abc.Foods[k][j];
                
                //System.out.print(child[j] );
            }
            //System.out.println();
            //System.out.println();
        }
        else
            fit = fitnessPar1;
        
        fl = Math.abs( fit-fitness[0] );
        k=0;
        
        for( i=1; i<abc.SN; i++ ) {
            int l = Math.abs( fit-fitness[i] ); 
            if( l>fl )  {
                k = i;
                fl = l;
            }
        }
        
        if( k!=0 && fit<=fitness[k]) {
           
            for( j=0; j<abc.D; j++ ) {
                abc.Foods[k][j] = child[j];
                fitness[k] = fit; 
            }
       
        }
        else {
            for( i=0; i<abc.SN; i++ ) {
                
                if( fit==fitness[i] ) {
                    for( j=0; j<abc.D; j++ ) {
                        abc.Foods[i][j] = child[j];
                    }
                    //break;
                }
            }
            
        }
         * 
         */
        
        for( i=0; i<abc.SN; i++ ) {
            if( fitness[i]>fitnessPar1 ) {
                for( j=0; j<abc.D; j++ )
                    abc.Foods[i][j] = child[j];
                fitness[i] = fitnessPar1;
                trial[i] = 0;
            }
            else {
                trial[i]++;
            }
            
        }
        
        int maxtrialindex = 0;
        
        for( i=1; i<abc.SN; i++ ) {
            if( trial[i]>trial[maxtrialindex] ) 
                maxtrialindex = i;
            if( trial[maxtrialindex]>=10 )
                abc.init(maxtrialindex);
        }
       
    }
    
    void MemorizeBestSource() {
        int i,j;
        
        for( i=0; i<abc.SN; i++ ) {
            if( fitness[i]<GlobalMin ) {
                GlobalMin = fitness[i];
                //System.out.println("memorize" + " " + i + " " + GlobalMin);
                
                for( j=0; j<abc.D; j++ ) {
                    GlobalParams[j] = abc.Foods[i][j];
                   // System.out.print(Foods[i][j] + " ");
                }
                
                //System.out.println();
                //System.out.println(CalculateFunction(Foods[i]));
            }
        }
    }
    
    
    static void qsort(Distance[] c,int start,int end){   
        if(end <= start) return;   
        Distance comp = c[start];   
        int i = start,j = end + 1;   
        for(;;){   
            do i++; while(i<end && c[i].dis<comp.dis);   
            do j--; while(j>start && c[j].dis>comp.dis);   
            if(j <= i)   break;   
            Distance tmp = c[i];   
            c[i] = c[j];   
            c[j] = tmp;   
        }   
        c[start] = c[j];   
        c[j] = comp;   
        qsort(c,start,j-1);   
        qsort(c,j+1,end);   
    }   
   
    //Sort the sequence in ascending order
    static void qsort(Distance[] c){   
        qsort(c,0,c.length-1);   
    }
    
    
    static void qsort1(Distance[] c,int start,int end){   
        if(end <= start) return;   
        Distance comp = c[start];   
        int i = start,j = end + 1;   
        for(;;){   
            do i++; while(i<end && c[i].dis>comp.dis);   
            do j--; while(j>start && c[j].dis<comp.dis);   
            if(j <= i)   break;   
            Distance tmp = c[i];   
            c[i] = c[j];   
            c[j] = tmp;   
        }   
        c[start] = c[j];   
        c[j] = comp;   
        qsort1(c,start,j-1);   
        qsort1(c,j+1,end);   
    }   
   
    //Sort the sequence in descending order
    static void qsort1(Distance[] c){   
        qsort1(c,0,c.length-1);   
    }   
   
}

class Distance {
    int dis,ind;
    
    Distance(int d, int i) {
        dis = d;
        ind = i;
    }
}