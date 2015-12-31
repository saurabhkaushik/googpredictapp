package com.supersk.servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.datanucleus.sco.backed.Map;

import com.supersk.common.Constants;
import com.supersk.engine.PredictionEngine;

public class ConfigServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException, ServletException {
		
		java.util.Map mapl = req.getParameterMap(); 
		String[] values;
		if (mapl.containsKey("PROJECT_NAME")) {
			values = (String[]) mapl.get("PROJECT_NAME");
			Constants.PROJECT_NAME = values[0];
			Constants.APPLICATION_NAME = values[0];
		}
		if (mapl.containsKey("GCP_ACCOUNT_KEY")) {
			values = (String[]) mapl.get("GCP_ACCOUNT_KEY");
			Constants.GCP_ACCOUNT_KEY = values[0];
		}
		if (mapl.containsKey("GCP_ACCOUNT_ID")) {
			values = (String[]) mapl.get("GCP_ACCOUNT_ID");
			Constants.GCP_ACCOUNT_ID = values[0];
		}
		if (mapl.containsKey("GCP_STORAGE_BUCKET")) {
			values = (String[]) mapl.get("GCP_STORAGE_BUCKET");
			Constants.GCP_STORAGE_BUCKET = values[0];
		}
		if (mapl.containsKey("GCP_STORAGE_TRAINING_FILENAME")) {
			values = (String[]) mapl.get("GCP_STORAGE_TRAINING_FILENAME");
			Constants.GCP_STORAGE_TRAINING_FILENAME = values[0];
		}
		if (mapl.containsKey("GCP_STORAGE_TESTING_FILENAME")) {
			values = (String[]) mapl.get("GCP_STORAGE_TESTING_FILENAME");
			Constants.GCP_STORAGE_TESTING_FILENAME = values[0];
		}

		RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.jsp");
		rd.forward(req, resp);
	}
}
