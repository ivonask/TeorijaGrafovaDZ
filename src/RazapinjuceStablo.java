import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class RazapinjuceStablo {

    private static int brojVrhova;
    private static int[][] matricaSusjedstva;
    private static int[][] L;

    private static int[][] parseInput (String path) {
        File file = new File(path);

        List<String> lines = Collections.emptyList();
        try
        {
            lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        brojVrhova = Integer.parseInt(lines.get(0).trim());
        lines.remove(0);
        lines.remove(0);

        matricaSusjedstva = new int[brojVrhova][brojVrhova];

        for (int i = 0; i<brojVrhova; i++) {

            String data[] = lines.get(i).split(" ");
            for (int j = 0; j<brojVrhova; j++) {
                matricaSusjedstva[i][j]=Integer.parseInt(data[j]);
            }
        }
        return matricaSusjedstva;
    }

    public static int[][] LaplaceovaMatrica(int[][] M) {
        int A[][] = new int[brojVrhova][brojVrhova];
        int length = M.length;

        for(int i = 0; i < length; i++) {
            int count = 0;
            for(int j = 0; j < length; j++) {
                if(M[i][j] == 1) {
                    count++;
                    A[i][j] = -1;
                }
            }
            A[i][i] = count;
        }
        return A;
    }

    private static int brRazapinjucihStabala(int[][] M) {
        int iStop, jStop, kStop;
        iStop = jStop = kStop = 1;

        int det = 1;
        int divisor = 1;

        for(int i = M.length - 1; i > iStop; i--) {
            for(int j = i - 1; j >= jStop; j--) {
                int jScalar = M[i][i]; //scalar for row j
                int iScalar = M[j][i]; //scalar for row i
                if (iScalar != 0) {
                    if (jScalar == 0) {
                        int[] temp = M[i].clone();
                        M[i] = M[j].clone();
                        M[j] = temp.clone();
                        det *= -1;
                    } else {
                        divisor *= jScalar;
                        for(int k = i; k >= kStop; k--) {
                            M[j][k] =  -iScalar * M[i][k] + jScalar * M[j][k];
                        }
                    }
                }
            }
            det *= M[i][i];
        }

        //assert: The determinant of any matrix is an integer. The number of spanning trees of a graph is a nonnegative integer.
        return det * M[1][1]/ divisor;
    }

    private static void print(int[][] M) {
        System.out.println();

        for (int i = 0; i < M[0].length; i++) {
            for (int j = 0; j < M[0].length; j++) {
                System.out.print(M[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static int[][] pronadiRazapinjuceStablo (int[][] ms) {
        int [][] stablo = new int[brojVrhova][brojVrhova];

        stablo[0] = ms[0];

        Set<Integer> posjeceni = new HashSet<>();

        for (int i=0; i<brojVrhova; i++) {
            if (stablo[0][i] == 1) {
                posjeceni.add(i);
            }
            if (!posjeceni.isEmpty()) posjeceni.add(0);
            stablo[i][0] = ms[i][0];
        }

        for (int i=1; i<brojVrhova; i++) {
            if (posjeceni.size() == brojVrhova) break;
            if (!posjeceni.contains(i)) {
                for (int j = 0; j < brojVrhova; j++) {
                    if (ms[i][j] == 1) {
                        if (posjeceni.contains(j)) {
                            stablo[i][j] = 1;
                            stablo[j][i] = 1;
                            posjeceni.add(i);
                            break;
                        }
                    }
                }
            }
        }

        print(stablo);

        return stablo;
    }


    public static void main(String[] args) {

        String path = args[0];
        matricaSusjedstva = parseInput(path);
        L = LaplaceovaMatrica(matricaSusjedstva);
        int rez = brRazapinjucihStabala(L);
        System.out.println(rez);
        pronadiRazapinjuceStablo(matricaSusjedstva);
    }
}
