package za.co.idea.web.test;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

public class TestClass {

	public static void main(String[] args) {
		System.out.println(Base64.encodeBase64URLSafeString(DigestUtils.md5("a".getBytes())));
	}

}
