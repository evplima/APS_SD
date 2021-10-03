package com.example.APS_SD.controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;


@RestController
@RequestMapping("/SPTrans")
public class SPTransController {

    private static String token;


    @GetMapping("/autentica")
    public void autenticaToken(){
        RestTemplate template = new RestTemplate();


        UriComponents uri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("api.olhovivo.sptrans.com.br/v2.1")
                .path("/Login/Autenticar")
                .queryParam("token", "3327bd7fcef029af6813fdb7a4ed060d29315bba34d3b60126d356ba2ec444c2")
                .build();

        ResponseEntity<String> entity = template.postForEntity(uri.toUriString(), null, null);
        token = entity.getHeaders().toString().substring(entity.getHeaders().toString().indexOf("Set-Cookie:") + 12,
                entity.getHeaders().toString().indexOf("; path=/; HttpOnly;"));
    }

    @GetMapping("/buscarLinhas/{parametros}")
    public String buscarLinhas(@PathVariable("parametros") String parametros) throws Exception {
        autenticaToken();

        return doGet("http://api.olhovivo.sptrans.com.br/v2.1/Linha/Buscar?termosBusca=" + parametros);
    }

    @GetMapping("/buscarParadas/{parametros}")
    public String buscarParadas(@PathVariable("parametros") String parametros) throws Exception {
        autenticaToken();

        return doGet("http://api.olhovivo.sptrans.com.br/v2.1/Parada/Buscar?termosBusca=" + parametros);
    }

    @GetMapping("/retornaPosicao")
    public String retornaPosicao() throws Exception {
        autenticaToken();

        return doGet("http://api.olhovivo.sptrans.com.br/v2.1/Posicao");
    }


    public String doGet(final String url) throws Exception {
        RestTemplate template = new RestTemplate();
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
       // headers.add(HttpHeaders.USER_AGENT, "Mozilla/5.0");
        headers.add(HttpHeaders.COOKIE, token);
        HttpEntity<?> entity = new HttpEntity<Object>(headers);
        HttpEntity<String> response = template.exchange(url, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }


}
