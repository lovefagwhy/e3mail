package com.e3.protal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.e3.content.service.ContentService;
import com.e3.shop.common.ADItem;

@RestController
public class ContentController {
	@Autowired
	private ContentService contentService;

}
