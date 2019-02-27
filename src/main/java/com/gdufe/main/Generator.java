package com.gdufe.main;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
public class Generator {

	
		/**
		 * mybatis逆向工程
		 * 根据配置生成mybatis的实体类、接口以及映射Mapper.xml文件
		 */

		    public void generator() throws Exception {

		        List<String> warnings = new ArrayList<String>();
		        boolean overwrite = true;
		        //指定 逆向工程配置文件  不知为什么相对路径不行
		        File configFile = new File("E:\\genetatorConfig.xml");
		        ConfigurationParser cp = new ConfigurationParser(warnings);
		        Configuration config = cp.parseConfiguration(configFile);
		        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
		        myBatisGenerator.generate(null);

		    }

		    public static void main(String[] args) throws Exception {
		        try {
		            Generator generatorSqlmap = new Generator();
		            generatorSqlmap.generator();
		        } catch (Exception e) {
		            e.printStackTrace();
		        }

		    }

		
}


