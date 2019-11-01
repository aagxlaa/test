package com.tedu.sp11;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import com.tedu.web.util.JsonResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderServiceFallback implements FallbackProvider{@Override
	public String getRoute() {
	
		return "order-servcie";
	}

	@Override
	public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
		
		return response();
	}

		private ClientHttpResponse response() {
			return new ClientHttpResponse() {
				// 下面三个方法都是协议号
				@Override
				public HttpStatus getStatusCode() throws IOException {

					return HttpStatus.OK;
				}

				@Override
				public int getRawStatusCode() throws IOException {

					return HttpStatus.OK.value();
				}

				@Override
				public String getStatusText() throws IOException {
					return HttpStatus.OK.getReasonPhrase();
				}

				@Override
				public void close() {

				}

				@Override
				public HttpHeaders getHeaders() {
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_JSON);
					return headers;
				}

				@Override
				public InputStream getBody() throws IOException {
					log.info("fallback body");
					String s = JsonResult.err().msg("后台服务器错误!!!!!!!!").toString();

					return new ByteArrayInputStream(s.getBytes("UTF-8"));
				}

			};
		}

	}