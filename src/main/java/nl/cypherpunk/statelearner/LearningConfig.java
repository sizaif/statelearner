/*
 *  Copyright (c) 2016 Joeri de Ruiter
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package nl.cypherpunk.statelearner;

import com.google.common.io.Files;

import java.io.*;
import java.util.*;

/**
 * Configuration class used for learning parameters
 * 
 * @author Joeri de Ruiter (joeri@cs.ru.nl)
 */
public class LearningConfig {
	static int TYPE_SMARTCARD = 1;
	static int TYPE_SOCKET = 2;
	static int TYPE_TLS = 3;

	private static String ARGS_FILE = "command.args";
	protected Properties properties;
	
	int type = TYPE_SMARTCARD;
	
	String output_dir = "output";
	
	String learning_algorithm = "lstar";
	String eqtest = "randomwords";
	
	// Used for W-Method and Wp-method
	int max_depth = 10;
	
	// Used for Random words
	int min_length = 5;
	int max_length = 10;
	int nr_queries = 100;
	int seed = 1;
	Map<String,String> mapargs = new HashMap<String,String>();
	public LearningConfig(String filename) throws IOException {
		properties = new Properties();

		InputStream input = new FileInputStream(filename);
		properties.load(input);

		loadProperties();
		//copyArgsToOutDir(mapargs,output_dir);
	}
	
	public LearningConfig(LearningConfig config) throws IOException {
		properties = config.getProperties();


		loadProperties();
		//copyArgsToOutDir(mapargs,output_dir);
	}
	
	public Properties getProperties() {
		return properties;
	}

	public void loadProperties() {

		if(properties.getProperty("output_dir") != null)
		{
			output_dir = properties.getProperty("output_dir");
			mapargs.put("output_dir",output_dir);
		}

		
		if(properties.getProperty("type") != null) {
			if(properties.getProperty("type").equalsIgnoreCase("smartcard"))
				type = TYPE_SMARTCARD;
			else if(properties.getProperty("type").equalsIgnoreCase("socket"))
				type = TYPE_SOCKET;
			else if(properties.getProperty("type").equalsIgnoreCase("tls"))
				type = TYPE_TLS;

			mapargs.put("type",String.valueOf(type));
		}
		
		if(properties.getProperty("learning_algorithm").equalsIgnoreCase("lstar") || properties.getProperty("learning_algorithm").equalsIgnoreCase("dhc") || properties.getProperty("learning_algorithm").equalsIgnoreCase("kv") || properties.getProperty("learning_algorithm").equalsIgnoreCase("ttt") || properties.getProperty("learning_algorithm").equalsIgnoreCase("mp") || properties.getProperty("learning_algorithm").equalsIgnoreCase("rs")){
			learning_algorithm = properties.getProperty("learning_algorithm").toLowerCase();
			mapargs.put("learning_algorithm",learning_algorithm);
		}

		
		if(properties.getProperty("eqtest") != null && (properties.getProperty("eqtest").equalsIgnoreCase("wmethod") || properties.getProperty("eqtest").equalsIgnoreCase("modifiedwmethod") || properties.getProperty("eqtest").equalsIgnoreCase("wpmethod") || properties.getProperty("eqtest").equalsIgnoreCase("randomwords"))){
			eqtest = properties.getProperty("eqtest").toLowerCase();
			mapargs.put("eqtest",eqtest);
		}

		
		if(properties.getProperty("max_depth") != null){
			max_depth = Integer.parseInt(properties.getProperty("max_depth"));
			mapargs.put("max_depth",String.valueOf(max_depth));
		}

		
		if(properties.getProperty("min_length") != null){
			min_length = Integer.parseInt(properties.getProperty("min_length"));
			mapargs.put("min_length",String.valueOf(min_length));
		}

		
		if(properties.getProperty("max_length") != null){
			max_length = Integer.parseInt(properties.getProperty("max_length"));
			mapargs.put("max_length",String.valueOf(max_length));
		}

		
		if(properties.getProperty("nr_queries") != null){
			nr_queries = Integer.parseInt(properties.getProperty("nr_queries"));
			mapargs.put("nr_queries",String.valueOf(nr_queries));
		}

		
		if(properties.getProperty("seed") != null){
			seed = Integer.parseInt(properties.getProperty("seed"));
			mapargs.put("seed",String.valueOf(seed));
		}

		if(properties.getProperty("keystore_filename") !=null){
			mapargs.put("keystore_filename",properties.getProperty("keystore_filename"));
		}
		if(properties.getProperty("keystore_password") !=null){
			mapargs.put("keystore_password",properties.getProperty("keystore_password"));
		}
		if(properties.getProperty("alphabet") != null){
			mapargs.put("alphabet",properties.getProperty("alphabet"));
		}
		if(properties.getProperty("target") != null){
			mapargs.put("target",properties.getProperty("target"));
		}
		if(properties.getProperty("cmd") != null){
			mapargs.put("cmd",properties.getProperty("cmd"));
		}
		if(properties.getProperty("timeout") != null){
			mapargs.put("timeout",properties.getProperty("timeout"));
		}
		if(properties.getProperty("host") != null){
			mapargs.put("host",properties.getProperty("host"));
		}
		if(properties.getProperty("port") != null){
			mapargs.put("port",properties.getProperty("port"));
		}
	}
	private static void copyArgsToOutDir(Map<String,String> args, String outDir)
			throws IOException {
		FileOutputStream fw = new FileOutputStream(new File(outDir, ARGS_FILE));
		PrintStream ps = new PrintStream(fw);
		Set<String> keyset = args.keySet();
		Iterator<String> it = keyset.iterator();
		while (it.hasNext()){
			String key = it.next();
			String value = args.get(key);
			ps.println(key+" = "+value);
		}

		ps.close();
		fw.close();
	}
}
