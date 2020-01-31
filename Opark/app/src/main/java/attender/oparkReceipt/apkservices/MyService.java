package attender.oparkReceipt.apkservices;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Boolean flag = false;
        try {
            //https://opark.in/O_par_aPi/stg/apk/apkname.apk
            // https://opark.in/O_par_aPi/stg/apk/app-debug.apk

            // URL url = new URL("http://api.opark.in/apk/app-debug.apk");
            URL url = new URL("https://opark.in/O_par_aPi/stg/apk/app-debug.apk");

            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();

            String PATH = Environment.getExternalStorageDirectory() + "/Download/";
            File file = new File(PATH);
            file.mkdirs();

            File outputFile = new File(file, "app-debug.apk");

            if (outputFile.exists()) {
                outputFile.delete();
            }


            InputStream is = c.getInputStream();

            int total_size = 1431692;//size of apk

            byte[] buffer = new byte[1024];
            int len1 = 0;
            int per = 0;
            int downloaded = 0;

            FileOutputStream fos = new FileOutputStream(outputFile);

            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
                downloaded += len1;
                per = (int) (downloaded * 100 / total_size);
                //publishProgress(per);
            }
            fos.close();
            is.close();

            OpenNewVersion(PATH);

            flag = true;
        } catch (Exception e) {
            // Log.e(TAG, "Update Error: " + e.getMessage());
            flag = false;
        }
        return START_STICKY;
    }


    void OpenNewVersion(String location) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(location + "app-debug.apk")), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);




    }
}
