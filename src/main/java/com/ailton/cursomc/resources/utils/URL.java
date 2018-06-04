package com.ailton.cursomc.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class URL {
	
	public static String decodeParam(String param) {
		try {
			return URLDecoder.decode(param, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	public static List<Integer> decodeIntList(String list) {
		String[] vet = list.split(",");
		List<Integer> intList = new ArrayList<>();
		
		for(int i=0; i < vet.length; i++) {
			intList.add(Integer.parseInt(vet[i]));
		}
		return intList;
		//mesmo resultado nesta única linha
		//return Arrays.asList(list.split(",")).stream().map(x -> Integer.parseInt(x)).collect(Collectors.toList());
	}
}
