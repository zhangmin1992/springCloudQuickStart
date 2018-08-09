package com.zm.provider.util.sms;

import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.mns.common.ClientException;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

@Service
public class SmsService {
	
	@Autowired
	private AliyunSmsCodeProperties properties;

	private String accessId;
	
	private String accessKey;
	
	private  String product = "Dysmsapi";
    
	private String domain = "dysmsapi.aliyuncs.com";

	//随机生成6位数的短信码
    private String makeCode(String phone) {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for(int i=0;i<6;i++){
            int next =random.nextInt(10);
            code.append(next);
        }
        return code.toString();
    }
    
    /**
     * 发信
     * @param phone
     * @param signName
     * @param templateCode
     * @param params
     * @return
     * @throws ClientException
     * @throws com.aliyuncs.exceptions.ClientException 
     */
   private SendSmsResponse send(String phone, String signName, String templateCode,JSONObject params) throws ClientException, com.aliyuncs.exceptions.ClientException {
        //初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessId,
                accessKey);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);
        //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
        request.setPhoneNumbers(phone);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(templateCode);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        request.setTemplateParam(params.toJSONString());
        request.setOutId(UUID.randomUUID().toString());
        //请求失败这里会抛ClientException异常
        return acsClient.getAcsResponse(request);
    }
	public void sendSmsCode(String phone){
        if(!phone.matches("^1[3|4|5|7|8][0-9]{9}$")){
            System.out.println("手机号码格式不正确");
            return;
        }
        String code = makeCode(phone);      //制作验证码，6位随机数字
        JSONObject smsJson=new JSONObject();
        smsJson.put("code",code);
        smsJson.put("product","Dysmsapi");
        SendSmsResponse sendSmsResponse=null;
        try {
			sendSmsResponse = send(phone,properties.getSignName(),properties.getCodeTemplate(),smsJson);
        }catch (com.aliyuncs.exceptions.ClientException e) {
        	e.printStackTrace();
            System.out.println("短信验证码发送失败");
            return;
		}
	}
	
	
}
