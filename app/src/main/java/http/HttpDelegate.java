package http;



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



public class HttpDelegate {
    private HttpClient client = new DefaultHttpClient();
    String bodyContent;
    public String doPost(String url,String body) throws URISyntaxException, IOException {
        HttpPost req = new HttpPost(url);
        req.setHeader("Content-Type","application/json");
        HttpEntity bodyEntity = new StringEntity(body,"UTF-8");
        req.setEntity(bodyEntity);

        HttpResponse resp = client.execute(req);
        System.out.println(Integer.toString(resp.getStatusLine().getStatusCode()));
        if(resp.getStatusLine().getStatusCode()==200) {
            HttpEntity entity = resp.getEntity();
            String charSet = ContentType.getOrDefault(entity).getCharset().name();
            bodyContent = StringEscapeUtils.unescapeJava(EntityUtils.toString(entity, charSet));
            return bodyContent;
        }
        else return null;


    }


}
