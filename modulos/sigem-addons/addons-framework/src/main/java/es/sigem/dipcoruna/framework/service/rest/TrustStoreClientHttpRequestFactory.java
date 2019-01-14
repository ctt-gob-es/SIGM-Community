package es.sigem.dipcoruna.framework.service.rest;

import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

public class TrustStoreClientHttpRequestFactory implements  FactoryBean<ClientHttpRequestFactory> {
    private final String trustStoreFile;
    private final String trustSTorePass;
           
    public TrustStoreClientHttpRequestFactory(String trustStoreFile, String trustSTorePass) {      
        this.trustStoreFile = trustStoreFile;
        this.trustSTorePass = trustSTorePass;
    }



    @Override
    public ClientHttpRequestFactory getObject() throws Exception {               
        final Resource trustoreResource =  new ClassPathResource(trustStoreFile);                       
        KeyStore trustore = KeyStore.getInstance("JKS");
        trustore.load(trustoreResource.getInputStream(), trustSTorePass.toCharArray());
        
       
        TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustFactory.init(trustore);
                
        SSLContext sslcontext = SSLContext.getInstance("SSLv3");
        sslcontext.init(null, trustFactory.getTrustManagers(), null);
                
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new AllowAllHostnameVerifier());         
		HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        
        return new HttpComponentsClientHttpRequestFactory(httpClient); 
    }

    

    @Override
    public Class<?> getObjectType() {    
        return ClientHttpRequestFactory.class;
    }


    @Override
    public boolean isSingleton() {
        return true;
    }
    
}
