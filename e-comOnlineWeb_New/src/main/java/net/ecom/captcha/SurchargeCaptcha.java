/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.captcha;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.common.GeneralConf;

/**
 *
 * @author deepa.p
 */
@WebServlet(name = "SurchargeCaptcha", urlPatterns = {"/SurchargeCaptcha"})
public class SurchargeCaptcha extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        response.setContentType("image/jpeg");

        try {
            BufferedImage image = new BufferedImage(120, 50, BufferedImage.TYPE_BYTE_GRAY);
            Graphics2D graphics2D = image.createGraphics();
            String enable_uppercase = (String) request.getServletContext().getAttribute(GeneralConf.Enable_Uppercase_Captcha);
            String ch = "";
            if ("Y".equalsIgnoreCase(enable_uppercase)) {
                ch = CaptchaGenerator.generateCaptcha();
            } else {
                Random r = new Random();
                String token = Long.toString(Math.abs(r.nextLong()), 36);
                ch = token.substring(0, 5);
            }
            request.getSession().setAttribute("captchaforsurcharge", ch);
            GradientPaint gp = new GradientPaint(50, 25, Color.BLACK, 15, 25, Color.BLACK, false);
            graphics2D.setPaint(gp);
            Graphics2D g = image.createGraphics();
            g.setColor(Color.GRAY);
            g.fillRect(0, 0, image.getWidth(), image.getHeight());
            for (int i = 0; i < ch.length(); i++) {
                Font font = new Font("Verdana", Font.HANGING_BASELINE, 25);//20 + (int) (Math.random() * 11)
                graphics2D.setFont(font);

                graphics2D.drawString(ch.substring(i, i + 1), 5 + (i * 22), 30);

                font = null;
            }
            for (int i = 0; i < 5; i++) {
                graphics2D.drawLine(0, i * 10, 120, i * 10);
            }
            graphics2D.dispose();

            session.setAttribute("captchaforsurcharge", ch);
            request.setAttribute("captchaforsurcharge", ch);

            OutputStream outputStream = response.getOutputStream();
            ImageIO.write(image, "jpeg", outputStream);
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
