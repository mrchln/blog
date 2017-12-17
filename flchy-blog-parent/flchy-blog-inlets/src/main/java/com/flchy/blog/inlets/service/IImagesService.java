package com.flchy.blog.inlets.service;

import java.io.IOException;

import org.csource.common.MyException;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.flchy.blog.common.fastdfs.FastDSFile;
import com.flchy.blog.pojo.Images;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author nieqs
 * @since 2017-12-03
 */
public interface IImagesService extends IService<Images> {


	Images imageUpload(FastDSFile fastDSFile, String fileName) throws IOException, MyException;

	Page selectImagePage(Integer current, Integer size, Images images);
	
}
