package http;



import android.util.Log;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class HttpDelegate {
    private HttpClient client = new DefaultHttpClient();
    String bodyContent;
    public String doPost(String url,String body) throws URISyntaxException, IOException {
        HttpPost req = new HttpPost(url);
        req.setHeader("Content-Type","application/json;charset=UTF-8");
        byte[] bytes = body.getBytes("UTF-8");
        String reqString = new String(bytes,"UTF-8");
        HttpEntity bodyEntity = new StringEntity(reqString,"UTF-8");
        String charS = ContentType.getOrDefault(bodyEntity).getCharset().name();
        Log.d("charset",charS);
        Log.d("reqString",reqString);
        req.setEntity(bodyEntity);

        HttpResponse resp = client.execute(req);
        System.out.println(Integer.toString(resp.getStatusLine().getStatusCode()));
        int code = resp.getStatusLine().getStatusCode();
        if(code==200 || code==201) {
            HttpEntity entity = resp.getEntity();
            String charSet = ContentType.getOrDefault(entity).getCharset().name();
            bodyContent = StringEscapeUtils.unescapeJava(EntityUtils.toString(entity, charSet));
            Log.d("charset_resp",charSet);
            return bodyContent;
        }
        else return null;


    }


}
