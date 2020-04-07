import java.io.*;
import java.util.Random;
import weka.core.*;
import weka.classifiers.*;
import weka.classifiers.functions.MultilayerPerceptron;

public class Entrenador{
    public Entrenador(){ };

    //Cuantas veces se va a entrenar
    public static String entrenar(String neuronasCapas, int epocas, Float learningRate, Float momentum, int kfolds){
        String resultados = "";

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
            resultados += "\nErrorCuadrático: "+eval.errorRate();
            //System.out.println(eval.toSummaryString("\nResultados\n======\n", false));
            trainreader.close();
            testreader.close();
            resultados += "\n"+eval.toSummaryString("Resultados\n<------------------------->\n", false)+"\n\n";
        } catch(Exception ex){
            ex.printStackTrace();
        }

        //regresa los resultados
        return resultados;
    }

    public static String capas(int numeroCapas, int neuronas){
        String buffer = "";
        for (int i = 1; i <= numeroCapas; i++) {
            if (i == 1) {
                buffer += neuronas;
            }else{
                buffer += ","+neuronas;
            }
        }
        System.out.print(buffer+" ");
        return buffer;
    }
    //Main
    public static void main(String args[]){
        int mejoresNeuronas[] = {16,25,21,24,26,17,27,22,14,15};

        //Global es mejor 3 capas
        String mejoresCapas[] = {"16","25","27,27","17,17","27,27,27",
                                    "14,14,14","21,21,21,21",
                                    "27,27,27,27",
                                    "16,16,16,16,16",
                                    "25,25,25,25,25"};
        FileWriter fichero = null;
        try{
            fichero = new FileWriter("resultados.txt",true);
            PrintWriter pw  = new PrintWriter(fichero);
            
            //Calculo de Neuronas
            /*
            int min = 3, max = 27;
            for (int i = min; i <= max; i++) {
                String buffer = "";
                //+","+i
                String neuronasCapas = ""+i;
                int epocas = 200;
                Float learningRate = new Float(0.3);
                Float momentum = new Float(0.2);
                int kfolds = 5;//Numero de Validaciones Cruzadas
                
                buffer += "Neuronas y Capas:"+neuronasCapas+" Epocas:"+epocas+" LR:"+learningRate+" M:"+momentum+" KF:"+kfolds+" ";
                buffer += entrenar(neuronasCapas, epocas, learningRate, momentum, kfolds);
            
                //Imprime buffer
                pw.println( buffer );
            }
            */
           
            //Calculo de Capas
            /*
            int min = 1, max = 5;
            for (int neurona : mejoresNeuronas) {
                for (int i = min; i <= max; i++) {
                    String buffer = "";
                    //+","+i
                    String neuronasCapas = capas(i, neurona);
                    int epocas = 200;
                    Float learningRate = new Float(0.3);
                    Float momentum = new Float(0.2);
                    int kfolds = 5;//Numero de Validaciones Cruzadas
                    
                    buffer += "Neuronas y Capas:"+neuronasCapas+" Epocas:"+epocas+" LR:"+learningRate+" M:"+momentum+" KF:"+kfolds+" ";
                    buffer += entrenar(neuronasCapas, epocas, learningRate, momentum, kfolds);
                
                    //Imprime buffer
                    pw.println( buffer );
                }
            }
            */

            //Calculo de Epocas
            System.out.print("Epocas,Capas,ErrorCuadratico");
            int min = 150, max = 2500;
            for (String capa : mejoresCapas) {
                for (int i = min; i <= max; i=i+25) {
                    String buffer = "";
                    String neuronasCapas = capa;
                    int epocas = i;
                    Float learningRate = new Float(0.3);
                    Float momentum = new Float(0.2);
                    int kfolds = 5;//Numero de Validaciones Cruzadas
                    System.out.print(""+i+","+capa+",");
                    buffer += "Neuronas y Capas:"+neuronasCapas+" Epocas:"+epocas+" LR:"+learningRate+" M:"+momentum+" KF:"+kfolds+" ";
                    buffer += entrenar(neuronasCapas, epocas, learningRate, momentum, kfolds);
                
                    //Imprime buffer
                    pw.println( buffer );
                }
            }
            
            //Fin de escritura en archivo
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fichero)
                    fichero.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

}
