package co.villalabs.certs;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenResponse {
	
	@JsonProperty("access_token")
	public String accessToken;
	
	@JsonProperty("expires_in")
	public String expiresIn;
	
	@JsonProperty("refresh_expires_in")
	public String refreshExpiresIn;
	
	@JsonProperty("refresh_token")
	public String refreshToken;
	
	@JsonProperty("token_type")
	public String tokenType;
	
	@JsonProperty("not-before-policy")
	public String notBeforePolicy;
	
	@JsonProperty("session_state")
	public String sessionState;
	
	@JsonProperty("scope")
	public String scope;
	
}
