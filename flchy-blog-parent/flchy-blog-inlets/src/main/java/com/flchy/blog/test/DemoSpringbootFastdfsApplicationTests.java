package com.flchy.blog.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.alibaba.fastjson.JSONArray;


//@RunWith(SpringRunner.class)
//@SpringBootTest
public class DemoSpringbootFastdfsApplicationTests {

//	@Test
	public void contextLoads() {
		try {
			FileInputStream fis = new FileInputStream(new File("G://upload_source15_qy.jpg"));
			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			byte[] cache = new byte[4096];
			while (fis.read(cache) != -1) {
				bos.write(cache);
			}
			fis.close();
			FastDSFile fastDSFile = new FastDSFile();
			fastDSFile.setContent(bos.toByteArray());
			fastDSFile.setExt("jpg");

			// -------上传----
			JSONArray rs = FastDFSClient.upload(fastDSFile);
			System.out.println("上传结束:" + rs);

			// -------下载----
			byte[] dfile = FastDFSClient.download(rs.getString(0), rs.getString(1));

			FileOutputStream fos = new FileOutputStream(new File("C:/Users/xq/Pictures/tx-fdfs.jpg"));
			fos.write(dfile);
			fos.flush();
			fos.close();
			
			// -------删除-----
			int ds=FastDFSClient.delete(rs.getString(0), rs.getString(1));
			//
			System.out.println("Delete:"+ds);
			System.out.println("---End----");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}