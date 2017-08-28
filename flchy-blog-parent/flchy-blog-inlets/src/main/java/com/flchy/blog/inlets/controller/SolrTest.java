package com.flchy.blog.inlets.controller;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@Path("solr")
//@Controller
//@Produces(MediaType.APPLICATION_JSON)
@RestController
@RequestMapping("solr")
public class SolrTest {
	@Autowired
	private SolrClient client;

	@GetMapping
	public String testSolr() throws IOException, SolrServerException {
		SolrDocument document = client.getById("test", "fe7a5124-d75b-40b2-93fe-5555512ea6d2");
		System.out.println(document);
		return document.toString();
	}

}
