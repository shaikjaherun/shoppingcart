package org.example.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.example.util.DBConnection;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req,
                       HttpServletResponse res)
            throws IOException, ServletException {

        HttpSession s = req.getSession();

        ArrayList<Integer> cart =
                (ArrayList<Integer>) s.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            res.getWriter().println("Cart is empty");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try {
                for (int id : cart) {

                    PreparedStatement ps = con.prepareStatement(
                            "SELECT * FROM products WHERE id=? AND qty>0");

                    ps.setInt(1, id);
                    ResultSet rs = ps.executeQuery();

                    if (!rs.next()) {
                        rs.close();
                        ps.close();
                        throw new Exception("Out of stock");
                    }

                    String name = rs.getString("name");
                    double price = rs.getDouble("price");

                    rs.close();
                    ps.close();

                    ps = con.prepareStatement(
                            "INSERT INTO orders VALUES(?,?,?)");

                    ps.setInt(1, id);
                    ps.setString(2, name);
                    ps.setDouble(3, price);
                    ps.executeUpdate();
                    ps.close();

                    ps = con.prepareStatement(
                            "UPDATE products SET qty=qty-1 WHERE id=? AND qty>0");

                    ps.setInt(1, id);

                    if (ps.executeUpdate() != 1) {
                        ps.close();
                        throw new Exception("Out of stock");
                    }

                    ps.close();
                }

                con.commit();
                s.removeAttribute("cart");

                res.setContentType("text/html");
                res.getWriter().println("Checkout successful!");

            } catch (Exception e) {
                con.rollback();
                throw e;
            }

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
