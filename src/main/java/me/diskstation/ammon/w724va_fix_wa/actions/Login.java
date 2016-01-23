/*
 * Copyright (C) 2016 Marco Ammon <ammon.marco@t-online.de>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package me.diskstation.ammon.w724va_fix_wa.actions;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Marco Ammon <ammon.marco@t-online.de>
 */
public class Login {
    private final CloseableHttpClient client;
    
    public Login(CloseableHttpClient client){
        this.client = client;
    }
    public boolean login (String host, String password){
        try {
            HttpPost post = preparePost(host, password);
            CloseableHttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            boolean loggedIn = isLoggedIn(EntityUtils.toString(entity));
            System.out.println(loggedIn);
            response.close();
            return loggedIn;
        } catch (URISyntaxException | IOException ex) {
            System.out.print(ex);
            return false;
        }
    }
    
    private HttpPost preparePost(String host, String password) throws URISyntaxException{
        URI uri = new URIBuilder()
                .setScheme("http")
                .setHost(host)
                .setPath("/data/Login.json")
                .setParameter("lang", "de")
                .build();
        StringEntity entity = new StringEntity("csrf_token=nulltoken&password="+ 
                md5HashPw(password) +"&showpw=0",
                ContentType.create("application/x-www-form-urlencoded", "UTF-8"));
        System.out.println(uri);
        HttpPost post = new HttpPost(uri);
        post.setEntity(entity);
        return post;
    }
    
    private String md5HashPw(String passwordString){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] password = passwordString.getBytes(Charset.forName("UTF-8"));
            md.update(password);
            byte[] digest = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < digest.length; ++i) {
                sb.append(Integer.toHexString((digest[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            System.out.print(ex);
            return null;
        }
    }
    
    private boolean isLoggedIn(String content){
        String[] splitted = content.split("\"varid\":\"login\",");
        if (splitted.length > 0){
            String loginStatus = splitted[1];
            loginStatus = loginStatus.split("\\}")[0];
            return loginStatus.contains("success");
        } else {
            return false;
        }
    }
}
