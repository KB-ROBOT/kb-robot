package org.jeewx.api.third.model;
/**
 * 授权方的账户信息 实体
 * @author 刘维
 */
public class ApiGetAuthorizerRet {
	//授权方的账户信息
	private ApiGetAuthorizerRetAuthorizer authorizer_info;
	
	//授权第三方相关信息
	private ApiGetAuthorizerRetAuthortion authorization_info;
	public ApiGetAuthorizerRetAuthorizer getAuthorizer_info() {
		return authorizer_info;
	}
	public void setAuthorizer_info(ApiGetAuthorizerRetAuthorizer authorizer_info) {
		this.authorizer_info = authorizer_info;
	}
	
	public ApiGetAuthorizerRetAuthortion getAuthorization_info() {
		return authorization_info;
	}
	public void setAuthorization_info(
			ApiGetAuthorizerRetAuthortion authorization_info) {
		this.authorization_info = authorization_info;
	}
	
}
