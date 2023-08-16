/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.segdi.pide.consultas.ws;

import java.io.IOException;
import org.xml.sax.SAXException;

/**
 *
 * @author oti18
 */
public class NewMain1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SAXException, IOException {
        // TODO code application logic here
        WSConsultaRuc obj=new WSConsultaRuc();
        
        obj.consultaSunat("https://ws3.pide.gob.pe/Rest/Sunat/DatosPrincipales?", "10");
        
        
        
    }
    
}
