package br.com.letscode.java;

import lombok.Value;

import static java.lang.Integer.parseInt;

@Value
public class Pessoa {
    int index;
    int year;
    int age;
    String name;
    String movie;


    public static Pessoa fromLine(String line) {
        String[] split = line.split("; ");
        return  new Pessoa(
                parseInt(split[0]),
                parseInt(split[1]),
                parseInt(split[2]),
                split[3],
                split[4]
        );

    }


}
