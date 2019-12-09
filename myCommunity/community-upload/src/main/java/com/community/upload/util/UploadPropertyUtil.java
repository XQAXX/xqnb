package com.community.upload.util;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class UploadPropertyUtil {
	private static Map<String, UploadPropertyUtil> instance = Collections.synchronizedMap(new HashMap<String, UploadPropertyUtil>());
	protected String sourceUrl;
	protected Properties properties;

	protected UploadPropertyUtil(String sourceUrl) {
		this.sourceUrl = sourceUrl;
		load();
	}

	public static UploadPropertyUtil getInstance(String sourceUrl) {
		synchronized (UploadPropertyUtil.class) {
			UploadPropertyUtil manager = instance.get(sourceUrl);
			if (manager == null) {
				manager = new UploadPropertyUtil(sourceUrl);
				instance.put(sourceUrl, manager);
			}
			return manager;
		}
	}

	private synchronized void load() {
		try {
			YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
			String path = sourceUrl + ".yml";
			String userDir = System.getProperty("user.dir");
			File sourceFile = new File(userDir+"/"+path);
			Resource resource = null;
			if(sourceFile.exists()&&sourceFile.isFile()){
				resource = new FileSystemResource(sourceFile);
			}else {
				resource = new ClassPathResource(path);
			}
			yaml.setResources(resource);
			this.properties = yaml.getObject();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("sourceUrl = " + this.sourceUrl+ "file load error!", e);
		}
	}

	public String getProperty(String key) {
		return this.properties.get(key).toString();
	}

}