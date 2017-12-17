package com.flchy.blog.inlets.service.impl;

import java.io.IOException;
import java.util.Date;

import org.csource.common.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.flchy.blog.base.response.ResponseCommand;
import com.flchy.blog.base.response.ResultPage;
import com.flchy.blog.common.fastdfs.FastDFSClient;
import com.flchy.blog.common.fastdfs.FastDSFile;
import com.flchy.blog.inlets.enums.StatusEnum;
import com.flchy.blog.inlets.mapper.ImagesMapper;
import com.flchy.blog.inlets.service.IImagesService;
import com.flchy.blog.pojo.Article;
import com.flchy.blog.pojo.Images;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author nieqs
 * @since 2017-12-03
 */
@Service
public class ImagesServiceImpl extends ServiceImpl<ImagesMapper, Images> implements IImagesService {
	@Autowired
	private FastDFSClient fastDFSClient;
	
	@Override
	public Images imageUpload(FastDSFile fastDSFile,String fileName) throws IOException, MyException{
		Images images=new Images();
		JSONArray upload = fastDFSClient.upload(fastDSFile);
		images.setImagePath( upload.get(0) + "/" + upload.get(1));
		images.setName(fileName);
		images.setGroupName(upload.getString(0));
		images.setRemoteFileName(upload.getString(1));
		images.setType(1);
		images.setCreateTime(new Date());
		try {
			boolean insert = insert(images);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return images;
	}
	
	@Override
	public Page<Images> selectImagePage(Integer current,Integer size, Images images) {
		Page<Images> page = new Page<>(Integer.valueOf(current), Integer.valueOf(size));
		selectPage(page, new EntityWrapper<Images>(images).where("status!={0}", StatusEnum.DELETE.getCode()));
		return page;
	}
	
	
	
}
