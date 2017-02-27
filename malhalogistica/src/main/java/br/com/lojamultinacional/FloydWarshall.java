package br.com.lojamultinacional;

import static java.lang.String.format;
import java.util.Arrays;

public class FloydWarshall {

	
	/**
	 * Calcula matriz de custo e matriz de adjacencia em O(n³).
	 * Porém, só ocorre na chamada para salvar as matrizes no banco de dados.
	 * 
	 * @param mapa
	 */
	static void floydWarshall(Mapa mapa) {

		int[][] weights = mapa.getMatrizInicialCusto();
		int numVertices = mapa.getNumberOfVertices();
		double[][] dist = new double[numVertices][numVertices];
		for (double[] row : dist)
			Arrays.fill(row, Double.POSITIVE_INFINITY);

		for (int[] w : weights){
			dist[w[0]][w[1]] = w[2];
			dist[w[1]][w[0]] = w[2];
		}
		
		for (int i = 0; i < numVertices; i++)
			dist[i][i] = 0;

		int[][] next = new int[numVertices][numVertices];
		for (int i = 0; i < next.length; i++) {
			for (int j = 0; j < next.length; j++)
				if (i != j)
					next[i][j] = j + 1;
		}

		for (int k = 0; k < numVertices; k++)
			for (int i = 0; i < numVertices; i++)
				for (int j = 0; j < numVertices; j++)
					if (dist[i][k] + dist[k][j] < dist[i][j]) {
						dist[i][j] = dist[i][k] + dist[k][j];
						next[i][j] = next[i][k];
					}

		//printResult(dist, next);
		mapa.setMatrizCustoCompleta(dist);
		mapa.setMatrizCaminho(next);
	}

	/**
	 * Método de print para debugar código
	 * 
	 * @param dist
	 * @param next
	 */
	static void printResult(double[][] dist, int[][] next) {
		System.out.println("pair     dist    path");
		for (int i = 0; i < next.length; i++) {
			for (int j = 0; j < next.length; j++) {
				if (i != j) {
					int u = i + 1;
					int v = j + 1;
					String path = format("%d -> %d    %2d     %s", u, v,
							(int) dist[i][j], u);
					do {
						u = next[u - 1][v - 1];
						path += " -> " + u;
					} while (u != v);
					System.out.println(path);
				}
			}
		}
	}
}