
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.concurrent.TimeUnit;

@Configuration
public class RestClientConfig {

    @Value("${test.request.timeout:5000}")
    int requestTimeout;

    @Bean
    public RestTemplate monitoringClient(RestTemplateBuilder restTemplateBuilder) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestWithBodyFactory();
        factory.setConnectTimeout(requestTimeout);
        factory.setConnectionRequestTimeout(requestTimeout);
        factory.setReadTimeout(requestTimeout);
        HttpClient httpClient = HttpClientBuilder.create().setConnectionTimeToLive(10000L, TimeUnit.MILLISECONDS).build();
        factory.setHttpClient(httpClient);
        return restTemplateBuilder.requestFactory(() -> factory).build();
    }

    private static final class HttpComponentsClientHttpRequestWithBodyFactory extends HttpComponentsClientHttpRequestFactory {
        @Override
        protected HttpUriRequest createHttpUriRequest(HttpMethod httpMethod, URI uri) {
            if (httpMethod == HttpMethod.GET) {
                return new HttpGetRequestWithEntity(uri);
            }
            return super.createHttpUriRequest(httpMethod, uri);
        }
    }

    private static final class HttpGetRequestWithEntity extends HttpEntityEnclosingRequestBase {
        public HttpGetRequestWithEntity(final URI uri) {
            super.setURI(uri);
        }

        @Override
        public String getMethod() {
            return HttpMethod.GET.name();
        }
    }

}
