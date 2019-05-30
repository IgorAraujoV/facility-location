package Package;

import static java.util.Arrays.fill;

public class Solution {
    /*Facilidade que atende o cliente i*/
    public int facOf[];
    
    /*Facilidade é ou não usada*/
    public boolean facU[];
    
    Facility fac;
    
    /*Custo de implantação da facilidade i*/
    public double facCostImp[];
    
    /*Numero de facilidades*/
    public int N;
    
    /*Numero de clientes*/
    public int cli;
    
    public static final int MAX = 99999999;

    @Override
    public String toString() {
        return "Solution { " + 
                "Quantidade de facilidades = " + facUsedCount()
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
        
        double costCnd[] = new double[cli]; // custo canditado para atendimento do cliente j
        fill(costCnd, MAX);
        
        for (int f=0; f<N; f++) {
            
            for (int c=0; c<cli; c++) {
                
                if (fac.costFacCli[f][c] <= costCnd[c]) {
                    if(costCnd[c] == MAX) {
                        if (fac.sumDem[f] + fac.demCli[c] <= fac.c[f]) {
                            facU[f] = true;
                            fac.sumDem[f] += fac.demCli[c];
                            facOf[c] = f;
                            costCnd[c] = fac.costFacCli[f][c];
                        }
                    }
                }
            }
        }
    }
    
    
}
