package com.flchy.blog.inlets.config;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
/**
 * 
 * @author flchy
 *
 */
@Configuration
@EnableSolrRepositories(basePackages = { "com.solr" }, multicoreSupport = true)
public class SolrConfig {

	@Value("${spring.data.solr.host}")
	private String url;

	@Bean
	public SolrClient solrClient() {
		return new HttpSolrClient(url);
	}

	@Bean
	public SolrTemplate solrTemplate() throws Exception {
		SolrTemplate solrTemplate = new SolrTemplate(solrClient());
//		System.out.println("1111111111");
		// solrTemplate.setSolrConverter(solrConverter);
		return solrTemplate;
	}
}
