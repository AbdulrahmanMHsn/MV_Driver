//package com.seamlabs.mvpdriver.Common.service;
//
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.RemoteViews;
//
//import androidx.annotation.Nullable;
//import androidx.core.app.NotificationCompat;
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//import androidx.navigation.NavDeepLinkBuilder;
//
//import com.facebook.common.executors.CallerThreadExecutor;
//import com.facebook.common.references.CloseableReference;
//import com.facebook.datasource.DataSource;
//import com.facebook.drawee.backends.pipeline.Fresco;
//import com.facebook.imagepipeline.core.ImagePipeline;
//import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
//import com.facebook.imagepipeline.image.CloseableImage;
//import com.facebook.imagepipeline.request.ImageRequest;
//import com.facebook.imagepipeline.request.ImageRequestBuilder;
//import com.google.firebase.messaging.RemoteMessage;
//import com.pusher.pushnotifications.fcm.MessagingService;
//import com.seamlabs.BlueRide.MainActivity;
//import com.seamlabs.BlueRide.R;
//import com.seamlabs.BlueRide.Starter.view.StarterActivity;
//import com.seamlabs.BlueRide.Utils.UserPreference;
//
//import static com.seamlabs.BlueRide.Utils.Constant.BASE_IMAGE_URL;
//
//public class NotificationsMessagingService extends MessagingService {
//
//    private String title;
//    private String body;
//    private String id;
//    private String schoolImg;
//    private String route_id;
//    private String school_id;
//    private String type;
//    private String student_id;
//    private int destinationFragment = 0;
//    private int graph = 0;
//    private Class className;
//    private Bundle bundle=new Bundle();
//
//
//    public static MutableLiveData<String> busLiveData = new MutableLiveData<>(null);
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//
//        title = remoteMessage.getData().get("title");
//        body = remoteMessage.getData().get("body");
//        id = remoteMessage.getData().get("message_id");
//        schoolImg = remoteMessage.getData().get("school_img");
//        route_id = remoteMessage.getData().get("route_id");
//        school_id = remoteMessage.getData().get("school_id");
//        student_id=remoteMessage.getData().get("student_id");
//        type = remoteMessage.getData().get("type");
//        if (type!=null){
//            switch (type) {
//                case "in_app_notification":
//                    bundle.putInt("notification_id", Integer.valueOf(id));
//                    destinationFragment = R.id.notificationDetailsFragment;
//                    getBitmap(schoolImg);
//                    break;
//                case "parent_route_notification":
//                    bundle.putInt("school_id", Integer.valueOf(school_id));
//                    bundle.putString("route_id", route_id);
////                    busLiveData.postValue("schoolId "+school_id);
////                    busLiveData.postValue("routeId "+route_id);
//                    RxBus.getInstance().publish("schoolId "+school_id);
//                    RxBus.getInstance().publish("routeId "+route_id);
//                    UserPreference.setSchoolIdForBuses(this, Integer.valueOf(school_id));
//                    destinationFragment = R.id.busesMainFragment;
//                    getBitmap(schoolImg);
//                    break;
//
//
//                case "bus_module":
//                    RxBus.getInstance().publish("refresh");
//                    destinationFragment = R.id.newBusMainFragment;
//                    getBitmap(schoolImg);
//                    break;
//
//                case "pending_request_verify":
//                    destinationFragment = R.id.home_main_fragment;
//                    getBitmap(schoolImg);
//                    break;
//
//                case "student_absent_notification":
//                    destinationFragment = R.id.attendanceReport;
//                    bundle.putInt("student_id", Integer.valueOf(student_id));
//                    getBitmap(schoolImg);
//                    break;
//
//                case "delegation_request_notification":
//                    destinationFragment = R.id.splashFragment;
//                    getBitmap(schoolImg);
//                    break;
//                default:
//                    destinationFragment = R.id.splashFragment;
//                    getBitmap(schoolImg);
//            }
//        }
//
//
//
//    }
//
//    private void getBitmap(String schoolImg) {
//        final Bitmap[] bmp = new Bitmap[1];
//
//        if (!Fresco.hasBeenInitialized()) {
//            Fresco.initialize(getApplicationContext());
//        }
//
//        ImageRequest imageRequest = ImageRequestBuilder
//                .newBuilderWithSource(Uri.parse(BASE_IMAGE_URL + schoolImg))
//                .build();
//        ImagePipeline imagePipeline = Fresco.getImagePipelineFactory().getImagePipeline();
//        final DataSource<CloseableReference<CloseableImage>>
//                dataSource = imagePipeline.fetchDecodedImage(imageRequest, this);
//        dataSource.subscribe(new BaseBitmapDataSubscriber() {
//            @Override
//            public void onNewResultImpl(@Nullable Bitmap bitmap) {
//                if (dataSource.isFinished() && bitmap != null) {
//                    Log.d("Bitmap", "has come");
//                    bmp[0] = Bitmap.createBitmap(bitmap);
//                    showNotification(title, body, id, bmp[0]);
//                    dataSource.close();
//                } else {
//                    Log.d("Bitmap", "has not come");
//                    showNotification(title, body, id, null);
//                }
//
//            }
//
//            @Override
//            public void onFailureImpl(DataSource dataSource) {
//                if (dataSource != null) {
//                    dataSource.close();
//                    showNotification(title, body, id, null);
//
//                }
//            }
//        }, CallerThreadExecutor.getInstance());
//
//
//    }
//
//    private void showNotification(String title, String body, String id, Bitmap schoolImg) {
//
//
//        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom_notification_layout);
//        if (schoolImg != null) {
//            contentView.setImageViewBitmap(R.id.school_image, schoolImg);
//        } else {
//            contentView.setImageViewResource(R.id.school_image, R.drawable.new_icon);
//        }
//
//        contentView.setTextViewText(R.id.title_school_name, title);
//        contentView.setTextViewText(R.id.description, body);
//
//        String channelId = "BlueRide";
//        String channelName = "BlueRide";
//
//        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel nChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
//            manager.createNotificationChannel(nChannel);
//        }
//        PendingIntent pendingIntent;
//
//        if (destinationFragment==R.id.splashFragment){
//             pendingIntent = new NavDeepLinkBuilder(this)
//                    .setComponentName(StarterActivity.class)
//                    .setArguments(bundle)
//                    .setGraph(R.navigation.starter_nav_graph)
//                    .setDestination(destinationFragment)
//                    .createPendingIntent();
//        }
//        else {
//             pendingIntent = new NavDeepLinkBuilder(this)
//                    .setComponentName(MainActivity.class)
//                    .setArguments(bundle)
//                    .setGraph(R.navigation.main_nav_graph)
//                    .setDestination(destinationFragment)
//                    .createPendingIntent();
//        }
//
//
//
//
//
//        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
//                    .setSound(alarmSound)
//                    .setSmallIcon(R.drawable.ic_notification_icon)
//                    .setContentTitle(title)
//                    .setContentText(body)
//                    .setLargeIcon(schoolImg)
//                    .setContentIntent(pendingIntent)
//                    .setAutoCancel(true)
//                    .setColor(Color.parseColor("#1A5FEA"))
//                    .setShowWhen(true);
//            manager.notify((int) System.currentTimeMillis(), builder.build());
//
//
//    }
//}
//
//
