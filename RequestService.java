@Service
@RequiredArgsConstructor
public class RequestService  {

    private final ObjectMapper objectMapper;
    private final String BASE_PATH = "/history";
    private final String LOGIN_PATH = BASE_PATH + "/login";
    private final String DETAIL_PATH = LOGIN_PATH + "/detail";

    private final RestExchangeUtil restExchangeUtil;

    public List<Login> retrieveHistory(HttpServletResponse httpServletResponse, Map<String, Object> body, Boolean masking) throws JsonProcessingException {

        ResponseEntity<JsonNode> response = restExchangeUtil.storePostResponseEntity(body,LOGIN_PATH);
        List<Login> histories = objectMapper.readValue(response.getBody().get("response").toString(), new TypeReference<List<Login>>() {});

        return histories;
  }
}
