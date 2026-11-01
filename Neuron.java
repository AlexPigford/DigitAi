public class Neuron{
    double[] weights;
    double b;
    public Neuron(int Nw){
        this(Nw,Math.random());
    }
    public Neuron(int Nw, double bias){
        weights = new double[Nw];
        for(int i = 0;i<weights.length;i++){
            weights[i] = Math.random()-.5;
        }
        b = bias;
    }
    public void setW(){
        for(int i = 0;i<weights.length;i++){
            weights[i] = Math.random()-.5;
        }
    }
    public int getNW(){
        return weights.length;
    }
    public void setW(double[] newWeights){
        weights = newWeights;
    }
    public void changeW(int i, double d){
        weights[i]+=d;
    }
    public double getW(int i){
        return weights[i];
    }
    public double getB(){
        return b;
    }
    public void changeB(double d){
        b+=d;
    }
    public double calculate(double[] nums){
        double sum = b;
        for(int i = 0;i<nums.length;i++){
            sum+=nums[i]*weights[i];
        }
        
        return 1/(1+Math.exp(-sum));
    }
    public void setWR(double range){
        for(int i = 0;i<weights.length;i++){
            weights[i] = 2*Math.random()*range-range;
        }
    }
}