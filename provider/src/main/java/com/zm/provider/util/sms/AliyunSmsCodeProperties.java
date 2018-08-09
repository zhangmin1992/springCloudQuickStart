package com.zm.provider.util.sms;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="sms")
public class AliyunSmsCodeProperties {

		private String accessKeyId;
	 
	    private String accessKeySecret;
	
	    private String signName;
	 
	    private String codeTemplate;
	 
	    private String domain = "dysmsapi.aliyuncs.com";
	 
	    private String product = "Dysmsapi";

		public String getAccessKeyId() {
			return accessKeyId;
		}

		public void setAccessKeyId(String accessKeyId) {
			this.accessKeyId = accessKeyId;
		}

		public String getAccessKeySecret() {
			return accessKeySecret;
		}

		public void setAccessKeySecret(String accessKeySecret) {
			this.accessKeySecret = accessKeySecret;
		}

		public String getSignName() {
			return signName;
		}

		public void setSignName(String signName) {
			this.signName = signName;
		}

		public String getCodeTemplate() {
			return codeTemplate;
		}

		public void setCodeTemplate(String codeTemplate) {
			this.codeTemplate = codeTemplate;
		}

		public String getDomain() {
			return domain;
		}

		public void setDomain(String domain) {
			this.domain = domain;
		}

		public String getProduct() {
			return product;
		}

		public void setProduct(String product) {
			this.product = product;
		}
}
