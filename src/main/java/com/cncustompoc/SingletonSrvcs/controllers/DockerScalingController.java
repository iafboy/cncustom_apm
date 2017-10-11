package com.cncustompoc.SingletonSrvcs.controllers;

import com.alibaba.fastjson.JSON;
import com.cncustompoc.SingletonSrvcs.domains.APITokenReply;
import com.cncustompoc.SingletonSrvcs.domains.LoginDetail;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DockerScalingController {
    static Logger logger=LoggerFactory.getLogger(DockerScalingController.class);
    static int initalnumber=1;
    @Value("${occs.apiauthurl}")
    String apiAuthUrl;
    @Value("${occs.apitoken}")
    String apiTokenUrl;
    @Value("${occs.deployments}")
    String deploymentsUrl;
    @Value("${occs.deploymentfile}")
    String deploymentFilePath;
    @Value("${occs.templatefile}")
    String templateFile;
    @Value("${occs.username}")
    String username;
    @Value("${occs.password}")
    String password;
    @Value("${occs.ip}")
    private  String serverIP;

    public RestTemplate restTemplate()
            throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        SSLContext ctx = SSLContext.getInstance("TLS");
        X509TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
            }
            public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
            }
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        DefaultHttpClient base = new DefaultHttpClient();
        ctx.init(null, new TrustManager[]{tm}, null);
        SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        ClientConnectionManager ccm = base.getConnectionManager();
        SchemeRegistry sr = ccm.getSchemeRegistry();
        sr.register(new Scheme("https", 443, ssf));
        DefaultHttpClient httpClient = new DefaultHttpClient(ccm, base.getParams());
        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        return restTemplate;
    }

    @RequestMapping(value = "/GetDeployFileTemplate/{InstanceID}", method = RequestMethod.GET,headers="Accept=application/json",produces="application/json;charset=UTF-8")
    public boolean getDeployFileTemplate(@PathVariable String InstanceID){
        String authToken=getAuthToken_();
        String apiToken=GetAPIToken_(authToken);
        String deploymentDetail=getDepolymentDetail_(apiToken,InstanceID);
        return save2File(templateFile+ File.separator+InstanceID+".template",deploymentDetail);
    }
    @RequestMapping(value = "/UpdateDeployScaling/{InstanceID}", method = RequestMethod.GET,headers="Accept=application/json",produces="application/json;charset=UTF-8")
    public boolean updateDeployScaling(@PathVariable String InstanceID){
        String authToken=getAuthToken_();
        String apiToken=GetAPIToken_(authToken);
        if(updateScalingFile_(InstanceID)) {
                return UpdateScaling_(apiToken, InstanceID);
        }
        return false;
    }

    @RequestMapping(value="/GetAuthToken",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    public String getAuthToken(){
        return this.getAuthToken_();
    }
    public String getAuthToken_() {
        logger.info("begin try to get AuthToken");
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate= null;
        try {
            restTemplate = this.restTemplate();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return null;
        }
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        Map parammap=new HashMap<String,String>();
        parammap.put("username",username);
        parammap.put("password",password);
        String jsonStr=JSON.toJSONString(parammap);
        HttpEntity<String> formEntity = new HttpEntity<String>(jsonStr, headers);
        logger.info("post request to "+apiAuthUrl);
        String result = restTemplate.postForObject(apiAuthUrl, formEntity,String.class);
        logger.info("return result:\n"+result.toString());
        LoginDetail loginDetail=JSON.parseObject(result,LoginDetail.class);
        if(loginDetail!=null){
            return loginDetail.getToken();
        }
        return null;
    }
    @RequestMapping(value = "/GetAuthToken/{authToken}", method = RequestMethod.GET,headers="Accept=application/json",produces="application/json;charset=UTF-8")
    public String GetAPIToken(@PathVariable String authToken){
        return this.GetAPIToken_(authToken);
    }
    public String GetAPIToken_(final String authToken){
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate= null;
        try {
            restTemplate = this.restTemplate();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return null;
        }
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.add("Authorization", "Session "+authToken);
        HttpEntity<?> requestEntity = new HttpEntity(headers);
        HttpEntity<String> response=restTemplate.exchange(apiTokenUrl, HttpMethod.GET,requestEntity,String.class);
        String reply = response.getBody();
        logger.info("apiAuth response:\n"+reply);
        APITokenReply apiTokenReply=JSON.parseObject(reply, APITokenReply.class);
        if(apiTokenReply!=null){
            return apiTokenReply.getToken();
        }
        return null;
    }
    @RequestMapping(value = "/GetDeploymentDetail/{ApiAuthToken}/{InstanceID}", method = RequestMethod.GET,headers="Accept=application/json",produces="application/json;charset=UTF-8")
    public String getDepolymentDetail(@PathVariable String ApiAuthToken,@PathVariable String instanceID) {
        return getDepolymentDetail_(ApiAuthToken,instanceID);
    }
    public String getDepolymentDetail_(final String ApiAuthToken,final String instanceID) {
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate= null;
        try {
            restTemplate = this.restTemplate();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return null;
        }
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Authorization", "Bearer "+ApiAuthToken);
        HttpEntity<String> requestEntity = new HttpEntity<String>( headers);
        logger.info("url: \n"+deploymentsUrl+instanceID);
        HttpEntity<String> response=restTemplate.exchange(deploymentsUrl+instanceID,HttpMethod.GET,requestEntity,String.class);
        String reply = response.getBody();
        logger.info("GetDeploymentDetail response:\n"+reply);
        return reply;
    }
    public boolean save2File(final String filename,final String reply){
        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(filename), StandardCharsets.UTF_8);
            writer.write(reply);
            writer.flush();
            writer.close();
            return true;
        }catch(Exception ex){
            logger.error(ex.toString(),ex);
        }
        return false;
    }
    @RequestMapping(value = "/UpdateScalingFile/{InstanceID}", method = RequestMethod.GET,headers="Accept=application/json",produces="application/json;charset=UTF-8")
    public boolean updateScalingFile(@PathVariable String InstanceID) {
        return updateScalingFile_(InstanceID);
    }
    public boolean updateScalingFile_(final String InstanceID){
        List<String> readLines=null;
        try {
            StringBuffer sb=new StringBuffer();
            readLines = Files.readAllLines(Paths.get(templateFile+File.separator+InstanceID+".template"));
            initalnumber++;
            String scalingstr=new Integer(initalnumber).toString();
            for(String line:readLines){
                if(line.contains("__SACLINE__")){
                    line=line.replace("__SACLINE__",scalingstr);
                }
                sb.append(line);
            }
            save2File(deploymentFilePath+File.separator+InstanceID+".json",sb.toString());
            return true;
        }catch(Exception ex){
            logger.error(ex.toString(),ex);
        }
        return false;
    }
    @RequestMapping(value = "/UpdateScaling/{ApiAuthToken}/{InstanceID}", method = RequestMethod.PUT,produces="application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    public boolean UpdateScaling(@PathVariable String ApiAuthToken,@PathVariable String InstanceID){
        return UpdateScaling_(ApiAuthToken,InstanceID);
    }
    public boolean UpdateScaling_(final String ApiAuthToken,final String InstanceID){
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate= null;
        try {
            restTemplate = this.restTemplate();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return false;
        }
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.add("Authorization", "Bearer "+ApiAuthToken);
        StringBuffer sb=new StringBuffer();
        List<String> readLines=null;
        try {
            readLines = Files.readAllLines(Paths.get(deploymentFilePath+File.separator+InstanceID+".json"));
        }catch(Exception ex){
            logger.error(ex.toString(),ex);
            return false;
        }
        if(readLines!=null) {
            for (String s :readLines) {
                sb.append(s);
            }
            logger.info("input:\n"+sb.toString());
            HttpEntity<String> requestEntity = new HttpEntity<String>(sb.toString().trim(), headers);
            HttpEntity<String> response = restTemplate.exchange(deploymentsUrl+InstanceID, HttpMethod.PUT, requestEntity, String.class);
            String reply = response.getBody();
            logger.info("update reply is: \n"+reply);
           return true;
        }
        return false;
    }
}