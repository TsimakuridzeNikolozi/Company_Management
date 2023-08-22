package controllers;

import model.ReverseTreeNode;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/treeServlet")
public class TreeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<ReverseTreeNode> rootPeople = ReverseTreeNode.getRootPeople();
        request.setAttribute("data", rootPeople);
        request.getRequestDispatcher("/WEB-INF/tree.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("/WEB-INF/tree.jsp").forward(request, response);
    }
}
