/*
 * Copyright (C) 2016 Marco Ammon <ammon.marco@t-online.de>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
public class Routing {
    private final CloseableHttpClient client;
    private final String host;
    
    public Routing(CloseableHttpClient client, String host){
        this.client = client;
        this.host = host;
    }
    
    public int getIndex(){
        try {
            HttpPost post = prepareListPost();
            CloseableHttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            int index = parseResponse(EntityUtils.toString(entity));
            response.close();
            return index;
        } catch (URISyntaxException|IOException ex){
            System.out.print(ex);
            return -1;
        }
    }
    
    public boolean deleteIndex(int index){
        try {
            HttpPost post = prepareDeletePost(index);
            CloseableHttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            int i = parseResponse(EntityUtils.toString(entity));
            response.close();
            return i == -1;
            
        } catch (URISyntaxException | IOException ex) {
            System.out.println(ex);
            return false;
        }
    }
    
    private HttpPost prepareListPost() throws URISyntaxException{
        URI uri = new URIBuilder()
                .setScheme("http")
                .setHost(host)
                .setPath("/data/routing.json")
                .build();
        StringEntity entity = new StringEntity("refresh_routing_table=refresh",
                ContentType.create("application/x-www-form-urlencoded", "UTF-8"));
        System.out.println(uri);
        HttpPost post = new HttpPost(uri);
        post.setEntity(entity);
        return post;
    }
    
    private HttpPost prepareDeletePost(int index) throws URISyntaxException{
        URI uri = new URIBuilder()
                .setScheme("http")
                .setHost(host)
                .setPath("/data/routing.json")
                .build();
        StringEntity entity = new StringEntity("id=" + index + "&deleteEntry=delete",
                ContentType.create("application/x-www-form-urlencoded", "UTF-8"));
        System.out.println(uri);
        HttpPost post = new HttpPost(uri);
        post.setEntity(entity);
        return post;
    }
    
    private int parseResponse(String content){
        JsonParser parser = new JsonParser();
        JsonElement root = parser.parse(content);
        JsonArray routes = root.getAsJsonArray();
        int i = 0;
        for (JsonElement route : routes){
            JsonObject obj = route.getAsJsonObject();
            JsonElement dest = obj.get("id_address");
            if (dest != null){
                i++;
                String destination = dest.getAsString();
                if (destination.equals("169.0.0.0")){
                    return i;
                }
            }
        }
        return -1;
    }
}
