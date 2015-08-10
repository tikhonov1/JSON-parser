package pavel.jsonlist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.IOException;
import java.io.InputStream;


//загружает изображения из интернета и устанавливает их в ImageView в CardView в RecyclerView
public class DownloadPicture extends AsyncTask<SetPicture, Void, Bitmap> {
    ImageView imageView = null;

    @Override
    protected Bitmap doInBackground(SetPicture... setPictures) {
        this.imageView = setPictures[0].getImageView();

        Bitmap b0=downloadImageViaDefaultHttpConnection((String) this.imageView.getTag());
        if(b0!=null)
            setPictures[0].getPerson().setPictureBitmap(b0);
        return b0;
    }
    @Override
    protected void onCancelled() {
        super.onCancelled();
        Log.d("my_log","Canceled");

    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if(result==null) return;


        try {
            imageView.setImageBitmap(result);
        }catch (Exception e){
            Log.d("Exception","in_AsyncTask");
        }
    }

    private Bitmap downloadImageViaDefaultHttpConnection(String imageUrl) {
        Bitmap imageBitmap = null;
        try {
            if(imageUrl.equals("")) {
                throw new Exception();
            }
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(imageUrl);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            InputStream imgStream = httpResponse.getEntity().getContent();
            imageBitmap = BitmapFactory.decodeStream(imgStream);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return imageBitmap;
    }
}