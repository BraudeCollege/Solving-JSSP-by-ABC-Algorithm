/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jsspwabc;

/**
 *
 * @author vikash
 */
public class ABC {
    
    int NP = 50;                 //Size of colony
    int SN = NP/2;               // No. of food source
    int limit = 100;             // A food source which could not be improved through "limit" trials(that food source) is abandoned by its employed bee
    int maxCycle = 1000;         //No. of cycle for foraging
    
    int noj = 6;                // No. of job
    int nom = 6;                // No. of machine
    int D = noj*nom;            // No. of parameter in sequence
    
    int runtime = 30;
    
    int Foods[][] = new int[SN][D];
    int Food[][] = new int[SN][D];
    int f[] = new int[SN];              //hold makespantime for each food source
    double fitness[] = new double[SN];  //hold fitness value of each food source
    int trial[] = new int[SN];          //for improvement of food source
    double prob[] = new double[SN];     //probability of each food source
    int solution[] = new int[D];        //operation scheduling list
    int sol1[] = new int[D];
    
    int ObjValSol;               
    double FitnessSol;
    int neighbour;
    
    // Parameter for memorizing the best source
    int GlobalMin;
    int GlobalParams[] = new int[D];
    int GlobalMins[] = new int[runtime];
    
    int r1;
    int r2;
    double pr;
    
    
    // Fitness funtion
    double CalculateFitness(int fun) {
        double result;
        
        result = 1.0/fun;
        
        return result;
    }
    
    
    // Best food source is memorized
    void MemorizeBestSource() {
        int i,j;
        
        for( i=0; i<SN; i++ ) {
            if( f[i]<GlobalMin ) {
                GlobalMin = f[i];
                //System.out.println("memorize" + " " + i + " " + GlobalMin);
                
                for( j=0; j<D; j++ ) {
                    GlobalParams[j] = Foods[i][j];
                   // System.out.print(Foods[i][j] + " ");
                }
                
                //System.out.println();
                //System.out.println(CalculateFunction(Foods[i]));
            }
        }
    }
    
    void init(int index) {
        int i,j;
        int flag[] = new int[noj];
        
        for( j=0; j<D; j++) {
            pr = ((double) Math.random()*32767 / ((double)(32767)+(double)(1)) );   //Generate random in [0,1]
            r1 = (int)(pr*noj);
                
            while( flag[r1]>=nom ) {
                pr = ((double) Math.random()*32767 / ((double)(32767)+(double)(1)) );
                r1 = (int)(pr*noj);
            }
            
            flag[r1]++;
            Foods[index][j] = r1;
        }
       
        for( i=0; i<D; i++ ) {
            solution[i] = Foods[index][i];
            //System.out.print(solution[i] + " ");
        }
        
        //System.out.println();
        
        f[index] = CalculateFunction(solution);
        //System.out.println(f[index]);
        fitness[index] = CalculateFitness(f[index]);
        trial[index] = 0;
        
    }
   
    //Initialization of food source
    void initial() {
        int i,j;
        
        for( i=0; i<SN; i++ ) {
            init(i);
            /*
             * for( int j=0;j<D;j++)
                System.out.print(Foods[i][j] + " ");
            System.out.println();
             * 
             */
            for( j=0; j<D; j++ )
                Food[i][j] = Foods[i][j];
        }
        
        GlobalMin = f[0];
        
        for( i=0; i<D; i++ ) 
            GlobalParams[i] = Foods[0][i];
    }
    
    // Employed Bees Phase
    void SendEmployedBees() {
        int i,j;
        
        for( i=0; i<SN; i++ ) {
           
            int randSelecJ[] = new int[noj];
            boolean randSelecD[] = new boolean[D];
            int nj[] = new int[noj];
            
            //Arrays.fill(randSelecJ, 0);
            
            for( j=0; j<noj; j++) 
                nj[j] = nom;
            
            // Exchanging information with a neighbouring food source based on PBX Method
            for( j=0; j<D; j++ ) {
                pr = ((double) Math.random()*32767 / ((double)(32767)+(double)(1)) );
                if( pr>=0.5 ) {
                    randSelecJ[Food[i][j]]++;
                    randSelecD[j] = true;
                }
               
            }
            
            pr = ((double) Math.random()*32767 / ((double)(32767)+(double)(1)) );
            r1 = (int)(pr*SN);
            
            while( r1==i ) {
                pr = ((double) Math.random()*32767 / ((double)(32767)+(double)(1)) );
                r1 = (int)(pr*SN);
            }
            
            neighbour = r1;
            
            int k = 0;
            
            for( j=0; j<D; j++ ) {
                if( randSelecD[j] ) {
                    while( randSelecJ[Food[neighbour][k]]==0 ) 
                        k++;
                    randSelecJ[Food[neighbour][k]]--;
                    Food[i][j] = Food[neighbour][k];
                    k++;    
                }
            }
            
            for( j=0; j<D; j++ ) {
                solution[j] = Food[i][j];
            }
            
            ObjValSol = CalculateFunction(solution);
            FitnessSol = CalculateFitness(ObjValSol);
            
            //System.out.println(ObjValSol + " " + FitnessSol + " " + fitness[i]);
            
            if( FitnessSol>fitness[i] ) {
                trial[i] = 0;
                //System.out.println("Employe");
                for( j=0; j<D; j++ ) {
                    Foods[i][j] = solution[j];
                    //System.out.print(Foods[i][j] + " ");
                }
                    
                //System.out.println("\n" + i + " " + CalculateFunction(Foods[i]));
                
                f[i] = ObjValSol;
                fitness[i] = FitnessSol;
                //System.out.println(f[i]);
               
            }
            else {
                trial[i] += 1;
                for( j=0; j<D; j++ )
                    Food[i][j] = Foods[i][j];
                
            }
        }
        
    }
    
    //Probability of all food sources based on their fitness value
    void CalculateProbabilities() {
        int i;
        double totfit = fitness[0];
        
        for( i=1; i<SN; i++ ) 
            totfit += fitness[i];
        
        for( i=0; i<SN; i++ )
            prob[i] = fitness[i]/totfit;
    }
    
    // Onlooker bee phase
    void SendOnlookerBees() {
        int i,j,t;
        i = t = 0;
        
        while( t<SN ) {
            pr = (   (double)Math.random()*32767 / ((double)(32767)+(double)(1)) );
            if( pr<prob[i] ) {
                t++;
                int randSelecJ[] = new int[noj];
                boolean randSelecD[] = new boolean[D];
                int nj[] = new int[noj];
            
                for( j=0; j<noj; j++) 
                    nj[j] = nom;
            
                // Exchanging information with a neighbouring food source based on PBX Method
                for( j=0; j<D; j++ ) {
                    pr = ((double) Math.random()*32767 / ((double)(32767)+(double)(1)) );
                    if( pr>=0.5 ) {
                        randSelecJ[Food[i][j]]++;
                        randSelecD[j] = true;
                    }
               
                }
            
                pr = ((double) Math.random()*32767 / ((double)(32767)+(double)(1)) );
                r1 = (int)(pr*SN);
            
                while( r1==i ) {
                    pr = ((double) Math.random()*32767 / ((double)(32767)+(double)(1)) );
                    r1 = (int)(pr*SN);
                }
            
                neighbour = r1;
            
                int k = 0;
            
                for( j=0; j<D; j++ ) {
                    if( randSelecD[j] ) {
                        while( randSelecJ[Food[neighbour][k]] == 0 ) 
                            k++;
                        randSelecJ[Food[neighbour][k]]--;
                        Food[i][j] = Food[neighbour][k];
                        k++;    
                    }
                }
            
                for( j=0; j<D; j++ ) {
                    solution[j] = Food[i][j];
                }
            
                ObjValSol = CalculateFunction(solution);
                FitnessSol = CalculateFitness(ObjValSol);
                
                //System.out.println();
                //System.out.println(ObjValSol + " " + FitnessSol + " " + fitness[i]);
            
                if( FitnessSol>fitness[i] ) {
                    trial[i] = 0;
                    //System.out.println("Onlooker");
                
                    for( j=0; j<D; j++ ) {
                        Foods[i][j] = solution[j];
                        //System.out.print(Foods[i][j] + " ");
                    }
                    
                    //System.out.println("\n" + i +" " +CalculateFunction(Foods[i]));
                
                    f[i] = ObjValSol;
                    fitness[i] = FitnessSol;
                    //System.out.println(f[i]);
               
                }
                else {
                    trial[i] += 1;
                                        
                    for( j=0; j<D; j++ )
                        Food[i][j] = Foods[i][j];
                                       
                }
            }
            i++;
            if( i==SN ) i = 0;
        }
        
    }
    
    // Scout bee phase
    void SendScoutBees() {
        int i,maxtrialindex = 0;
        
        for( i=1; i<SN; i++ ) {
            if( trial[i]>trial[maxtrialindex] ) 
                maxtrialindex = i;
            if( trial[maxtrialindex]>=limit )           // Abandon food source F[maxtrialindex]
                init(maxtrialindex);
        }
    }
    
    
    //Variable Neighbouring Search (VNS) Procedure to improve best food source
    /*void VNS_Procedure() {
        int i;
        
        for( i=0; i<D; i++ ) {
            solution[i] = GlobalParams[i];
            //System.out.print(solution[i] + " ");
        }
        
        //System.out.println();
        
        int step = 0;
        int p = 1;
        
        pr = ((double) Math.random()*32767 / ((double)(32767)+(double)(1)) );
        r1 = (int)(pr*D);
        
        pr = ((double) Math.random()*32767 / ((double)(32767)+(double)(1)) );
        r2 = (int)(pr*D);
        
        while( r2==r1 ) {
            pr = ((double) Math.random()*32767 / ((double)(32767)+(double)(1)) );
            r2 = (int)(pr*D);
        }
        
        if( r1>r2 ) {
            int t = r1;
            r1 = r2;
            r2=t;
        }
        
        Exchanging_Process(solution,r1,r2);
        
        pr = ((double) Math.random()*32767 / ((double)(32767)+(double)(1)) );
        r1 = (int)(pr*D);
        
        pr = ((double) Math.random()*32767 / ((double)(32767)+(double)(1)) );
        r2 = (int)(pr*D);
        
        while( r2==r1 ) {
            pr = ((double) Math.random()*32767 / ((double)(32767)+(double)(1)) );
            r2 = (int)(pr*D);
        }
        
        if( r1>r2 ) {
            int t = r1;
            r1 = r2;
            r2=t;
        }
        
        Inserting_Process(solution,r1,r2);
        
        pr = ((double) Math.random()*32767 / ((double)(32767)+(double)(1)) );
        r1 = (int)(pr*D);
        
        pr = ((double) Math.random()*32767 / ((double)(32767)+(double)(1)) );
        r2 = (int)(pr*D);
        
        while( r2==r1 ) {
            pr = ((double) Math.random()*32767 / ((double)(32767)+(double)(1)) );
            r2 = (int)(pr*D);
        }
        
        if( r1>r2 ) {
            int t = r1;
            r1 = r2;
            r2=t;
        }
        
        Exchanging_Process(solution,r1,r2);
        
        int ter = D*(D-1);
        
        for( i=0; i<D; i++ ) {
            sol1[i] = solution[i];
            //System.out.print(sol1[i] + " ");
        }
        
        double fitSolution = CalculateFitness(CalculateFunction(solution));
        double fitSol = CalculateFitness(CalculateFunction(sol1));
        
        while( step<=ter ) {
            pr = ((double) Math.random()*32767 / ((double)(32767)+(double)(1)) );
            r1 = (int)(pr*D);
        
            pr = ((double) Math.random()*32767 / ((double)(32767)+(double)(1)) );
            r2 = (int)(pr*D);
        
            while( r2==r1 ) {
                pr = ((double) Math.random()*32767 / ((double)(32767)+(double)(1)) );
                r2 = (int)(pr*D);
            }
            
            if( r1>r2 ) {
                int t = r1;
                r1 = r2;
                r2=t;
            }
            
            if( p==1 ) {
                Exchanging_Process(solution,r1,r2);
                fitSolution = CalculateFitness(CalculateFunction(solution));
            }
            else {
                Inserting_Process(sol1,r1,r2);
                fitSol = CalculateFitness(CalculateFunction(sol1));
            }
            
            //System.out.println(fitSolution + " " + fitSol);
            if( fitSol>fitSolution ) {
                for( i=0; i<D; i++ ) 
                    solution[i] = sol1[i];
            }
            else {
                p = Math.abs(p-1);
            }
            
            step++;
        }
        
        fitSolution = CalculateFitness(CalculateFunction(solution));
        double fitGl = CalculateFitness(GlobalMin);
        
        //System.out.println(fitSolution + " " + fitGl);
        
        if( fitSolution>=fitGl ) {
            //System.out.println(fitSolution + " " + fitGl + "\n");
            GlobalMin = CalculateFunction(solution);
            for( i=0; i<D; i++ ) {
                //System.out.print(solution[i]);
                GlobalParams[i] = solution[i];
            }
            //System.out.println();
        }
        
    }
    
    void Exchanging_Process(int sol[],int a,int b) {
        int t = sol[a];
        sol[a] = sol[b];
        sol[b] = t;
        
    }
    
    void Inserting_Process(int sol[],int a,int b) {
        int i;
        int t = sol[a];
        
        for( i=a; i<b; i++ )
            sol[i] = sol[i+1];
        
        sol[b] = t;
    }
     */ 
     
    
    //Calculate makespan time of operation sequence list
    int CalculateFunction(int sol[]) {
        JSSP jssp = new JSSP(sol,noj,nom);
        return jssp.minMakespanT;
        
    }
}
