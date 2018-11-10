package io;
 
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
 
public class Generador {
 
    MatrizSimetrica matriz;
    int nodos;
    int colores;
    int aristas;
 
    double adyacencia;
 
    public Generador(int n) {
        this.matriz = new MatrizSimetrica(n);
        this.nodos = n;
    }
 
    /*
     * Para generar un grafo aleatorio dando la cantidad de nodos y la probabilidad
     * para cada arista, asignarle un valor aleatorio a cada una de ellas, y si ese
     * valor esta en el rango de adyacencia, es una arista del grafo. Si no, no.
     * luego, debo verificar si lo que genere es o no un grafo (es decir, no queden
     * nodos aislados).
     */
 
    public void generarAleatoriosProbabilidadArista(int ady) throws IOException {
        int i, j;
        for (i = 0; i < this.nodos - 1; i++)
            for (j = i + 1; j < this.nodos; j++) {
                if (Math.random() <= ady / 100)
                    this.matriz.setValor(i, j, true);
            }
        this.verfNoNodosSolos();
    }
 
    class Arista implements Comparable {
        int i;
        int j;
        double random;
 
        public Arista(int i, int j, double r) {
            this.i = i;
            this.j = j;
            this.random = r;
        }
 
        @Override
        public int compareTo(Object o) {
            Arista otra = (Arista) o;
            return Double.compare(this.random, otra.random);
        }
    }
 
    /*
     * Para crear un grafo a partir de la cantidad de nodos y del porcentaje de
     * adyacencia real, le asignamos a cada arista un valor aleatorio y ordenamos el
     * conjunto de aristas por medio de ese valor. Luego, tomamos el valor de
     * aristas correspondientes al porcentaje de adyacencia, y luego verificamos si
     * lo que cree es un grafo.
     */
 
    public void generarAleatoriosPorcentajeAdyacencia(int ady) throws IOException {
 
        ArrayList<Arista> lista = new ArrayList<Arista>();
 
        if (!this.verificarMinMax(ady))
            throw new IOException("Con la adyacencia indicada es imposible generar un grafo Xd");
        for (int i = 0; i < this.nodos - 1; i++)
            for (int j = i + 1; j < this.nodos; j++) {
                Arista nueva = new Arista(i, j, Math.random());
                lista.add(nueva);
            }
        lista.sort(null);
 
        int total = (this.nodos) * (this.nodos - 1) / 2;
        int max = ady * total / 100;
        int i = 0;
 
        Iterator<Arista> it = lista.iterator();
        while (it.hasNext() && i++ < max) {
            Arista nueva = it.next();
            this.matriz.setValor(nueva.i, nueva.j, true);
        }
        this.verfNoNodosSolos();
    }
 
    /*
     * Para crear un grafo regular sabiendo su cantidad de nodos y su grado, primero
     * debo verificar si es posbile crear un grafo asi. Una vez que verifico eso, lo
     * creo. En este caso no quedaran aristas sin conexiones con otras.
     */
 
    public boolean generarRegularesGrado(int g) throws IOException {
       
        // Verifico que sea posible la creacion de un grafo de n nodos y grado g
       
        if (!esPosible(g))
            return false;
       
        // La forma de crear un grafo de grado g consiste en ir incluyendo aristas
        // que salten de a i nodos, incrementando esa cantidad hasta que i sea igual
        // a la cantidad de nodos
       
        for (int i = 1; i <= g/2; i++)
            this.generarRegularesConSalto(i);
       
        // Si el grado del grafo que quiero crear es impar, debo agregarle la "cruz",
        // uniendo cada nodo con su opuesto en el diagrama
       
        if (g%2!=0)
            this.generarRegularesGrado1();
        return true;
    }
   
    private boolean generarRegularesGrado1() throws IOException {
        for (int i = 0; i < this.nodos / 2; i++)
            this.matriz.setValor(i, (i + this.nodos / 2), true);
        return true;
    }
 
    private boolean generarRegularesConSalto(int s) throws IOException {
        for (int i = 0; i < this.nodos; i++)
            this.matriz.setValor(i, (i + s) % this.nodos, true);
        return true;
    }
 
    private boolean esPosible(int grado) {
        if (this.nodos % 2 == 0 && grado >= 1 && grado < this.nodos)
            return true;
        else if (this.nodos % 2 != 0 && grado % 2 == 0 && grado >= 2 && grado <= this.nodos)
            return true;
        return false;
    }
 
    /*
     *  Para crear un grafo regular sabiendo el porcentaje de adyacencia, simplemente
     *  puedo realizar una conversion, dado que:
     *  
     *    % ady = G / (N-1), siendo G el grado del grafo y N la cantidad de nodos.
     *
     *  Si despejo el grado del grafo, obtengo:
     *
     *   G = % ady * (N-1), y creo el grafo sabiendo el grado y la cantidad de nodos.
     */
 
    public boolean generarRegularesAdyacencia(double ady) throws IOException {
        return this.generarRegularesGrado((int)(ady/100 * (this.nodos - 1)));
    }
 
    /*
     *  La particularidad que caracteriza a los grafos n-partitos, es que estan
     *  dividos en n-conjuntos disjuntos, de modo que ningun nodo se vincula
     *  con otro que pertenezca al mismo conjunto.
     */
   
    /*public boolean generarNPartitos(int k) throws IOException {
       
        // No se puede crear un grafo n partito si n es mayor a la cantidad de nodos.
       
        if (k > this.nodos)
            return false;
       
        /*
         * Idea: crear N/n vectores, e ir recorriendo todos los vectores (salvo el
         * ultimo), y crear aristas que vinculen a cada elemento de un vector
         * con todos los elementos del vector siguiente). Fin xD.
         */
        /*int pivote = 0;
        for (int i = 0; i < this.nodos - 1; i++) {
        	if(pivote == k)
        		pivote=0;
        	for (int j = k - pivote; j < this.nodos; j++)
        		this.matriz.setValor(i, j, true);
        	pivote++;
        }
       
        int p = this.nodos % k == 0? this.nodos / k : (this.nodos / k) + 1;
        
        for (int i = 0; i < (this.nodos / k) * k ; i++) {
        	for (int j = p * ((i/p) + 1) ; j < this.nodos ; j++) {
        		System.out.println(i + " - " + j);
        	//for (int j = (this.nodos % k == 0? this.nodos / k : (this.nodos / k) + 1) * ((i/k) + 1) ; j < this.nodos ; j++) {
        		this.matriz.setValor(i, j, true);
        	}
        }        
        return true;
    }*/

	public boolean generarNPartitos(int k) throws IOException {
		if (k > this.nodos)
            return false;
		int c = 0, i;
		ArrayList<Integer> conjuntos []= new ArrayList [k];
		for (i = 0; i < k - 1; i++) {
			conjuntos[i] = new ArrayList<Integer>();
			for (int j = 0; j < this.nodos / k; j++)
				conjuntos[i].add(c++);
		}
		conjuntos[i] = new ArrayList<Integer>();
		while (c < this.nodos) 
			conjuntos[i].add(c++);
		
		for (i = 0; i < k; i++)
			System.out.println(conjuntos[i].toString());
		
		for (i = 0; i < k - 1; i ++) 
			for (int j = i + 1; j < k; j++)
				for(int y = 0; y < conjuntos[i].size(); y++)
					for (int p = 0; p < conjuntos[j].size() ; p++) 
						this.matriz.setValor(conjuntos[i].get(y), conjuntos[j].get(p), true);
		
		return true;
    }
 
    
    
    private void verfNoNodosSolos() throws IOException {
        for (int i = 0; i < this.nodos; i++) {
            int acum = 0;
            for (int j = i + 1; j < this.nodos; j++)
                acum += this.matriz.getValor(i, j) ? 1 : 0;
            if (acum == 0) {
                int c = (int) (Math.random() * (double) this.nodos);
                while (i == c)
                    c = (int) (Math.random() * (double) this.nodos);
                this.matriz.setValor(i, c, true);
            }
        }
    }
 
    private boolean verificarMinMax(int ady) {
 
        int max = this.nodos * (this.nodos - 1) / 2; // 10
        int min = this.nodos / 2;// 2
 
        if ((double) (ady) / 100 * max >= min && (double) (ady) / 100 * max <= max)
            return true;
        return false;
    }
 
    public static void main(String[] args) throws IOException {
        Generador gene = new Generador(8);
        //gene.generarRegularesAdyacencia(100);
        gene.generarNPartitos(8);
        gene.matriz.mostrarMatriz();
    }
}