package com.tedu.sp11.filter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.tedu.web.util.JsonResult;

@Component
public class AccessFilter extends ZuulFilter {

	@Override
	public boolean shouldFilter() {
		// 对指定的serviceid过滤,如果哟啊过滤所有服务器,直接返回true
		RequestContext ctx = RequestContext.getCurrentContext();
		String serviceId = (String) ctx.get(FilterConstants.SERVICE_ID_KEY);
		if (serviceId.equals("item-service")) {
			return true;
		}

		return false;
	}

	@Override
	public Object run() throws ZuulException {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest req = ctx.getRequest();
		//token使用 shiro安全框架和单点登录中 和zuul中
		String at = req.getParameter("token");
		if (at == null) {
			//此设置会阻止请求被路由到后台的微服务
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(200);	//设置响应参数代码200
			ctx.setResponseBody(JsonResult.err().code(JsonResult.NOT_LOGIN).toString());
		}
		//zuul过滤器返回的数据设计为以后扩展使用
		//目前返回值还未使用
		
		return null;
	}

	@Override
	public String filterType() {

		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		//该过滤器顺序要>5,才能得到serviceid
		return FilterConstants.PRE_DECORATION_FILTER_ORDER+1;
	}

}
