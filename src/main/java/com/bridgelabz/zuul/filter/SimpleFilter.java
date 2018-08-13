package com.bridgelabz.zuul.filter;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class SimpleFilter extends ZuulFilter {

	@Value("${mykey}")
	String KEY;

	@Override
	public Object run() throws ZuulException {

		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		// System.out.println(request.getRequestURI());

		String token = request.getHeader("token");
		// System.out.println(token);
		Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(KEY)).parseClaimsJws(token)
				.getBody();
		ctx.addZuulRequestHeader("userId", claims.getSubject());
		return null;

	}

	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		// System.out.println(request.getRequestURI());
		if (request.getRequestURI().startsWith("/notes/")) {
			return true;
		}
		return false;
	}

	@Override
	public int filterOrder() {

		return 1;
	}

	@Override
	public String filterType() {

		return "pre";
	}

}
