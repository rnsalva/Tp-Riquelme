package io;

import java.io.IOException;

public class MatrizSimetrica {

	boolean[] vector;
	int n;

	public MatrizSimetrica(int n) {
		this.n = n;
		vector = new boolean[(n * n - n) / 2];
	}

	public boolean getValor(int f, int c) throws IOException {
		if (f > c) {
			int aux = f;
			f = c;
			c = aux;
		}
		if (f == c)
			return false;
		return this.vector[f * this.n + c - (f * f + 3 * f + 2) / 2];
	}

	public void setValor(int f, int c, boolean v) throws IOException {
		if (f > c) {
			int aux = f;
			f = c;
			c = aux;
		}
		if (f == c)
			return;
			//throw new IOException("Es el mismo valor, salame");
		this.vector[f * this.n + c - (f * f + 3 * f + 2) / 2] = v;
	}

	public void mostrarVector() {
		for (boolean b : this.vector)
			System.out.println(b);
	}

	public void mostrarMatriz() throws IOException {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				System.out.print((this.getValor(i, j)?1:0) + " ");
			}
			System.out.println();
		}
	}

}