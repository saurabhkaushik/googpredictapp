package com.supersk.servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.supersk.engine.PredictionEngine;

public class StatusServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException, ServletException {
		PredictionEngine predicEng = PredictionEngine.getInstance();
		if (predicEng.authenticate() == null) {
			resp.setContentType("text/plain");
			resp.getWriter().println("Authentication Failed");
			return;
		}
		String modelid = req.getParameter("modelid"); 
		if (modelid == null || modelid.equals("")) {
			req.setAttribute("message", "Invalid Model Id");
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/results.jsp");
			rd.forward(req, resp);
			return;
		}
		
		String status = null;
		try {
			status = predicEng.processStatusCheck(modelid);
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		req.setAttribute("message", "Training Status : " + status);
		req.setAttribute("modelid", modelid);
		req.setAttribute("results", "none");

		RequestDispatcher rd = getServletContext().getRequestDispatcher("/results.jsp");
		rd.forward(req, resp);
	}
}
