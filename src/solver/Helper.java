/**
 * 
 */
package solver;

/**
 * @author Administrator
 * 
 */
public class Helper {

	/**
	 * Copy cells.
	 * 
	 * @param cells
	 *            the cells
	 * 
	 * @return the int[]
	 */
	public static boolean[] copyCells(boolean[] cells) {
		// Create new reference variable to store array and load data into it.
		//
		boolean[] newCells = new boolean[cells.length];

		for (int i = 0; i < cells.length; i++) {
			newCells[i] = cells[i];
		}

		return newCells;
	}

	/**
	 * Copy cells.
	 * 
	 * @param cells
	 *            the cells
	 * 
	 * @return the int[]
	 */
	public static int[] copyCells(int[] cells) {
		// Create new reference variable to store array and load data into it.
		//
		int[] newCells = new int[cells.length];

		for (int i = 0; i < cells.length; i++) {
			newCells[i] = cells[i];
		}

		return newCells;
	}

	/**
	 * Copy cells.
	 * 
	 * @param cells
	 *            the cells
	 * 
	 * @return the int[][]
	 */
	public static int[][] copyCells(int[][] cells) {
		// Create new reference variable to store array and load data into it.
		//
		int[][] newCells = new int[cells.length][cells.length];

		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells.length; j++) {
				newCells[i][j] = cells[i][j];
			}
		}

		return newCells;
	}

	public static void outputCells(int[][] cells) {
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells.length; j++) {
				System.out.print(cells[i][j]);
			}
			System.out.println();
		}
	}
}
