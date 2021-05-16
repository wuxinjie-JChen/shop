package com.yc.bean;

import java.io.Serializable;

import lombok.Data;
@Data
public class AdminBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4794300619219888395L;
	private int id;
	private String account;
	private String pwd;
}
