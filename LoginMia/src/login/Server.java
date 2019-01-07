/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

import java.awt.List;

/**
 *
 * @author maggioni_matteo00
 */
public class Server {

    //private static List listaUtentiRegistrati = new List();
    public static void main(String args[]) throws Exception {
        TCPServer serverss = new TCPServer();
        System.out.println("inizio server");

        while (true) {
            System.out.println("-----inizio ciclo-----");
            String leggi = File.LeggiDaFile();
            String[] s = leggi.split(";");
            serverss.GestioneSocketServer();
            String ricevuto = serverss.riceviServer();

            String[] ComandoRicevuto = splitmex(ricevuto);

            if (ComandoRicevuto[0].equals("LOGIN")) {
                String user = ComandoRicevuto[1];
                String Password = ComandoRicevuto[2];
                boolean trovato = false;
                boolean bloccato = false;
                boolean temporaneo = false;

                for (int i = 0; i < s.length; i++) {
                    String[] parz = s[i].split(",");
                    String us = parz[0];
                    String pw = parz[1];

                    if (us.equals(user) && pw.equals(Password)) {
                        if(parz[8].equals("no")){
                            trovato = true;
                        }else if(parz[8].equals("Si")){
                            bloccato=true;
                        }else{
                            bloccato=true;
                        }                     
                    }
                }
                if (trovato == true) {
                    serverss.inviaServer("OK");
                } else if(bloccato==true){
                    serverss.inviaServer("BLOCCATO");
                }else {
                    serverss.inviaServer("ERROR");
                }

            } else if (ComandoRicevuto[0].equals("REGISTRAZIONE")) {
                //REGISTRAZIONE,username,password,cognome,nome,anno,classe,specializzazione,amministratore;
                String user = ComandoRicevuto[1];
                boolean trovato = false;

                for (int i = 0; i < s.length; i++) {
                    String[] parz = s[i].split(",");
                    String us = parz[0];
                    String pw = parz[1];

                    if (us.equals(user)) {
                        trovato = true;
                    }
                }
                if (trovato == true) {
                    serverss.inviaServer("GIA REGISTRATO CON QUESTO USERNAME");
                } else {
                    //NON ESISTO MI REGISTRO
                    String dascrivere = ComandoRicevuto[1] + "," + ComandoRicevuto[2]
                            + "," + ComandoRicevuto[3] + "," + ComandoRicevuto[4] + "," + ComandoRicevuto[5]
                            + "," + ComandoRicevuto[6] + "," + ComandoRicevuto[7] + "," + ComandoRicevuto[8] + "," + ComandoRicevuto[9] + ";";
                    File.SalvaSuFile(dascrivere);
                    serverss.inviaServer("OK");
                }              

            } else if (ComandoRicevuto[0].equals("MODIFICA_INFO")) {
                //String invio = "MODIFICA_INFO," +UserVecchio+","+Password+","+ User + "," + cognome + ","
                //+ nome + "," + anno + "," + classe + "," + specializzazione + "," + amministratore;
                String user = ComandoRicevuto[1];
                String Password = ComandoRicevuto[2];
                boolean trovato = false;
                String pot = "";

                for (int i = 0; i < s.length; i++) {
                    String[] parz = s[i].split(",");
                    String us = parz[0];
                    String pw = parz[1];

                    if (us.equals(user) && pw.equals(Password)) {
                        System.out.println("trovato");
                        String dascrivere = ComandoRicevuto[3] + "," + pw
                                + "," + ComandoRicevuto[4] + "," + ComandoRicevuto[5] + "," + ComandoRicevuto[6]
                                + "," + ComandoRicevuto[7] + "," + ComandoRicevuto[8] + "," + ComandoRicevuto[9] + "," + parz[8] + ";";
                        pot += dascrivere;
                        trovato = true;
                    } else {
                        pot += s[i] + ";";
                    }
                }
                if (trovato == true) {
                    serverss.inviaServer("OK");
                } else {
                    serverss.inviaServer("ERROR");
                }

                File.SovrascriviFile(pot);
            } else if (ComandoRicevuto[0].equals("MODIFICA_PASSWORD")) {
//                String invio = "MODIFICA_PASSWORD," +UserVecchio+","+Password+","+ psw1 + "," + pswnew1 + ","
//                + pswnew2 ;
                String user = ComandoRicevuto[1];
                String Password = ComandoRicevuto[2];
                boolean trovato = false;
                String pot = "";

                if (Password.equals(ComandoRicevuto[3]) && ComandoRicevuto[4].equals(ComandoRicevuto[5])) {
                    for (int i = 0; i < s.length; i++) {
                        String[] parz = s[i].split(",");
                        String us = parz[0];
                        String pw = parz[1];

                        if (us.equals(user) && pw.equals(Password)) {
                            System.out.println("trovato");
                            String dascrivere = parz[0] + "," + ComandoRicevuto[4]
                                    + "," + parz[2] + "," + parz[3] + "," + parz[4]
                                    + "," + parz[5] + "," + parz[6] + "," + parz[7] + parz[8]+";";
                            pot += dascrivere;
                            trovato = true;
                        } else {
                            pot += s[i] + ";";
                        }
                    }
                    if (trovato == true) {
                        serverss.inviaServer("OK");
                    } else {
                        serverss.inviaServer("ERROR");
                    }
                    File.SovrascriviFile(pot);
                } else {
                    serverss.inviaServer("ERROR");
                }
            }else if (ComandoRicevuto[0].equals("GET_ALL")) {
                //solo l'utente amministratore può ottenere l'elenco di 
                //tutti gli username degli utenti 
                
                //String invio = "GET_ALL," +user+","+password;
                String user = ComandoRicevuto[1];
                String password = ComandoRicevuto[2];
                boolean trovato = false;
                boolean amm =false;
                String invio="";

                for (int i = 0; i < s.length; i++) {
                    String[] parz = s[i].split(",");
                    String us = parz[0];
                    String pw = parz[1];

                    if (us.equals(user)&&pw.equals(password)) {
                        trovato = true;
                        if(parz[7].equals("Si")){
                            amm=true;
                        }
                    }
                }
                if (trovato == true&&amm==true) {
                    for (int i = 0; i < s.length; i++) {
                    String[] parz = s[i].split(",");
                    String us = parz[0];
                    invio+=us+",";
                }
                    serverss.inviaServer(invio);
                } else {                    
                    serverss.inviaServer("ERROR");
                }                   
            }else if (ComandoRicevuto[0].equals("BLOCCOTEMPORANEO")) {
                //TEMPORANEO
                //invio = "BLOCCOTEMPORANEO," + userdabloccare + "," + tempo + user + "," + password;
                
                String user = ComandoRicevuto[3];
                String password = ComandoRicevuto[4];
                boolean trovato = false;
                boolean amm =false;
                String pot = "";
                

                for (int i = 0; i < s.length; i++) {
                    String[] parz = s[i].split(",");
                    String us = parz[0];
                    String pw = parz[1];

                    if (us.equals(user)&&pw.equals(password)) {
                        trovato = true;
                        if(parz[7].equals("Si")){
                            amm=true;
                        }
                    }
                }
                if (trovato == true&&amm==true) {
                    for (int i = 0; i < s.length; i++) {
                    String[] parz = s[i].split(",");
                    String us = parz[0];
                    
                    if (us.equals(ComandoRicevuto[1])) {
                        String dascrivere = parz[0] + "," + parz[1]
                                    + "," + parz[2] + "," + parz[3] + "," + parz[4]
                                    + "," + parz[5] + "," + parz[6] + "," + parz[7] + "," + ComandoRicevuto[2] + ";";
                            pot += dascrivere;
                    }else {
                            pot += s[i] + ";";
                        }
                    
                }
                    File.SovrascriviFile(pot);
                    serverss.inviaServer("OK");
                } else {                    
                    serverss.inviaServer("ERROR");
                }  
                
        
                
            }else if (ComandoRicevuto[0].equals("BLOCCODEFINITIVO")) {
                //PERMANENTE
                //invio = "BLOCCODEFINITIVO," + userdabloccare + user + "," + password;
                String user = ComandoRicevuto[2];
                String password = ComandoRicevuto[3];
                boolean trovato = false;
                boolean amm =false;  
                String pot = "";

                for (int i = 0; i < s.length; i++) {
                    String[] parz = s[i].split(",");
                    String us = parz[0];
                    String pw = parz[1];

                    if (us.equals(user)&&pw.equals(password)) {
                        trovato = true;
                        if(parz[7].equals("Si")){
                            amm=true;
                        }
                    }
                }
                if (trovato == true&&amm==true) {
                    for (int i = 0; i < s.length; i++) {
                    String[] parz = s[i].split(",");
                    String us = parz[0];
                    
                    if (us.equals(ComandoRicevuto[1])) {
                        String dascrivere = parz[0] + "," + parz[1]
                                    + "," + parz[2] + "," + parz[3] + "," + parz[4]
                                    + "," + parz[5] + "," + parz[6] + "," + parz[7] + "," + "Si" + ";";
                            pot += dascrivere;
                    }else {
                            pot += s[i] + ";";
                        }
                    
                }
                    File.SovrascriviFile(pot);
                    serverss.inviaServer("OK");
                } else {                    
                    serverss.inviaServer("ERROR");
                }  
            }
            //else if            
            serverss.chiudiServer();
        }

    }

    public static String[] splitmex(String messaggio) {
        String str = messaggio;
        String[] arrOfStr = str.split(",");
        return arrOfStr;
    }

}
