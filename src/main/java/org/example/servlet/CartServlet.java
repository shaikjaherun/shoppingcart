package org.example.servlet;

import java.io.*;
import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    public void doGet(HttpServletRequest req,
                      HttpServletResponse res)
            throws IOException, ServletException {
        req.getRequestDispatcher("/view_cart.jsp")
                .forward(req, res);
    }

    public void doPost(HttpServletRequest req,
                       HttpServletResponse res)
            throws IOException {

        HttpSession s = req.getSession();
        ArrayList<Integer> cart =
                (ArrayList<Integer>) s.getAttribute("cart");

        if (cart == null) {
            cart = new ArrayList<>();
            s.setAttribute("cart", cart);
        }

        cart.add(Integer.parseInt(req.getParameter("id")));
        res.sendRedirect("cart");
    }
}