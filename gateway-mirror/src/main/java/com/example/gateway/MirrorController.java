package com.example.gateway;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Enumeration;

@RestController
public class MirrorController {

    private static final String DELIMITER = "/";

    @RequestMapping("/**")
    public ResponseEntity<byte[]> mirrorRest(@RequestBody(required = false) String body,
                                             HttpMethod method, HttpServletRequest request, HttpServletResponse response)
            throws URISyntaxException {
        String[] requestUrlSegments = request.getRequestURI().split("/");

        URI uri = new URI("http", requestUrlSegments[1], null, null);

        uri = UriComponentsBuilder.fromUri(uri)
                .path(servicePath(requestUrlSegments))
                .query(request.getQueryString())
                .build(true).toUri();

        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.set(headerName, request.getHeader(headerName));
        }

        HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<byte[]> proxyResponse;

        try {
            proxyResponse = restTemplate.exchange(uri, method, httpEntity, byte[].class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getRawStatusCode())
                    .headers(e.getResponseHeaders())
                    .body(e.getResponseBodyAsByteArray());
        }
        return new ResponseEntity<>(proxyResponse.getBody(), proxyResponse.getHeaders(), proxyResponse.getStatusCode());
    }

    private String servicePath(String[] requestUrlSegments) {
        boolean hasPath = requestUrlSegments.length > 2;
        if (hasPath) {
            return String.join(DELIMITER, Arrays.copyOfRange(requestUrlSegments, 2, requestUrlSegments.length));
        }
        return null;
    }
}