package com.flchy.blog.inlets.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.flchy.blog.inlets.mapper.ImagesMapper;
import com.flchy.blog.inlets.service.IImagesService;
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
	
}
