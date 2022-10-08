@Slf4j
@Component
@RequiredArgsConstructor
public class RestExchangeUtil {

    private final RestTemplate restTemplate;

    @Value("${auth-token}")
    private String authToken;

    @Value("${legacy.store.local}")
    private String REQUEST_URL;

    protected URI getUri(String baseUrl, String path, Map<String, Object> queryParams) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl).path(path).encode(StandardCharsets.UTF_8);
        if (queryParams != null) {
            queryParams.forEach((key, value) -> builder.queryParam(key, value));
        }
        return builder.build().toUri();
    }

    protected <T> HttpEntity<T> getRequestEntity(T body, Map<String, String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null) {
            headers.forEach((key, value) -> httpHeaders.add(key, value));
        }

        if (httpHeaders.getContentType() == null) {
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        }

        if (httpHeaders.get("Authorization") == null) {
            httpHeaders.setBearerAuth(portalToken);
        }

        return new HttpEntity<T>(body, httpHeaders);
    }

    public <T> ResponseEntity<T> get(String baseUrl, String path, Map<String, Object> queryParams, Class<T> responseType) {
        URI uri = getUri(baseUrl, path, queryParams);
        Map<String, String> requestHeader = setStoreRequestHeader();
        return restTemplate.exchange(uri, HttpMethod.GET, getRequestEntity(null, requestHeader), responseType);
    }

    public <T> ResponseEntity<T> get(String baseUrl, String path, Map<String, Object> queryParams, ParameterizedTypeReference<T> responseType) {
        URI uri = getUri(baseUrl, path, queryParams);
        Map<String, String> requestHeader = setStoreRequestHeader();
        return restTemplate.exchange(uri, HttpMethod.GET, getRequestEntity(null, requestHeader), responseType);
    }

    public <T> ResponseEntity<T> post(String baseUrl, String path, Map<String, Object> queryParams, Object body, Class<T> responseType) {
        URI uri = getUri(baseUrl, path, queryParams);
        Map<String, String> requestHeader = setStoreRequestHeader();
        return restTemplate.exchange(uri, HttpMethod.POST, getRequestEntity(body, requestHeader), responseType);
    }
    
    public <T> ResponseEntity<T> put(String baseUrl, String path, Map<String, Object> queryParams, Object body, Class<T> responseType) {
        URI uri = getUri(baseUrl, path, queryParams);
        Map<String, String> requestHeader = setStoreRequestHeader();
        return restTemplate.exchange(uri, HttpMethod.PUT, getRequestEntity(body, requestHeader), responseType);
    }

    public <T> ResponseEntity<T> delete(String baseUrl, String path, Map<String, Object> queryParams, Object body, Class<T> responseType) {
        URI uri = getUri(baseUrl, path, queryParams);
        Map<String, String> requestHeader = setStoreRequestHeader();
        return restTemplate.exchange(uri, HttpMethod.DELETE, getRequestEntity(body, requestHeader), responseType);
    }

    public <T> ResponseEntity<T> delete(String baseUrl, String path, Map<String, Object> queryParams, Class<T> responseType) {
        URI uri = getUri(baseUrl, path, queryParams);
        Map<String, String> requestHeader = setStoreRequestHeader();
        return restTemplate.exchange(uri, HttpMethod.DELETE, getRequestEntity(null, requestHeader), responseType);
    }


    public ResponseEntity<JsonNode> storePostResponseEntity(Map<String, Object> body, String path) {
        ResponseEntity<JsonNode> response = this.post(STORE_URL, path, null, body, JsonNode.class);
        return response;
    }


    public ResponseEntity<JsonNode> storeGetResponseEntity(Map<String, Object> params, String path) {
        ResponseEntity<JsonNode> response = this.get(STORE_URL, path, params, JsonNode.class);
        return response;
    }

    private Map<String, String> setStoreRequestHeader() {
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Content-Type", "application/json;charset=UTF-8");
        requestHeader.put("Accept", "application/json");
        return requestHeader;
    }


}
