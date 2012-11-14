package net.stefanschramm.postaggerservlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class TaggerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final String defaultLanguage = "de";
	private Map<String, MaxentTagger> taggers = new HashMap<String, MaxentTagger>();

	public TaggerServlet() throws IOException, ClassNotFoundException {
		taggers.put(
				"de",
				new MaxentTagger(getClass().getResource(
						"/models/german-fast.tagger").getFile()));
		taggers.put(
				"en",
				new MaxentTagger(getClass().getResource(
						"/models/wsj-0-18-left3words.tagger").getFile()));
	}

	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws ServletException,
			IOException {

		MaxentTagger tagger;
		String language = httpServletRequest.getParameter("language");
		if (language == null || !taggers.containsKey(language)) {
			tagger = taggers.get(defaultLanguage);
		} else {
			tagger = taggers.get(language);
		}

		httpServletResponse.setContentType("text/javascript");
		PrintWriter out = httpServletResponse.getWriter();

		String taggedText = "";
		try {
			String text = httpServletRequest.getParameter("text");
			taggedText = tagger.tagString(text);
		} catch (Exception e) {
		}

		JSONObject response = new JSONObject();
		response.put("taggedText", taggedText);

		out.print("callback(" + response + ");");
		out.close();
	}
}
