package com.dingli.diandians.newProject.moudle.user.protocol;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by lwq on 2017/6/1
 */
public class LoginResultProtocol {
	@Expose
	@SerializedName("refresh_token")
	public String refresh_token; // 登录令牌
	@Expose
	@SerializedName("token_type")
	public String	token_type;
	@Expose
	@SerializedName("access_token")
	public String	access_token;
}
