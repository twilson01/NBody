import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JFileChooser;

import princeton.StdAudio;
import princeton.StdDraw;

public class NBody{
	
    public static final double G = 6.67E-11;

    /**
     * returns Euclidean distance between (x1, y1) and (x2, y2)
     *
     * @param x1
     *            x-coordinate of point 1
     * @param y1
     *            y-coordinate of point 1
     * @param x2
     *            x-coordinate of point 2
     * @param y2
     *            y-coordinate of point 2
     * @return Euclidean distance between (x1, y1) and (x2, y2)
     */
    public double distance(double x1, double y1, double x2, double y2) {
    	double dist = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2-y1,2));
    	
        return dist;
    }
    
    
    /**
     * return the magnitude of the gravitational force between two bodies of
     * mass m1 and m2 that are distance r apart
     *
     * @param m1
     *            mass of body 1 in kg
     * @param m2
     *            mass of body 2 in kg
     * @param r
     *            distance in m
     * @return force between m1 and m2 that are distance r apart
     */
    public double force(double m1, double m2, double r) {
           double F = (G*(m1)*(m2))/Math.pow(r, 2);
    	
        return F;
    }


    /**
     * Returns the x positions and y positions of bodies
     * @param totalTime The total amount of universe time to run for
     * @param timeStep The value of delta t in the equations to calculate position
     * @param info The scanner with info about the initial conditions of the
     * bodies
     * @return an array whose first element is the x and y position of the first body, and so on
     * t = totalTime
     */
    public double[][] positions(Scanner info, int totalTime, int timeStep) {
        //TODO: Complete positions
    	
    	int bodies = info.nextInt();
    	double r = info.nextDouble();
    	double px[] = new double[bodies];
    	double py[] = new double[bodies];
    	double vx[] = new double[bodies];
    	double vy[] = new double[bodies];
    	double mass[] = new double[bodies];
    	String image[] = new String[bodies];
    	
    	double[][] output = new double[bodies][2]; //Replace 0 with the number of
        										//bodies, read from the file
    	
    	for (int i = 0; i < bodies; i++){
    		px[i] = info.nextDouble();
    		py[i] = info.nextDouble();
    		vx[i] = info.nextDouble();
    		vy[i] = info.nextDouble();
    		mass[i] = info.nextDouble();
    		image[i] = info.next();
    	}
    	
    	
    	double fx[] = new double[bodies];
    	double fy[] = new double[bodies];
    	
    	for (int i = 0; i < totalTime; i += timeStep){
    		
    		StdDraw.setXscale(-r, r);
			StdDraw.setYscale(-r, r);
			StdDraw.clear();
	    	StdDraw.picture(0, 0, "data/starfield.jpg");
	    	
    	

	    	for(int k = 0; k < mass.length; k++ ){
	    		double forcex = 0; 
	    		double forcey = 0; 
	    		for(int p = 0; p < mass.length; p++){
	    			if(p!=k){
	    				forcex += force(mass[k], mass[p], distance(px[k], py[k], px[p], py[p])) * (px[p]-px[k])/distance(px[k], py[k], px[p], py[p]);
	    				forcey += force(mass[k], mass[p], distance(px[k], py[k], px[p], py[p])) * (py[p]-py[k])/distance(px[k], py[k], px[p], py[p]); 
	    			}
	    		}
	    		fx[k] = forcex;
	    		fy[k] = forcey;
	    		//System.out.println(fx[k] + " " + fy[k]);
	    		//updates force
    			
    			}
	    		double accelerationx[] = new double [bodies];
	    		double accelerationy[] = new double [bodies];
	    		for(int j = 0; j < px.length; j++ ){
	    			accelerationx[j] = fx[j]/mass[j];
	    			accelerationy[j] = fy[j]/mass[j];
	    			System.out.println(accelerationx[j]);
			
			
	    			//updates acceleration
			
	    		}
    	
		
	    		for(int m = 0; m < px.length; m++){
	    			vx[m] +=  timeStep * accelerationx[m];
	    			vy[m] += timeStep *accelerationy[m];
	    			px[m] += vx[m] * timeStep;
	    			py[m] += vy[m] * timeStep;
	    			System.out.println(px[m]);
			
	    			//updates velocity and position 
			
    		
    		
    		
	    		}
    		
	    		for(int z = 0; z < image.length; z++){
	    			StdDraw.picture(px[z], py[z], "data/" + image[z]);
	    		}

	    		for(int value = 0; value < bodies; value++){
	    			output[value][0] = px[value];
	    			output[value][1] = py[value];
        		
	    		}
	    		StdDraw.show(100);
        	}
    	return output;
    	
    }

    public static void main(String[] args) {
        Scanner info = openFile();
        int time = 10000000;
        int dt = 25000;
        
        if (info != null) {
            StdAudio.play("data/2001.mid");
            NBody myNBody = new NBody();
            double[][] results = myNBody.positions(info, time, dt);
            for(int i = 0; i < results.length; i++) {
                for(int j = 0; j < results[i].length; j++) {
                    System.out.print(results[i][j]+" ");
                }
                System.out.println();
            }
            StdAudio.close();
        }
    }
    /**
     * Displays file chooser for browsing in the project directory. and opens
     * the selected file
     *
     * @return a new Scanner that produces values scanned from the selected
     *         file. null if file could not be opened or was not selected
     */
    
    public static Scanner openFile() {
        Scanner scan = null;
        System.out.println("Opening file dialog.");
        JFileChooser openChooser = new JFileChooser(System.getProperties()
                                                    .getProperty("user.dir"));
        
        int retval = openChooser.showOpenDialog(null);
        if (retval == JFileChooser.APPROVE_OPTION) {
            File file = openChooser.getSelectedFile();
            System.out.println(file.getAbsolutePath());
            try {
                scan = new Scanner(file);
                System.out.println("Opening: " + file.getName() + ".");
            } catch (FileNotFoundException e) {
                System.out.println("Could not open selected file.");
                e.printStackTrace();
            }
        } else {
            System.out.println("File open canceled.");
            System.exit(0);
        }
        
        return scan;
    }
}