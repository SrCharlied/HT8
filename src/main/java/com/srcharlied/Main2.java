package com.srcharlied;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main2 {
    public static void main(String[] args) {
        String filePath = "C:\\Users\\carlo\\com.srcharlied\\HT8\\src\\main\\java\\com\\srcharlied\\pacientes.txt";

        VectorHeap<Paciente> queue = new VectorHeap<>();

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

        System.out.println("Orden de atención de pacientes:");
        while (!queue.isEmpty()) {
            Paciente paciente = queue.remove();
            System.out.println(paciente);
        }
    }
}