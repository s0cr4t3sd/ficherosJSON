/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ficherosjson;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 *
 * @author cronida
 */


public class Main {
    
    private static void leerFichero(String ruta, ArrayList<String> lista){
        String linea;
        ArrayList<String> listaTxt = new ArrayList<>();
        String[] tokens;
        try (Scanner datosFichero = new Scanner(new FileReader(ruta))){
            
            while (datosFichero.hasNextLine()) {
                linea = datosFichero.nextLine();
                listaTxt.add(linea);
            }
            if(listaTxt.get(0).split("},").length > 0){
                for (int i = 0; i < listaTxt.get(0).split("},").length; i++) {
                    tokens =listaTxt.get(0).split("},");
                    if(i != (tokens.length-1)){
                        lista.add(tokens[i]+"},");
                    }else{
                        lista.add(tokens[i]);
                    }
                }
            }else{
                lista.add(listaTxt.get(0));
            }
                
            
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } 
        
    }
   
    private static void crearFicheroApp(ArrayList<App> lista){
        for(App i : lista){
            crearFicheroJSON("aplicaciones/"+i.getNombre()+".json", i);
        }
    }
    
    private static void crearFicheroJSON(String ruta, App app){        
        try{
            ObjectMapper objMap = new ObjectMapper();
            objMap.writeValue(new File(ruta), app);
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    private static ArrayList<App> pasoJSONaOBJS(String ruta){  
        final ObjectMapper JSON_MAPPER = new ObjectMapper();
        try{
            ArrayList<App> lista = new ArrayList<App>();
            lista = JSON_MAPPER.readValue(new File(ruta),
                    JSON_MAPPER.getTypeFactory().constructCollectionType(ArrayList.class, App.class));
            return lista;
        }catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private static App pasoJSONaOBJ(String ruta){  
        final ObjectMapper objMap = new ObjectMapper();
        try{           
          App app = objMap.readValue(new File(ruta), App.class);
          return app;
        }catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private static void crearDirectorio(String ruta){
        Path file = Paths.get(ruta);
        try{
            if(!Files.exists(file)){
                Files.createDirectory(file);   
            }else{
                System.out.println("El directorio "+ruta+" ya existe");
            }            
        }catch(IOException e) {
            System.out.println(e.toString());
        }
    }
    
    private static void eliminarContenidoDir(String ruta){
         File f = new File(ruta);
        if (f.exists()) {
            File[] ficheros = f.listFiles();
            for (File i : ficheros) {
                i.delete();
            }
        } else {
            System.out.println("No existe el directorio " + ruta);
        }
    }
    
    private static void contenidoDirectorio(String ruta, ArrayList<String> lista) {
        File f = new File(ruta);
        if (f.exists()) {
            File[] ficheros = f.listFiles();
            for (File i : ficheros) {
                lista.add(i.getName());
                System.out.println(i.getName());
            }
        } else {
            System.out.println("No existe el directorio " + ruta);
        }
    }
    
    public static void main(String[] args) throws IOException {
        ArrayList<App> lista = new ArrayList<App>();
        Scanner scn = new Scanner(System.in);
        
        //CREO LAS 50 APPS
        for (int i = 0; i < 50; i++) {
            lista.add(new App());
        }
        
        //BORRO EL CONTENIDO DEL DIRECTORIO
        eliminarContenidoDir("aplicaciones");
        
        //CREO EL DIRECTORIO APLICACIONES PARA ALMACENAR CADA ARCHIVO JSON
        crearDirectorio("aplicaciones");
        
        //CREO CADA FICHERO JSON
        crearFicheroApp(lista);
       
        //CREO UN DIRECTORIO PARA GUARDAR TODA LA INFORMACION DE LA APP 
        crearDirectorio("ficheroJSON");
        
        //GUARDO TODAS LAS APP EN UN SOLO FICHERO DE JSON
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File("ficheroJSON/app.json"), lista);
        
        //LEO EL FICHERO JSON Y LO PASO A OBJETOS EN UNA LISTA DE TIPO APP
        ArrayList<App> leeApp = pasoJSONaOBJS("ficheroJSON/app.json");       
        //LO MUESTRO POR PANTALLA 
        System.out.println("");
        System.out.println("Paso de JSON a objetos App lo que hay en ficherosJSON/app.json");
        for(App i : leeApp){
            System.out.println(i);
        }
        System.out.println("");
        
        System.out.println("Muestro el contenido del fichero aplicaciones/");
        ArrayList<String> listaFicheros = new ArrayList<>();
        contenidoDirectorio("aplicaciones", listaFicheros);
        String opcFichero="";
        System.out.println("");
        boolean seguir = true;
        while (seguir) {
            System.out.println("Dime que fichero quieres pasar de json a obj tipo app?");
            opcFichero = scn.nextLine();
            System.out.println("");
            seguir = !listaFicheros.contains(opcFichero);
        }
        
        //LEO EL FICHERO Y PASO EL JSON A OBJ
        App ap = pasoJSONaOBJ("aplicaciones/"+opcFichero);
        System.out.println("Aqui el objeto del archivo "+opcFichero+":");
        System.out.println(ap);
        System.out.println("");
        
        //AQUI LEO PINTO LOS FICHEROS TAL Y COMO ESTAN GUARDADOS
        ArrayList<String> listaTxt = new ArrayList<String>();        
        leerFichero("ficheroJSON/app.json", listaTxt);
        
        System.out.println("Muestro lo que se ve en el fichero ficheroJSON/app.json:");
        for(String i : listaTxt){
            System.out.println(i);
        }
        listaTxt.clear();
        System.out.println("");
       
        
        leerFichero("aplicaciones/"+opcFichero, listaTxt);
        System.out.println("Muestro lo que se ve en el fichero aplicaciones/"+opcFichero+":");
        for(String i : listaTxt){
            System.out.println(i);
        }
        listaTxt.clear();
        System.out.println("");
    }
   
}
