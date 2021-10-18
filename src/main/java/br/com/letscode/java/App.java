package br.com.letscode.java;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class App {

    private List<Pessoa> atores;

    public static void main(String[] args) {


        App app = new App(); App appAtor = new App(); App appAtris = new App();

        appAtor.prepararLeituraArquivoCsv("male.csv");
        appAtris.prepararLeituraArquivoCsv("female.csv");

        List<Pessoa> listaAtores = new ArrayList<>(appAtor.atores);
        List<Pessoa> listaAtrizes = new ArrayList<>(appAtris.atores);
        List<List<Pessoa>> listaTotal = List.of(listaAtores, listaAtrizes);

        String pessoa = "Leonardo DiCaprio";

        //Quem foi o ator mais jovem a ganhar um Oscar?
        System.out.println("O ator mais jovem a ganhar um Oscar foi: ");
        appAtor.findAtormaisjovem();

        //Quem foi a atriz que mais vezes foi premiada?
        System.out.println("\nA atriz que mais vezes foi premiada foi: ");
        appAtris.findAtrizmaispremiada();

        //Qual atriz entre 20 e 30 anos que mais vezes foi vencedora?
        System.out.println("\nA atriz entre 20 e 30 anos que foi vencedora mais vezes foi: ");
        appAtris.findAtrisMaispremiadapoIdade();

        //Quais atores ou atrizes receberam mais de um Oscar?
        System.out.println("\nOs atores ou atrizes receberam mais de um Oscar são: ");
        app.findAtorAtrizqueRecebeuMaisdeUmOscar(listaTotal);

        //Quando informado o nome de um ator ou atriz, dê um resumo
        // de quantos prêmios ele/ela recebeu e liste ano, idade e nome de cada filme pelo qual foi premiado(a).
        System.out.println("\nInformações de: "+ pessoa);
        app.findPessoaInformada(pessoa,listaTotal);

    }

    private void  findAtrizmaispremiada(){
        this.atores.stream()
                .collect(Collectors.groupingBy(Pessoa::getName, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .ifPresent(System.out::println);
    }

    private void findAtrisMaispremiadapoIdade() {
        this.atores
                .stream()
                .filter(p -> p.getAge() >= 20 && p.getAge() <= 30)
                .collect(Collectors.groupingBy(Pessoa::getName, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .ifPresent(System.out::println);
    }

    private void findAtormaisjovem(){
        this.atores.stream()
                .min(Comparator.comparingInt(Pessoa::getAge))
                .ifPresent(System.out::println);
    }

    private void findAtorAtrizqueRecebeuMaisdeUmOscar(List<List<Pessoa>> list) {
        Stream.of(list)
                .flatMap(List::stream)
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(Pessoa::getName, Collectors.counting()))
                .entrySet().stream().filter(e -> e.getValue() > 1)
                .collect(Collectors.toList())
                .stream()
                .map(Map.Entry::getKey)
                .forEach(System.out::println);
    }

    private void findPessoaInformada(String nome, List<List<Pessoa>> list){
        Stream.of(list)
                .flatMap(List::stream)
                .flatMap(List::stream)
                .filter(p -> p.getName().equals(nome))
                .forEach(System.out::println);

    }

    private void prepararLeituraArquivoCsv(String NomeArquivo) {
        String filepath = getFilepathFromResourceAsStream(NomeArquivo);
        try (Stream<String> lines = Files.lines(Path.of(filepath))) {
            this.atores = lines.skip(1)
                    .map(Pessoa::fromLine)
                    .toList();//Java 17

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFilepathFromResourceAsStream(String fileName) {
        URL url = getClass().getClassLoader().getResource(fileName);
        File file = new File(url.getFile());
        return file.getPath();
    }

}

