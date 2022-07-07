package controllers;

import com.google.inject.Inject;

import ninja.Result;
import ninja.Results;
import ninja.utils.NinjaProperties;

public class CorsHeadersController {
	
	@Inject
	private NinjaProperties ninjaProperties;
	
	public Result routeForOptions() {
		return addHeaders(Results.ok().json().render("key", "value"));
	}

	public Result addHeaders(Result result) {
		final String domainUrl = ninjaProperties.getOrDie("airwaybooking.url.landingpage");
		final String finalDomainUrl = domainUrl.substring(0, domainUrl.length() - 1);
		return result.addHeader("Access-Control-Allow-Origin", finalDomainUrl)
				.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
				.addHeader("Access-Control-Request-Method", "GET, POST, DELETE, PUT, OPTIONS")
				.addHeader("Access-Control-Allow-Headers", "apikey, hrEmail, Content-Type, Content-Range, Content-Disposition, Content-Description, Authorization, X-PING, Set-Cookie")
				.addHeader("Access-Control-Allow-Credentials", "true")
		        .addHeader("Access-Control-Request-Headers", "Cookie")
		        .addHeader("Access-Control-Max-Age", "85000");
		
	}
}
