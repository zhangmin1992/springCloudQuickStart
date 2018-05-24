/**
 * Copyright: Copyright (c)2011
 * Company: 易宝支付(YeePay)
 */
package com.zm.provider.report;

import java.io.Serializable;
import java.util.Map;

import org.mvel2.MVEL;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateRuntime;

public class MVELUtils {
	
	 public static Object executeTemplate(CompiledTemplate compiledTemplate, Object bean, Map<String, Object> context) {
	        return TemplateRuntime.execute(compiledTemplate, bean,context);
	 }

}
