package com.geetest.sdk;

/**
 * GeetestWeb配置文件
 * 
 *
 */
public class GeetestConfig {

	// 填入自己的captcha_id和private_key
	private static final String captcha_id = "55b9258621d070d87921ceb89d64b1dc";
	private static final String private_key = "0f243e101ad7cf61ce3117ddfa199116";
	

	public static final String getCaptcha_id() {
		return captcha_id;
	}

	public static final String getPrivate_key() {
		return private_key;
	}

}
