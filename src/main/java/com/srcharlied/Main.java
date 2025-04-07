package com.srcharlied;

import java.util.PriorityQueue;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String filePath = "C:\\Users\\carlo\\com.srcharlied\\HT8\\src\\main\\java\\com\\srcharlied\\pacientes.txt";
        
        PriorityQueue<Paciente> queue = new PriorityQueue<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                
                String[] campos = linea.split(",\\s*"); 
                if (campos.length == 3) {
                    String nombre = campos[0];
                    String sintoma = campos[1];
                    char codigo = campos[2].charAt(0); 
                    Paciente paciente = new Paciente(nombre, sintoma, codigo);
                    queue.add(paciente); 
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            return;
        }

        System.out.println("Pacientes en orden de atención:");
        while (!queue.isEmpty()) {
            System.out.println(queue.poll()); 
            }

    }
}