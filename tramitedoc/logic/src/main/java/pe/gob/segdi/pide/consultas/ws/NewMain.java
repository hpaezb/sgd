/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.segdi.pide.consultas.ws;

import pe.gob.onpe.libreria.util.ConstantesSec;
import pe.gob.onpe.libreria.util.Utility;

/**
 *
 * @author mvaldera
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        /*
        String pass=Utility.getInstancia().cifrar("20161704378",ConstantesSec.SGD_SECRET_KEY_PROPERTIES);
        System.out.println("pass==>"+pass);
        String pass2=Utility.getInstancia().descifrar("B6C0958243D537A32C4F818AD13D2A1F",ConstantesSec.SGD_SECRET_KEY_PROPERTIES);
        System.out.println("pass2==>"+pass2);  */
        
        String pass3=Utility.getInstancia().cifrar("Demo2022",ConstantesSec.SGD_SECRET_KEY_PASSWORD);
        System.out.println("pass==>"+pass3);
        String pass4=Utility.getInstancia().descifrar("FFB380D3C7D47D180349E3014D06C48E",ConstantesSec.SGD_SECRET_KEY_PASSWORD);
        System.out.println("pass2==>"+pass4);
        String pass5=Utility.getInstancia().descifrar("FC0E279628B80EAC966E7BC736227547",ConstantesSec.SGD_SECRET_KEY_PASSWORD);
        System.out.println("pas2s2==>"+pass5);
         
    }
    
}
