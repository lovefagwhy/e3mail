package com.e3.shop.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.e3.shop.common.FastDFSClient;
import com.e3.shop.common.JsonUtils;
import com.e3.shop.common.PictureResult;

@Controller
public class PictureController {
	@Value("${TRACKER_SERVER}")
	private String tracker_server;
	@RequestMapping("/pic/upload")
	@ResponseBody
	public String  fileUpload(MultipartFile uploadFile){
		try {
			String filename = uploadFile.getOriginalFilename();
			String ext = filename.substring(filename.lastIndexOf(".")+1);
			FastDFSClient client = new FastDFSClient("classpath:fdfs_client.conf");
			String path = client.uploadFile(uploadFile.getBytes(), ext, null);
			PictureResult result = new PictureResult(0, tracker_server+"/"+path);
			String json = JsonUtils.objectToJson(result);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			//5、返回map
			PictureResult result = new PictureResult(1, null, "图片上传失败");
			String json = JsonUtils.objectToJson(result);
			return json;
		}
	}
}
