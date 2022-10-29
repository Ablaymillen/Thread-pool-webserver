package queuecore;

import static java.lang.Math.pow;

public class ServerFunctions {
    public int fibonacci(int n)  {
        if(n == 0)
            return 0;
        else if(n == 1)
            return 1;
        else
            return fibonacci(n - 1) + fibonacci(n - 2);
    }

    public String createFileName(String request){
        fibonacci(40);
        int i = 0;
        String s = request.split("create/")[1];
        String filename = "";
        while (s.charAt(i) != ' ' && s.charAt(i) != '\n'){
            filename += s.charAt(i);
            i++;
        }
        return filename;
    }

    public String deleteFileName(String request){
        fibonacci(40);
        int i = 0;
        String s = request.split("delete/")[1];
        String filename = "";
        while (s.charAt(i) != ' ' && s.charAt(i) != '\n'){
            filename += s.charAt(i);
            i++;
        }
        return filename;
    }
    public double[] square(String request){
        fibonacci(40);
        int i = 0;
        String s = request.split("square/")[1];
        String num = "";
        while (s.charAt(i) != ' ' && s.charAt(i) != '\n'){
            num += s.charAt(i);
            i++;
        }
        double[] res= new double[2];
        res[0] = Double.parseDouble(num);
        res[1] = pow(Double.parseDouble(num), 2);
        return res;
    }

    public double[] add(String request){
        fibonacci(40);
        int i = 0;
        String s = request.split("add/")[1];
        String num = "";
        while (s.charAt(i) != ' ' && s.charAt(i) != '\n'){
            num += s.charAt(i);
            i++;
        }
        double[] res= new double[3];
        res[0] = Double.parseDouble(num.split("\\+")[0]);
        res[1] = Double.parseDouble(num.split("\\+")[1]);
        res[2] = res[0] + res[1];
        return res;
    }
}
