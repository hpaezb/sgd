package pe.gob.onpe.tramitedoc.web.servlet;

import java.io.IOException;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import javax.servlet.ServletContext;
import pe.gob.onpe.libreria.util.ServletUtility;
import pe.gob.onpe.tramitedoc.web.util.Util;

public class CaptchaGenServlet extends HttpServlet {

    public static final String FILE_TYPE = "jpeg";

    @Override
    public void init() throws ServletException {
        super.init();    
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException {
         response.setHeader("Cache-Control", "no-cache");
         response.setDateHeader("Expires", 0);
         response.setHeader("Pragma", "no-cache");
         response.setDateHeader("Max-Age", 0);
         int iTotalChars = 6;
         String captchaStr="";

         captchaStr=Util.generateCaptchaTextMethod2(iTotalChars);

         try {
             //int width=176;      int height=52;
             //Color bg = new Color(0,255,255);
             //Color fg = new Color(0,100,0);

             Font font = new Font("Arial", Font.BOLD, 36);
             //BufferedImage cpimg =new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

             /*
             Graphics g = cpimg.createGraphics();
             g.setFont(font);
             g.setColor(bg);
             g.fillRect(0, 0, width, height);
             g.setColor(fg);
             g.drawString(captchaStr,10,25);  */ 

             ServletContext sc = request.getSession().getServletContext();
             BufferedImage cpimg= ImageIO.read(new File(sc.getRealPath("/images/textura_3.jpg")));
             Random random = new Random(); 
             Graphics2D graphics2d = (Graphics2D) cpimg.getGraphics();
             int iCircle = 15;
             for (int i = 0; i < iCircle; i++) {
                 graphics2d.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
             }
             graphics2d.setFont(font);
             for (int i = 0; i < iTotalChars; i++) {
                 graphics2d.setColor(Color.WHITE);
                 //graphics2d.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                 if (i % 2 == 0) {
                     graphics2d.drawString(captchaStr.substring(i, i + 1), 25 * i + 15, 34);
                 } else {
                     graphics2d.drawString(captchaStr.substring(i, i + 1), 25 * i + 15, 45);
                 }
             }
             //HttpSession session = request.getSession(true);
             //session.setAttribute("CAPTCHA", captchaStr);
             ServletUtility.getInstancia().saveSessionAttribute(request, "CAPTCHA", captchaStr);
             //String sesionNew=(String) ServletUtility.getInstancia().loadSessionAttribute(request, "CAPTCHA");
             //System.out.println("CAPTCHA SERVLET 2: "+ sesionNew);
             OutputStream outputStream = response.getOutputStream();

             ImageIO.write(cpimg, FILE_TYPE, outputStream);
             outputStream.close();
         } catch (Exception e) {
                 e.printStackTrace();
         }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException {
        doPost(request, response);
    }
}
