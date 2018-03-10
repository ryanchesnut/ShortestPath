import java.util.ArrayList;
import java.util.List;

public class BellmanFord<E> {

	private long endTime;
	private int itterations = 1;

	public BellmanFord() {

	}

	public long getTestTime() {
		return this.endTime;
	}

	public int getItterations() {
		return this.itterations;
	}

	public ArrayList<ArrayList<Integer>> bellmanFord(List<Node<E>> nodes) {
		long startTime = System.nanoTime(); 

		final int N = nodes.size();
		ArrayList<ArrayList<Integer>> matrix = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> tempList = new ArrayList<Integer>();

		int[][] D_ij = new int[N][N];
		int[][] C_ij = new int[N][N];
		int[][] D_ij2 = new int[N][N];
		int[] compareList1 = new int[N];
		int[] compareList2 = new int[N];
		int[] compareList3 = new int[N];

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {

				if (i == j) {
					C_ij[i][j] = 0;
				}

				else {
					C_ij[i][j] = 100000/* Integer.MAX_VALUE-1000 */;
				}
			}// end j
		}// end i

		Edge<E> tempEdge;

		for (int i = 0; i < N; i++) {
			Node<E> tempNode = nodes.get(i);
			for (int k = 0; k < tempNode.getConnections().size(); k++) {
				tempEdge = (Edge<E>) tempNode.getConnections().get(k);
				C_ij[i][nodes.indexOf(tempEdge.getTwo())] = tempEdge
						.getDistance();
			}
		}

		itterations++; 
		// Get the values of all nodes directly connected

		while (D_ij != D_ij2) {
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					for (int n = 0; n < N; n++) {
						compareList1[n] = C_ij[i][n];
					}
					for (int m = 0; m < N; m++) {
						compareList2[m] = C_ij[m][j];
					}

					for (int v = 0; v < N; v++) {
						compareList3[v] = (compareList2[v] + compareList1[v]);
					}

					int minValue = 1000000;
					for (int d = 0; d < N; d++) {
						if (minValue > compareList3[d]) {
							minValue = compareList3[d];
						}
					}
					D_ij[i][j] = minValue;

				}// End for j
			}// End for i
			itterations++; 
			//if (D_ij != D_ij2) {
				for (int i = 0; i < N; i++) {
					for (int j = 0; j < N; j++) {
						for (int n = 0; n < N; n++) {
							compareList1[n] = D_ij[i][n];
						}
						for (int m = 0; m < N; m++) {
							compareList2[m] = D_ij[m][j];
						}

						for (int v = 0; v < N; v++) {
							compareList3[v] = (compareList2[v] + compareList1[v]);
						}

						int minValue = 88;
						for (int d = 0; d < N; d++) {
							if (minValue > compareList3[d]) {
								minValue = compareList3[d];
							}
						}
						D_ij2[i][j] = minValue;
					}// End for j
					
				}// End for i
				itterations++; 
				D_ij = D_ij2;
		}//end while
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				tempList.add(D_ij2[i][j]);
			}
			matrix.add(tempList);
			tempList = new ArrayList<Integer>();
			//System.out.println(" ");

		}
		endTime = System.nanoTime() - startTime;
		System.out.println("Bellman time: " + endTime);
		return matrix;
	}

}
