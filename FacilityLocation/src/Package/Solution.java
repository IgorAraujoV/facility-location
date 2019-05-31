package Package;

import static java.util.Arrays.fill;

public class Solution {
    /*Facilidade que atende o cliente i*/
    public int facOf[];
    
    /*Facilidade é ou não usada*/
    public boolean facU[];
    
    Facility fac;
    
    /*Custo total da implantação das facilidades*/
    public double facCost;
    
    /*Quantidade de facilidades usadas*/
    public int facUsedCount;
    
    /*Numero de facilidades*/
    public int N;
    
    /*Numero de clientes*/
    public int cli;
    
    /*Salvar demanda de cada facilidade*/
    public double save[];
    
    public static final int MAX = 99999999;

    @Override
    public String toString() {
        return "Solution { " + 
                "Facilidades Usadas = " + facUsedCount()
                + ", Custo = " + cost()
                + '}';
    }
    
    public Solution(Facility fac)
    {
        this.fac = fac;
        N = fac.N;
        cli = fac.cli;
        facOf = new int[cli];
        facU = new boolean[N];
        save = new double[N];
    }
    
    /*Número de facilidades usadas*/
    public int facUsedCount()
    {
        int count = 0;
        for (boolean u : facU)
            if(u)
                count++;
        return count;
    }
    
    /*Custo de implantação + custo de atendimento
    do cliente i pela facilidade de i*/
    public double cost()
    {
        double cost = 0;
        for (int i=0; i<N; i++) {
            if (facU[i])
                cost += fac.costImp[i];
        }
        
        for (int i=0; i<cli; i++) {
            cost += fac.costFacCli[facOf[i]][i];
        }
        
        return cost;
    }
    
    public void run() 
    {
        fill(facOf, -1);
        fill(facU, false);
        fill(fac.sumDem, 0);
        
        /*Custo canditado para atendimento do cliente j*/
        double costCnd[] = new double[cli]; 
        fill(costCnd, MAX);
        
        boolean alt;
        for (int f=0; f<N; f++) {
            alt = false;
            if(!alt) {
                for (int c=0; c<cli; c++) {
                    
                    double capacity = fac.c[f] - fac.demCli[c] - fac.sumDem[f];
                    if (capacity < 0)
                        continue;
                    
                    if (fac.costFacCli[f][c] < costCnd[c]) {
                        if(costCnd[c] == MAX) {
                            if (fac.sumDem[f] + fac.demCli[c] <= fac.c[f]) {
                                facU[f] = true;
                                fac.sumDem[f] += fac.demCli[c];
                                facOf[c] = f;
                                costCnd[c] = fac.costFacCli[f][c];
                            }
                        } else {
                            if (fac.sumDem[f] + fac.demCli[c] <= fac.c[f]) {
                                int fc = facOf[c];
                                fac.sumDem[fc] -= fac.demCli[c];

                                if (fac.sumDem[fc] == 0)
                                    facU[fc] = false;
                                else if (!facU[f])
                                    facU[f] = true;

                                facOf[c] = f;
                                fac.sumDem[f] += fac.demCli[c];
                                costCnd[c] = fac.costFacCli[f][c];  
                                alt = true;
                            }
                        }
                    }                    
                }
                if (f!=0 && alt) {
                    f = -1;
                }
            }            
        }
    }
}
