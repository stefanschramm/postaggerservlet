package net.stefanschramm.postaggerservlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class TaggerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private MaxentTagger tagger;

	public TaggerServlet() throws IOException, ClassNotFoundException {
		String taggerFile = getClass().getResource("/models/german-fast.tagger")
				.getFile();
		tagger = new MaxentTagger(taggerFile);
	}

	@SuppressWarnings({ "static-access", "unchecked" })
	protected void doGet(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws ServletException,
			IOException {

		httpServletResponse.setContentType("text/javascript");
		PrintWriter out = httpServletResponse.getWriter();

		String taggedText = "";
		try {
			String text = httpServletRequest.getParameter("text");
			taggedText = tagger.tagString(text);
		} catch (Exception e) {
		}

		// JSONObject response = new JSONObject();
		// response.put("taggedText", taggedText);
		out.print(taggedText);

		// out.print("callback(" + response + ");");
		out.close();
	}
}
