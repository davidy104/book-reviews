package nz.co.bookreviews.config;

import javax.servlet.annotation.WebInitParam
import javax.servlet.annotation.WebServlet

import com.sun.jersey.spi.spring.container.servlet.SpringServlet

@WebServlet(urlPatterns = ["/rest/*"], initParams = [
	@WebInitParam(name = "com.sun.jersey.config.property.packages", value = "nz.co.bookreviews"),
	@WebInitParam(name = "com.sun.jersey.api.json.POJOMappingFeature", value = "true")])
class JerseySpringServlet extends SpringServlet {
}
