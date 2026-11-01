import java.util.*;
import java.io.*;
public class MNISTLoader{
    BufferedReader in, in2;
    public MNISTLoader(){
        try{
            in = new BufferedReader(new FileReader("mnist_train.csv"));
            in2 = new BufferedReader(new FileReader("mnist_test.csv"));
        }
        catch(Exception e){
            System.out.println("Something went wrong");
        }
    }

    public double[] getTrain() throws Exception{
        String s = in.readLine();
        if(s==null){
            in = new BufferedReader(new FileReader("mnist_train.csv"));
            s = in.readLine();
        }
        String[] temp = s.split(",");
        double[] nums = new double[785];
        nums[784] = Double.parseDouble(temp[0]);
        for(int i = 0;i<784;i++){

            nums[i] = Double.parseDouble(temp[i+1])/255;

        }

        return nums;
    }

    public double[] getTest() throws Exception{
        String s = in2.readLine();
        if(s==null){
            in2 = new BufferedReader(new FileReader("mnist_test.csv"));
            s = in2.readLine();
        }
        String[] temp = s.split(",");
        double[] nums = new double[785];
        nums[784] = Double.parseDouble(temp[0]);
        for(int i = 0;i<784;i++){

            nums[i] = Double.parseDouble(temp[i+1])/255;

        }

        return nums;
    }
}
