import java.io.*;
import java.util.Random;
import weka.core.*;
import weka.core.Instances;
import weka.classifiers.Evaluation;
import weka.classifiers.*;
import weka.classifiers.functions.MultilayerPerceptron;

public class Entrenador{
    private String neuronasCapas;
    private int epocas;
    private Float learningRate;
    private Float momentum;
    private int kfolds;

    public Entrenador(String neuronasCapas, int epocas, Float learningRate, Float momentum, int kfolds){
        this.neuronasCapas = neuronasCapas;
        this.epocas = epocas;
        this.learningRate = learningRate;
        this.momentum = momentum;
        this.kfolds = kfolds;
    }

    public String entrenar(){
        String resultados = null;
        try{
            FileReader trainreader = new FileReader("ForestFireNormalizado.arff");
            FileReader testreader = new FileReader("ForestFireNormalizadoTest.arff");

            Instances train = new Instances(trainreader);
            Instances test = new Instances(testreader);
            train.setClassIndex(train.numAttributes() - 1);
            test.setClassIndex(test.numAttributes() - 1);

            MultilayerPerceptron mlp = new MultilayerPerceptron();
            //mlp.setOptions(Utils.splitOptions("-L 0.3 -M 0.2 -N 500 -V 0 -S 0 -E 20 -H 4"));
            
            //Setting Parameters
            mlp.setHiddenLayers( neuronasCapas );//Neuronas, Capas
            mlp.setTrainingTime( epocas );//Epocas
            mlp.setLearningRate( learningRate );//LearningRate
            mlp.setMomentum( momentum );//Momentum

            //Se pone a entrenar
            mlp.buildClassifier(train);

            //Evalua resultados
            
            Evaluation eval = new Evaluation(train);

            eval.crossValidateModel(mlp, train, kfolds, new Random(1));

            System.out.println(eval.errorRate()); //Error cuadrático Medio
            //System.out.println(eval.toSummaryString("\nResultados\n======\n", false));
            trainreader.close();
            testreader.close();
            resultados = ""+eval.toSummaryString("\nResultados\n======\n", false);
        } catch(Exception ex){
            ex.printStackTrace();
        }

        //regresa los resultados
        return resultados;
    }

    //Main
    public static void main(String args[]){
        String neuronasCapas = "3,3";
        int epocas = 200;
        Float learningRate = 0.3;
        Float momentum = 0.2;
        int kfolds = 5;//Nume ro de validaciones Cruzadas
        
        Entrenador training = new Entrenador(neuronasCapas,epocas,learningRate,momentum,kfolds);
        System.out.println(training.entrenar());
    }

}
