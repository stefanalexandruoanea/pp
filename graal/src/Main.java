//import libraria principala polyglot din graalvm
import org.graalvm.polyglot.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import org.graalvm.polyglot.*;
//clasa principala - aplicatie JAVA
class Polyglot {
    //metoda privata pentru conversie low-case -> up-case folosind functia toupper() din R
    private static String RToUpper(String token){
        //construim un context care ne permite sa folosim elemente din R
        Context polyglot = Context.newBuilder().allowAllAccess(true).build();
        //folosim o variabila generica care va captura rezultatul excutiei funcitiei R, toupper(String)
        //pentru aexecuta instructiunea I din limbajul X, folosim functia graalvm polyglot.eval("X", "I");
        Value result = polyglot.eval("R", "toupper(\""+token+"\");");
        //utilizam metoda asString() din variabila incarcata cu output-ul executiei pentru a mapa valoarea generica la un String
        String resultString = result.asString();
        // inchidem contextul Polyglot
        polyglot.close();

        return resultString;
    }


    //metoda privata pentru evaluarea unei sume de control simple a literelor unui text ASCII, folosind PYTHON
    private static int SumPolynomial(String token){
        //construct a Polyglot context that allows us to use elements from Python
        Context polyglot = Context.newBuilder().allowAllAccess(true).build();
        //use a generic variable that will capture the result of executing the polynomial formula in Python
        //we can replace some elements of the script we're building for evaluation, here the token comes from Java but will be interpreted by Python
        Value result = polyglot.eval("python", "sum([ord(ch)*(i**5) for i, ch in enumerate('" + token.substring(1, token.length()-1) + "')])");
        //use the asInt() method from the variable loaded with the execution output to map the generic value to an int
        int resultInt = result.asInt();
        //close the Polyglot context
        polyglot.close();

        return resultInt;
    }

    private static int random20list(){
        Context polyglot = Context.newBuilder().allowAllAccess(true).build();
        polyglot.eval("python","import random");
        Value result = polyglot.eval("python", "random.randint(0, 20)");
        int resultInt = result.asInt();
        polyglot.close();
        return resultInt;
    }
    public static void main(String[] args) {
        //construim un context pentru evaluare elemente JS
        Context polyglot = Context.create();
        //construim un array de string-uri, folosind cuvinte din pagina web:  https://chrisseaton.com/truffleruby/tenthings/
        Value array = polyglot.eval("js", "[\"If\",\"we\",\"run\",\"the\",\"java\",\"command\",\"included\",\"in\"," +
                "\"GraalVM\",\"we\",\"will\",\"be\",\"automatically\",\"using\",\"the\",\"Graal\",\"JIT\",\"compiler\",\"no\",\"extra\",\"configuration\",\"is\"," +
                "\"needed\",\"I\",\"will\",\"use\",\"the\",\"time\",\"command\",\"to\",\"get\",\"the\",\"real\",\"wall\",\"clock\",\"elapsed\",\"time\",\"it\",\"takes\"," +
                "\"to\",\"run\",\"the\",\"entire\",\"program\",\"from\",\"start\",\"to\",\"finish\",\"rather\",\"than\",\"setting\",\"up\",\"a\",\"complicated\",\"micro\"," +
                "\"benchmark\",\"and\",\"I\",\"will\",\"use\",\"a\",\"large\",\"input\",\"so\",\"that\",\"we\",\"arent\",\"quibbling\",\"about\",\"a\",\"few\",\"seconds\"," +
                "\"here\",\"or\",\"there\",\"The\",\"large.txt\",\"file\",\"is\",\"150\",\"MB\"];");
        //pentru fiecare cuvant, convertim la upcase folosind R si calculam suma de control folosind PYTHON
        for (int i = 0; i < array.getArraySize();i++){
            String element = array.getArrayElement(i).asString();
            String upper = RToUpper(element);
            int crc = SumPolynomial(upper);
            System.out.println(upper + " -> " + crc);
        }
        // inchidem contextul Polyglot
        polyglot.close();
    }
    public class LinearRegression {
        public  void main(String[] args) throws IOException {
            String datasetFile = "dataset.txt";
            String dataset = new String(Files.readAllBytes(Paths.get(datasetFile)));

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter output file name (without extension): ");
            String outputFile = scanner.nextLine();
            System.out.print("Enter output path: ");
            String outputPath = scanner.nextLine();
            System.out.print("Enter plot colors (in R format): ");
            String plotColors = scanner.nextLine();

            Context polyglot = Context.newBuilder().allowAllAccess(true).build();

            String rCode = String.format(
                    "library(lattice);\n" +
                            "data <- read.csv(text=\"%s\", header=TRUE);\n" +
                            "fit <- lm(y ~ x, data=data);\n" +
                            "png(file=\"%s/%s.png\");\n" +
                            "xyplot(y ~ x, data=data, type='p', col='%s');\n" +
                            "abline(fit, col='%s');\n" +
                            "dev.off();\n",
                    dataset, outputPath, outputFile, plotColors, plotColors
            );

            // Evaluate the R code in the Polyglot context
            polyglot.eval("R", rCode);

            // Close the Polyglot context
            polyglot.close();

            // Open the output file using the system's default program
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                Runtime.getRuntime().exec("cmd /c start " + outputPath + "/" + outputFile + ".png");
            } else if (os.contains("mac")) {
                Runtime.getRuntime().exec("open " + outputPath + "/" + outputFile + ".png");
            } else {
                Runtime.getRuntime().exec("xdg-open " + outputPath + "/" + outputFile + ".png");
            }
        }
    }
}

