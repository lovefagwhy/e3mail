package com.e3.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.e3.shop.pojo.TbItem;
import com.e3.shop.pojo.TbItemDesc;
import com.e3.shop.service.ItemService;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateNotFoundException;

public class ItemDetailListener implements MessageListener {
	@Autowired
	private ItemService itemService;
	@Autowired
	private FreeMarkerConfigurer freeMarKerConfigurer;
	@Value("${HTML_PATH}")
	private String HTML_PATH;
	@Override
	public void onMessage(Message message) {
		Long itemId = null;
		if(message instanceof TextMessage){
			TextMessage tm = (TextMessage)message;
			try {
				itemId = Long.parseLong(tm.getText());
				Thread.sleep(3000);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Configuration configuration = freeMarKerConfigurer.getConfiguration();
		try {
			Template template = configuration.getTemplate("item.ftl");
			Map<String,Object> map = new HashMap<>();
			TbItem tbItem = itemService.findItemByID(itemId);
			TbItemDesc tbItemDesc = itemService.findItemDescByID(itemId);
			map.put("item",tbItem );
			map.put("itemDesc",tbItemDesc);
			Writer writer = new FileWriter(new File(HTML_PATH+itemId+".html"));
			template.process(map, writer);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
