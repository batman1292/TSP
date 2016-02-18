/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tsp;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author 2IDR-PC
 */
public class Tour {
    private ArrayList<City> tour = new ArrayList<City>();
    private double fitness = 0.0;
    private double distance = 0.0;
    
    public Tour(int num_city, City [] city){
        for (int i = 0; i < num_city; i++)
            this.tour.add(city[i]);
    }
    
    public void initIndividual(){
        Collections.shuffle(this.tour);
    }
    
    public void calDistance(){
        for(int i = 0; i < this.tour.size()-1; i++){
            this.distance += this.tour.get(i).distanceTo(this.tour.get(i));
        }
    }
    
    public double getDistance(){
        if(this.distance == 0){
            calDistance();
        }
        return this.distance;
    }
    
    public double getFitness(){
        if(this.distance == 0){
            calDistance();
        }
        this.fitness /= this.distance;
        return this.fitness;
    }
}
