import java.util.*;
public class NeuralNetwork{
    Neuron[][] layer;
    double[][] outputs;
    double[][] deltas;
    public NeuralNetwork(int layers){
        layer = new Neuron[layers][];
        outputs = new double[layers][];
        deltas = new double[layers][];
    }

    public int getLN(){
        return layer.length;
    }

    public void setAll(int inC){
        for(int i = 0;i<layer.length;i++){
            for(int j = 0;j<layer[i].length;j++){
                if(i==0){
                    layer[i][j] = new Neuron(inC,0);
                }
                else{
                    layer[i][j] = new Neuron(layer[i-1].length,0);
                }
                outputs[i] = new double[layer[i].length];
                deltas[i] = new double[layer[i].length];
            }
        }
    }
    
    public void setAll(int inC, int hidC, int endC){
        for(int i = 0;i<layer.length;i++){
            if(i!=layer.length-1){
                layer[i] = new Neuron[hidC];
                outputs[i] = new double[hidC];
                deltas[i] = new double[hidC];
                
            }
            else{
                layer[i] = new Neuron[endC];
                outputs[i] = new double[endC];
                deltas[i] = new double[endC];
            }
            double set = 0;
            if(i==0){
                set= Math.sqrt(6.0/(hidC+inC));
            }
            else if (i == layer.length-1){
                set = Math.sqrt(6.0/(endC+hidC));
            }
            else{
                set = Math.sqrt(6.0/(hidC*2));
            }
            for(int j = 0;j<layer[i].length;j++){
                if(i==0){
                    layer[i][j] = new Neuron(inC,0);
                    
                }
                else{
                    layer[i][j] = new Neuron(layer[i-1].length,0);
                }
                layer[i][j].setWR(set);
            }
        }
    }
    
    public void setAll(int inC, int[] hidC, int endC){
        for(int i = 0;i<layer.length;i++){
            if(i!=layer.length-1){
                layer[i] = new Neuron[hidC[i]];
                outputs[i] = new double[hidC[i]];
                deltas[i] = new double[hidC[i]];
            }
            else{
                layer[i] = new Neuron[endC];
                outputs[i] = new double[endC];
                deltas[i] = new double[endC];
            }
            double set = 0;
            if(i==0){
                set= Math.sqrt(6.0/(hidC[0]+inC));
            }
            else if (i == layer.length-1){
                set = Math.sqrt(6.0/(endC+hidC[hidC.length-1]));
            }
            else{
                set = Math.sqrt(6.0/(hidC[i]+hidC[i-1]));
            }
            for(int j = 0;j<layer[i].length;j++){
                if(i==0){
                    layer[i][j] = new Neuron(inC,0);
                }
                else{
                    layer[i][j] = new Neuron(layer[i-1].length,0);
                }
                layer[i][j].setWR(set);
            }
        }
    }
    
    public NeuralNetwork(Neuron[][] layer){
        this.layer = layer;
        outputs = new double[layer.length][];
        deltas = new double[layer.length][];
        for(int i = 0;i<outputs.length;i++){
            outputs[i] = new double[layer[i].length];
            deltas[i] = new double[layer[i].length];
        }
    }

    public void setLayer(int i,Neuron[] layer){
        this.layer[i] = layer;
        outputs[i] = new double[layer.length];
        deltas[i] = new double[layer.length];
    }

    public void setLayer(int i,Neuron n){
        layer[i] = new Neuron[] {n};
        outputs[i] = new double[1];
        deltas[i] = new double[1];
    }

    public double[] calculate(double[] nums){
        double[] values = new double[layer[0].length];
        for(int i = 0;i<layer[0].length;i++){
            double t = layer[0][i].calculate(nums);
            outputs[0][i] = t;
            values[i] = t;
        }
        for(int i = 1;i<layer.length;i++){

            double[] temp = new double[layer[i].length];
            for(int j = 0;j<layer[i].length;j++){
                double t = layer[i][j].calculate(values);
                outputs[i][j] = t;
                temp[j] = t;
            }
            values = temp;
        }

        return values;
    }

    public double error(double[] inputs, double target){
        double[] ans = calculate(inputs);
        double error = target - ans[0];
        return error*error;
    }

    public double[] errors(double[][] inputs, double[] targets){
        double[] errors = new double[targets.length];
        for(int i = 0;i<errors.length;i++){
            errors[i] = error(inputs[i],targets[i]);
        }return errors;
    }

    public double[][] errors(double[][] inputs, double[][] targets){
        double[][] result = new double[targets.length][];

        for(int i = 0; i < targets.length; i++){
            double[] prediction = calculate(inputs[i]);
            result[i] = new double[prediction.length];

            for(int j = 0; j < prediction.length; j++){
                double difference = prediction[j] - targets[i][j];
                result[i][j] = difference * difference;
            }
        }

        return result;
    }

    public double changeW(int i, int j,int w,double[][] inputs, double[] targets){
        double e1 = avgError(inputs,targets);
        layer[i][j].changeW(w,.001);
        double e2 = avgError(inputs,targets);
        layer[i][j].changeW(w,-.001);
        return (e1-e2)/.001;
    }

    public double changeB(int i, int j,double[][] inputs, double[] targets){
        double e1 = avgError(inputs,targets);
        layer[i][j].changeB(.001);
        double e2 = avgError(inputs,targets);
        layer[i][j].changeB(-.001);
        return (e1-e2)/.001;
    }

    public double avg(double[] nums){
        double sum = 0;
        for(double d : nums){
            sum+=d;
        }
        return sum/nums.length;
    }

    public double avgError(double[][] inputs, double[][] targets){
        double sum = 0;
        int count = 0;

        double[][] allErrors = errors(inputs, targets);

        for(int i = 0; i < allErrors.length; i++){
            for(int j = 0; j < allErrors[i].length; j++){
                sum += allErrors[i][j];
                count++;
            }
        }

        return sum / count;
    }

    public double avgError(double[][] inputs, double[] targets){
        return avg(errors(inputs,targets));
    }

    public void trainW2(double[][] inputs, double[] targets, double rate){
        for(int i = 0;i<layer.length;i++){
            for(int j = 0;j<layer[i].length;j++){
                for(int g = 0;g < layer[i][j].getNW();g++){
                    layer[i][j].changeW(g,rate*changeW(i,j,g,inputs,targets));
                }
            }
        }
    }

    public void trainB2(double[][] inputs, double[] targets, double rate){
        for(int i = 0;i<layer.length;i++){
            for(int j = 0;j<layer[i].length;j++){

                layer[i][j].changeB(rate*changeB(i,j,inputs,targets));

            }
        }
    }

    public void train2(double[][] inputs,double[] targets,double Wrate, double Brate){
        trainW2(inputs,targets,Wrate);
        trainB2(inputs,targets,Brate);
    }

    public double calcD(double a, double t){
        return 2*(a-t)*a*(1-a);
    }

    public void setD(double[] nums, double[] ans){

        calculate(nums);

        for(int i = 0;i<deltas[deltas.length-1].length;i++){
            double a = outputs[outputs.length-1][i];
            deltas[deltas.length-1][i] = calcD(a,ans[i]);
        }
        for(int i = layer.length-2;i>=0;i--){
            for(int j = 0;j<layer[i].length;j++){
                double b = outputs[i][j];
                b*=(1-b);
                double sum = 0;
                for(int g = 0;g<layer[i+1].length;g++){
                    double w = layer[i+1][g].getW(j);
                    w*=deltas[i+1][g];
                    sum+=w;
                }
                b*=sum;
                deltas[i][j] = b;
            }
        }
    }

    public double BG(int i, int j){

        return deltas[i][j];

    }

    public double WG(int i,int j, int g, double[] nums){
        if(i == 0){
            return nums[g]*deltas[i][j];
        }
        return outputs[i-1][g] * deltas[i][j];
    }

    public void train(double[] nums, double[] ans, double train){
        setD(nums,ans);
        for(int i = 0;i<layer.length;i++){
            for(int j = 0;j<layer[i].length;j++){
                for(int g = 0;g<layer[i][j].getNW();g++){
                    layer[i][j].changeW(g,-WG(i,j,g,nums)*train);

                }
                layer[i][j].changeB(-BG(i,j)*train);
            }
        }
    }

    public void train(double[][] nums, double[][] ans, double train){
        int[] order = new int[ans.length];
        for(int i = 0;i<ans.length;i++){
            order[i] = i;
        }
        shuffleArray(order);
        for(int i = 0;i<nums.length;i++){
            train(nums[order[i]],ans[order[i]],train);
        }
    }

    public static void shuffleArray(int[] array) {
        Random rand = new Random();

        // Loop backwards from the last element down to index 1
        for (int i = array.length - 1; i > 0; i--) {
            // Pick a random index from 0 to i (inclusive)
            int randomIndex = rand.nextInt(i + 1);

            // Swap array[i] with the element at the random index
            int temp = array[i];
            array[i] = array[randomIndex];
            array[randomIndex] = temp;
        }
    }
}