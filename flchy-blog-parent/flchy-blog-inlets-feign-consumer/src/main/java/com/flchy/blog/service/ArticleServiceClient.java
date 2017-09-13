package com.flchy.blog.service;

import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient("flchy-blog-inlets-feign-client")
public interface ArticleServiceClient extends ArticleService {
}
