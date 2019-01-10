package cz.rojik.backend.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.rojik.backend.auth.user.Login;
import cz.rojik.backend.dto.user.UserDTO;
import cz.rojik.backend.exception.InvalidBearerTokenException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public class TokenHandler {
	private static final String HMAC_ALGO = "HmacSHA256";
	private static final String SEPARATOR = ".";
	private static final String SEPARATOR_SPLITTER = "\\.";
	private static final String BEARER_SEPARATOR = " ";

	private final Mac hmac;

	/**
	 * Constructor which create handler for generate token
	 * @param secretKey secret key using for generate token
	 */
	public TokenHandler(byte[] secretKey) {
		try {
			hmac = Mac.getInstance(HMAC_ALGO);
			hmac.init(new SecretKeySpec(secretKey, HMAC_ALGO));
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			throw new IllegalStateException("failed to initialize HMAC: " + e.getMessage(), e);
		}
	}

	/**
	 * Parse user from giving user's token
	 * @param token user's token
	 * @return parsed user from token
	 */
	public UserDTO parseUserFromToken(String token) {
		final String[] parts = token.split(SEPARATOR_SPLITTER);
		if (parts.length == 2 && parts[0].length() > 0 && parts[1].length() > 0) {
			try {
				String tokenPart = parts[0].split(BEARER_SEPARATOR)[1];
				final byte[] userBytes = fromBase64(tokenPart);
				final byte[] hash = fromBase64(parts[1]);

				boolean validHash = Arrays.equals(createHmac(userBytes), hash);
				if (validHash) {
					final UserDTO user = fromJSON(userBytes);
					if (new Date().getTime() < user.getExpires()) {
						return user;
					}
					else {
						throw new InvalidBearerTokenException("Invalid bearer token - token is expired: " + token);
					}
				}
			} catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
                throw new InvalidBearerTokenException("Invalid bearer token: " + token);
			}
		}
		return null;
	}

	/**
	 * Create Bearer token for user
	 * @param user user object to encode to token
	 * @return user's token
	 */
	public String createTokenForUser(UserDTO user) {
		byte[] userBytes = toJSON(new Login(user));
		byte[] hash = createHmac(userBytes);
		final StringBuilder sb = new StringBuilder(170);
		sb.append(toBase64(userBytes));
		sb.append(SEPARATOR);
		sb.append(toBase64(hash));
		return sb.toString();
	}

	/**
	 * Decode user from JSON
	 * @param userBytes array of user's information
	 * @return user object
	 */
	private UserDTO fromJSON(final byte[] userBytes) {
		try {
			return new UserDTO(new ObjectMapper().readValue(new ByteArrayInputStream(userBytes), Login.class));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Generate array of byte from user login
	 * @param login user login
	 * @return array of byte contains user's information
	 */
	private byte[] toJSON(Login login) {
		try {
			return new ObjectMapper().writeValueAsBytes(login);
		} catch (JsonProcessingException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Encode content to Base64
	 * @param content content to encode
	 * @return encode string
	 */
	private String toBase64(byte[] content) {
		return DatatypeConverter.printBase64Binary(content);
	}

	/**
	 * Decode content from Base64
	 * @param content Base64 content
	 * @return array of byte contains user information
	 */
	private byte[] fromBase64(String content) {
		return DatatypeConverter.parseBase64Binary(content);
	}

	/**
	 * Generate unique hash. Synchronized to guard internal hmac object
	 * @param content content to hash
	 * @return generated hash
	 */
	private synchronized byte[] createHmac(byte[] content) {
		return hmac.doFinal(content);
	}

}
