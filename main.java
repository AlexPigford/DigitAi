import java.util.*;
public class main{
    static double[] trainCs = new double[] {.5,.35,.275,.2,.1,.05,.025,.01};
    public static NeuralNetwork nn;
    public static void main(String[] args) throws Exception{
        //int[] nodes = new int[]{1,32,64,128,256};
        //testModels(nodes);
        long start = System.nanoTime();
        nn = new NeuralNetwork(3);
        nn.setAll(784,128,10);
        boolean done = false;
        MNISTLoader loader = new MNISTLoader();
        int testN = 10000;
        double[][] testAns = new double[testN][10];
        double[][] testInputs = new double[testN][784];

        for(int i = 0;i<testN;i++){
            double[] raw = loader.getTest();
            testAns[i][(int)raw[784]]=1;

            for(int j = 0;j<784;j++){
                testInputs[i][j] = raw[j];

            }
        }
        double[][] result = new double[testN][];
        int cc = test(testN,testInputs,testAns,nn);

        double startAcc = getAcc(cc,testN);
        System.out.println(getAcc(cc,testN));

        int trainN = 60000;
        double[][] ans = new double[trainN][10];
        double[][] inputs = new double[trainN][784];
        double trainR = .2;
        for(int i = 0;i<trainN;i++){
            double[] raw = loader.getTrain();

            ans[i][(int)raw[784]]=1;

            for(int j = 0;j<784;j++){
                inputs[i][j] = raw[j];

            }
        }
        for(int i = 0;i<2;i++){
            nn.train(inputs,ans,trainR);
            trainR*=.9;
            cc = 0;

            for(int j = 0;j<testN;j++){
                result[j] = nn.calculate(testInputs[j]);
                if(getMax(result[j]) == getMax(testAns[j])){
                    cc++;
                }
            }
            System.out.println(getAcc(cc,testN));
        }

        result = new double[trainN][];
        cc = test(testN,testInputs,testAns,nn);
        System.out.println("Took: " + (System.nanoTime()-start)/1000000.0/1000 + " seconds to train");
        System.out.println(getAcc(cc,testN));
        System.out.println("Start: " + startAcc);
        System.out.println("End: " + getAcc(cc,testN));
        System.out.println("Inc: " + (getAcc(cc,testN)-startAcc));
        System.out.println(getAcc(cc,testN) + "/" + (getAcc(cc,testN)-startAcc));
        DigitWindow.createWindow();
    }

    public static void testModels(int[] nodes) throws Exception{
        for(int i = 0;i<nodes.length;i++){
            System.out.println(nodes[i]+":");
            testModel(nodes[i]);
        }
    }

    public static void testModel(int nodes) throws Exception{
        for(int g = 0;g<trainCs.length;g++){
            NeuralNetwork nn = new NeuralNetwork(3);
            nn.setAll(784,nodes,10);
            MNISTLoader loader = new MNISTLoader();
            int testN = 10000;
            double[][] testAns = new double[testN][10];
            double[][] testInputs = new double[testN][784];

            for(int i = 0;i<testN;i++){
                double[] raw = loader.getTest();
                testAns[i][(int)raw[784]]=1;

                for(int j = 0;j<784;j++){
                    testInputs[i][j] = raw[j];

                }
            }
            double[][] result = new double[testN][];
            int cc = test(testN,testInputs,testAns,nn);

            double startAcc = getAcc(cc,testN);
            //System.out.println(getAcc(cc,testN));

            int trainN = 60000;
            double[][] ans = new double[trainN][10];
            double[][] inputs = new double[trainN][784];
            for(int i = 0;i<trainN;i++){
                double[] raw = loader.getTrain();

                ans[i][(int)raw[784]]=1;

                for(int j = 0;j<784;j++){
                    inputs[i][j] = raw[j];

                }
            }
            for(int i = 0;i<5;i++){
                nn.train(inputs,ans,trainCs[g]);
                cc = 0;

                for(int j = 0;j<testN;j++){
                    result[j] = nn.calculate(testInputs[j]);
                    if(getMax(result[j]) == getMax(testAns[j])){
                        cc++;
                    }
                }
                //System.out.println(getAcc(cc,testN));
            }

            result = new double[trainN][];
            cc = test(testN,testInputs,testAns,nn);

            //System.out.println(getAcc(cc,testN));
            //System.out.println("Start: " + startAcc);
            //System.out.println("End: " + getAcc(cc,testN));
            //System.out.println("Inc: " + (getAcc(cc,testN)-startAcc));

            System.out.println(Math.round(getAcc(cc,testN)*100)/100.0 + "/" + Math.round((getAcc(cc,testN)-startAcc)*100)/100.0);

        }
        System.out.println();
    }

    public static int test(int testN, double[][] testInputs, double[][] testAns, NeuralNetwork nn){
        int cc = 0;
        double[][] result = new double[testN][];
        for(int i = 0;i<testN;i++){
            result[i] = nn.calculate(testInputs[i]);
            //printCalc(testInputs[i],nn);
            //System.out.println(getMax(result[i]));
            //System.out.println(getMax(testAns[i]));

            if(getMax(result[i]) == getMax(testAns[i])){
                cc++;
            }
        }return cc;
    }

    public static int getMax(double[] nums){
        int maxI = 0;
        for(int i = 1;i<nums.length;i++){
            if(nums[maxI] < nums[i]){
                maxI = i;
            }
        }return maxI;
    }

    public static void printCalc(double[] inputs, NeuralNetwork nn){
        System.out.println(Arrays.toString(nn.calculate(inputs)));

    }

    public static double getAcc(double correct, double total){
        return correct/total*100;
    }
}