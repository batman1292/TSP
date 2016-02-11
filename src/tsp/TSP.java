/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tsp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author 2IDR-PC
 */
public class TSP {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        readData();
    }
    
    public static void readData() {
        String path = "D:\\BVH\\MEE\\Intelligent System\\Task4\\berlin52.txt";
        File file = new File(path);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            int current_line = 0;
            while ((line = br.readLine()) != null) {
//                System.out.println(line);
                String[] ary = line.split(" ");
                for (int i = 0; i < ary.length; i++) {
                    System.out.println(ary[i]);
                }
                current_line++;
            }
            br.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
