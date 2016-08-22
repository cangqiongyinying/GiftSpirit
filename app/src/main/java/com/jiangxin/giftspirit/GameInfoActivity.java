package com.jiangxin.giftspirit;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jiangxin.giftspirit.JSONresource.GameInfoMessage;
import com.jiangxin.httplibrary.HttpCallback;
import com.jiangxin.httplibrary.HttpUtils;
import com.jiangxin.imagelibrary.ImageLoader;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by my on 2016/8/17.
 */
public class GameInfoActivity extends BaseActivity implements HttpCallback{
    private static final String mGiftUrlHead = "http://www.1688wan.com";
    private static final String mGiftJsonContextUrl = "/majax.action?method=getAppInfo";
    private String gameId;
    private Map<String,Object> map=new HashMap<>();
    private ImageView gameImage;
    private TextView gameName;
    private TextView gameType;
    private TextView gameSize;
    private TextView gameMessage;
    private RecyclerView gameRecyclerView;
    private Button gameDownloadBtn;
    private GameInfoMessage gameInfoMessage;
    private MyRecyclerAdapter mRecyclerAdapter;
    private Notification.Builder builder;
    private NotificationManager notificationManager;
    private String downloadName;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
                    intall();
            }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);
        getGiftId();
        loadHttps("id",gameId);
        initView();
    }

    private void initView() {
        gameImage= (ImageView) findViewById(R.id.game_info_image);
        gameName= (TextView) findViewById(R.id.game_info_gname);
        gameType= (TextView) findViewById(R.id.game_info_gtype);
        gameSize= (TextView) findViewById(R.id.game_info_size);
        gameMessage= (TextView) findViewById(R.id.game_info_game_message);
        gameDownloadBtn= (Button) findViewById(R.id.game_info_download_btn);
        gameRecyclerView= (RecyclerView) findViewById(R.id.game_info_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        gameRecyclerView.setLayoutManager(linearLayoutManager);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private void loadHttps(String key,String value){
        map.put(key,value);
        HttpUtils.loadPost(mGiftUrlHead+mGiftJsonContextUrl,map).callback(this,1);
        map.clear();
    }
    private void getGiftId(){
        Intent intent=this.getIntent();
        gameId=intent.getStringExtra("id");
    }

    @Override
    public void success(String result, int requestCode) {
        Gson gson=new Gson();
        gameInfoMessage=gson.fromJson(result,GameInfoMessage.class);
        initBar(gameInfoMessage.getApp().getName());
        ImageLoader.init(this).load(mGiftUrlHead+gameInfoMessage.getApp().getLogo(),gameImage);
        gameName.setText(gameInfoMessage.getApp().getName());
        String gType="类型："+gameInfoMessage.getApp().getTypename();
        gameType.setText(gType);
        String gSize="大小：";
        if(gameInfoMessage.getApp().getAppsize()==null||gameInfoMessage.getApp().getAppsize().equals("")){
            gSize+="未知";
        }else {
            gSize+=gameInfoMessage.getApp().getAppsize();
        }
        gameSize.setText(gSize);
        gameMessage.setText(gameInfoMessage.getApp().getDescription());
        if (gameInfoMessage.getApp().getDownload_addr().equals("")){
            gameDownloadBtn.setEnabled(false);
            gameDownloadBtn.setText("暂无下载");
        }else {
            gameDownloadBtn.setEnabled(true);
            gameDownloadBtn.setText("立即下载");
            gameDownloadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Uri uri= Uri.parse(gameInfoMessage.getApp().getDownload_addr());
//                    Intent intent=new Intent(Intent.ACTION_VIEW,uri);
//                    startActivity(intent);
                    download(gameInfoMessage.getApp().getDownload_addr());
                }
            });
        }
        int start = gameInfoMessage.getApp().getDownload_addr().lastIndexOf("/");
        downloadName=gameInfoMessage.getApp().getDownload_addr().substring(start);
        mRecyclerAdapter=new MyRecyclerAdapter();
        gameRecyclerView.setAdapter(mRecyclerAdapter);
    }

    private void intall() {
        File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(externalStoragePublicDirectory + downloadName);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        startActivity(intent);

    }
    private void showNotification() {
        builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("正在下载...");
        Notification notification = builder.getNotification();
        notificationManager.notify(88002201,notification);
    }
    private void download(final String path) {

        final File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(externalStoragePublicDirectory + downloadName);
        if (file.exists()) {
            Toast.makeText(GameInfoActivity.this, "已经下载", Toast.LENGTH_SHORT).show();
            intall();
            return;
        }

        showNotification();
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream inputStream = null;
                FileOutputStream fileOutputStream = null;
                try {
                    URL url = new URL(path);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();

                    if (urlConnection.getResponseCode() == 200) {
                        fileOutputStream = new FileOutputStream(externalStoragePublicDirectory+downloadName);

                        inputStream = urlConnection.getInputStream();
                        int len = 0;
                        byte[] buffer = new byte[1024];

                        builder.setProgress(100,1,true);
                        notificationManager.notify(88002201,builder.getNotification());
                        while((len = inputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer,0,len);
                        }
                        fileOutputStream.flush();

                        builder.setProgress(100,100,false);
                        builder.setContentTitle("提示");
                        builder.setContentText("下载完成");
                        notificationManager.notify(88002201,builder.getNotification());
                        Message message = mHandler.obtainMessage();
                        message.what = 1;
                        message.sendToTarget();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    close(inputStream);
                    close(fileOutputStream);
                }

            }
        }).start();
    }
    private void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>{

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
           View view = LayoutInflater.from(GameInfoActivity.this).inflate(R.layout.recyclerview_item,parent,false);
            ViewHolder viewHolder=new ViewHolder(view);
            viewHolder.mImage= (ImageView) view.findViewById(R.id.simple_recycler_view_image);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ImageLoader.init(GameInfoActivity.this).load(mGiftUrlHead+gameInfoMessage.getImg().get(position).getAddress(),holder.mImage);
        }

        @Override
        public int getItemCount() {
            return gameInfoMessage.getImg()==null? 0:gameInfoMessage.getImg().size();
        }
        class ViewHolder extends RecyclerView.ViewHolder{
            public ViewHolder(View itemView) {
                super(itemView);
            }
            public ImageView mImage;
        }
    }
}
