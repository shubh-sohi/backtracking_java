
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class Quantum {


    //makes an empty grid
    private static int [][] make_grid(int X){
        int row_size = X;
        int coln_size = X+2;
        int[][] grid = new int[row_size][coln_size];
        for (int i = 0; i < row_size; i++){
            int[] temp = new int[coln_size];
            for (int j = 0; j< coln_size; j++){
                //initiates the first and last row with all 1 for easy computation purposes
                if (i==0 || i ==X-1) temp[j] = 1;
                else temp[j] = 0;
            }
            grid[i] = temp;
        }
        return grid;
    }

    //backtracking in full swing
    private static int possibleConnection(int[][] grid, int X){
        int row_size = X;
        int coln_size = X+2;
        int count = 0;
        //starting with the first row and compare with all others rows except the same as here
        for (int i = 0; i < row_size; i++){
            for (int j = 0; j< row_size; j++){
                boolean CW_or_WC = false;
                ArrayList<Integer> innerCountList = new ArrayList<Integer>();
                if (j>i){
                    if (j-i == row_size-1)break;
                    //iterator for the columns
                    for (int k = 0; k < coln_size; k++){
                        boolean initail = false;
                        int diff = j - i -1;
                        if (grid[i][k] == 1 && grid[j][k] == 1){
                            initail = true;
                        }
                        //check if rows are adjacent
                        if (!initail){
                            if (k == coln_size-1 && !CW_or_WC){
                                return 30;
                            }
                            continue;
                        }
                        //checks if rows are at 1 row apart
                        if (initail){
                            CW_or_WC = true;
                            if (diff == 0){
                                break;
                            }
                            if (diff == 1){
                                if (grid[i+1][k] ==0){
                                    innerCountList.add(0);
                                    break;
                                }
                                if (k == coln_size-1){
                                    break;
                                }
                                else{
                                    innerCountList.add(1);
                                }
                            }
                            //check is rows are more than 1 row apart
                            if (diff > 1){
                                boolean innerBool = true;
                                int DownRow = i;
                                int innerCount = 0;
                                while (DownRow < j-1){
                                    DownRow +=1;
                                    if (grid[DownRow][k] == 0 && innerBool){
                                        innerBool = true;
                                    }
                                    else{
                                        if (grid[DownRow][k] == 1){
                                            innerCount += 1;
                                        }
                                        innerBool = false;
                                    }
                                }
                                if (innerBool){
                                    innerCountList.add(0);
                                    break;
                                }
                                else{
                                    innerCountList.add(innerCount);
                                }
                            }
                        }
                    }
                }
                if (innerCountList.size() > 0){
                    count += Collections.min(innerCountList);
                }
            }
        }
        return count;
    }

    //generator for the line in a row which was initiated to all zeros using a start stop index
    private static void generate_number(int [] grid, int start , int stop, int X){
        int coln_size = X+2;
        for (int i = 0; i < coln_size; i++){
            if (i < start){
                grid[i] = 0;
            }
            if ( i >= start){
                if (i < stop){
                    grid[i] = 1;
                }
            }
            if (i >= stop){
                grid[i] = 0;
            }
        }
    }

    //provides a start stop index integer and saves it in a arraylist of arraylist, makes all the possible combinations
    //using brute force.
    private static ArrayList<ArrayList<Integer>> SSlist(int X){
        int coln_size = X+2;
        ArrayList<ArrayList<Integer>> SSlist = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < coln_size; i++){
            ArrayList<Integer> tempList = new ArrayList<Integer>();
            int start = 0;
            int stop = i+1;
            tempList.add(start);
            tempList.add(stop);
            SSlist.add(tempList);
            for (int j = 0; j < coln_size; j++){
                if (stop < coln_size){
                    ArrayList<Integer> tempList01 = new ArrayList<Integer>();
                    start +=1;
                    stop +=1;
                    tempList01.add(start);
                    tempList01.add(stop);
                    SSlist.add(tempList01);
                }
            }
        }
        return SSlist;
    }


    //everything comes together here, uses the start stop list and the generate numbers function
    //to generate a row with a line representing one's and then is simultaneously checked by the backtracking function
    public static void makeRowCombination(int X, int [][]grid,  ArrayList<Integer> countList, ArrayList<ArrayList<Integer>> SSlist, int row){
        for (int i = 0; i < SSlist.size(); i++){
            int start = SSlist.get(i).get(0);
            int stop = SSlist.get(i).get(1);
            generate_number(grid[row+1], start, stop, X);
            int temp = possibleConnection(grid, X);
            if(temp <20){
                countList.add(temp);
            }
            if (row < grid.length-3){
                makeRowCombination(X, grid, countList, SSlist, row+1);
            }
        }
    }

//****************
//START: READ ONLY
//****************

    /**
//     * @param n : The number of buses
     * @return The cost of minimum crossing configuration with X buses
     */
    public static int cost(int X) {

        //sss669
        if (X == 1 || X == 0){
            return 0;
        }
        int [][] grid = make_grid(X);
        ArrayList<ArrayList<Integer>> SSlist = SSlist(X);
        ArrayList<Integer> countList = new ArrayList<Integer>();
        makeRowCombination(X, grid, countList, SSlist, 0);
//        System.out.println(countList);
        int MinValue = Collections.min(countList);
        //start: edit and write your code here

        return MinValue;
        //end: write your code here

    }


    //File I/O was provided by the professor


    /**
     * Main Function.
     */
    public static void main(String[] args) {

        BufferedReader reader;
        File file = new File("output.txt");
        int X = 0;
        String line;
        try {
            reader = new BufferedReader(new FileReader("Quantum.txt"));
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            while(true){
                line = reader.readLine();
                if(line == null) break;
                X = Integer.parseInt(line);
                writer.write(cost(X) + "\n");
                writer.flush();
            }

            reader.close();
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("Could not locate input file.");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

}