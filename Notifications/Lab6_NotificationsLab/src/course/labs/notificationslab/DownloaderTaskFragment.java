package course.labs.notificationslab;

import android.app.Activity;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;

public class DownloaderTaskFragment extends Fragment {

    @SuppressWarnings("unused")
    private static final String TAG = "Lab-Notifications";
    private final int MY_NOTIFICATION_ID = 11151990;
    private DownloadFinishedListener mCallback;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Preserve across reconfigurations
        setRetainInstance(true);

        // TODO: Create new DownloaderTask that "downloads" data

        // TODO: Retrieve arguments from DownloaderTaskFragment
        // Prepare them for use with DownloaderTask.


        // TODO: Start the DownloaderTask


    }

    // Assign current hosting Activity to mCallback
    // Store application context for use by downloadTweets()
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mContext = activity.getApplicationContext();

        // Make sure that the hosting activity has implemented
        // the correct callback interface.
        try {
            mCallback = (DownloadFinishedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement DownloadFinishedListener");
        }
    }

    // Null out mCallback
    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    // TODO: Implement an AsyncTask subclass called DownLoaderTask.
    // This class must use the downloadTweets method (currently commented
    // out). Ultimately, it must also pass newly available data back to
    // the hosting Activity using the DownloadFinishedListener interface.

    // public class DownloaderTask extends ...

    private String[] downloadTweets(Integer resourceIDS[]) {

        final int simulatedDelay = 2000;
        String[] feeds = new String[resourceIDS.length];
        boolean downLoadCompleted = false;

        try {
            for (int idx = 0; idx < resourceIDS.length; idx++) {
                InputStream inputStream;
                BufferedReader in;
                try {
                    // Pretend downloading takes a long time
                    Thread.sleep(simulatedDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                inputStream = mContext.getResources().openRawResource(
                        resourceIDS[idx]);
                in = new BufferedReader(new InputStreamReader(inputStream));

                String readLine;
                StringBuffer buf = new StringBuffer();

                while ((readLine = in.readLine()) != null) {
                    buf.append(readLine);
                }

                feeds[idx] = buf.toString();

                if (null != in) {
                    in.close();
                }
            }

            downLoadCompleted = true;
            saveTweetsToFile(feeds);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Notify user that downloading has finished
        notify(downLoadCompleted);

        return feeds;

    }


    // TODO: Uncomment this helper method
    // Simulates downloading Twitter data from the network

    private void notify(final boolean success) {

        final Intent restartMainActivityIntent = new Intent(mContext,
                MainActivity.class);
        restartMainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Sends an ordered broadcast to determine whether MainActivity is
        // active and in the foreground. Creates a new BroadcastReceiver
        // to receive a result indicating the state of MainActivity

        // The Action for this broadcast Intent is
        // MainActivity.DATA_REFRESHED_ACTION
        // The result, MainActivity.IS_ALIVE, indicates that MainActivity is
        // active and in the foreground.

        mContext.sendOrderedBroadcast(new Intent(
                        MainActivity.DATA_REFRESHED_ACTION), null,
                new BroadcastReceiver() {

                    final String failMsg = mContext
                            .getString(R.string.download_failed_string);
                    final String successMsg = mContext
                            .getString(R.string.download_succes_string);
                    final String notificationSentMsg = mContext
                            .getString(R.string.notification_sent_string);

                    @Override
                    public void onReceive(Context context, Intent intent) {

                        // TODO: Check whether or not the MainActivity
                        // received the broadcast


                        if (getResultCode() != Activity.RESULT_OK) {
                            // TODO: If not, create a PendingIntent using
                            // the
                            // restartMainActivityIntent and set its flags
                            // to FLAG_UPDATE_CURRENT

                            //PendingIntent i = new PendingIntent(context,MainActivity.class);
                            final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, restartMainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);


                            // Uses R.layout.custom_notification for the
                            // layout of the notification View. The xml
                            // file is in res/layout/custom_notification.xml

                            RemoteViews mContentView = new RemoteViews(
                                    mContext.getPackageName(),
                                    R.layout.custom_notification);

                            // TODO: Set the notification View's text to
                            // reflect whether the download completed
                            // successfully
                            if(success){
                                mContentView.setTextViewText(R.id.text, successMsg);
                            } else {
                                mContentView.setTextViewText(R.id.text, failMsg);
                            }

                            // TODO: Use the Notification.Builder class to
                            // create the Notification. You will have to set
                            // several pieces of information. You can use
                            // android.R.drawable.stat_sys_warning
                            // for the small icon. You should also
                            // setAutoCancel(true).

                            Notification.Builder notificationBuilder = new Notification.Builder(context)
                                                                    .setSmallIcon(android.R.drawable.stat_sys_warning)
                                                                    .setAutoCancel(true)
                                                                    .setContent(mContentView);

                            // TODO: Send the notification
                            NotificationManager mNotificationManager = (NotificationManager) context
                                    .getSystemService(Context.NOTIFICATION_SERVICE);
                            mNotificationManager.notify(MY_NOTIFICATION_ID,
                                    notificationBuilder.build());

                            Toast.makeText(mContext, notificationSentMsg,
                                    Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(mContext,
                                    success ? successMsg : failMsg,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, null, 0, null, null);
    }
    // Uncomment this helper method.
    // If necessary, notifies the user that the tweet downloads are
    // complete. Sends an ordered broadcast back to the BroadcastReceiver in
    // MainActivity to determine whether the notification is necessary.

    private void saveTweetsToFile(String[] result) {
        PrintWriter writer = null;
        try {
            FileOutputStream fos = mContext.openFileOutput(
                    MainActivity.TWEET_FILENAME, Context.MODE_PRIVATE);
            writer = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(fos)));

            for (String s : result) {
                writer.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != writer) {
                writer.close();
            }
        }
    }


    // Uncomment this helper method
    // Saves the tweets to a file

    public class DownloaderTask extends AsyncTask<String, Void, String[]> {

        private static final int SIM_NETWORK_DELAY = 5000;
        private static final String TAG = "Lab-Notifications";
        private final int MY_NOTIFICATION_ID = 11151990;
        private String mFeeds[] = new String[3];
        private MainActivity mParentActivity;
        private Context mApplicationContext;

        // Change this variable to false if you do not have a stable network
        // connection
        private static final boolean HAS_NETWORK_CONNECTION = true;

        // Raw feed file IDs used if you do not have a stable connection
        public final int txtFeeds[] = { R.raw.tswift, R.raw.rblack,
                R.raw.lgaga };

        // Constructor
        public DownloaderTask(MainActivity parentActivity) {
            super();

            mParentActivity = parentActivity;
            mApplicationContext = parentActivity.getApplicationContext();

        }

        @Override
        protected String[] doInBackground(String... urlParameters) {

            return download(urlParameters);

        }

        private String[] download(String urlParameters[]) {

            boolean downloadCompleted = false;

            try {

                for (int idx = 0; idx < urlParameters.length; idx++) {

                    URL url = new URL(urlParameters[idx]);
                    try {
                        Thread.sleep(SIM_NETWORK_DELAY);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    InputStream inputStream;
                    BufferedReader in;

                    // Alternative for students without
                    // a network connection
                    if (HAS_NETWORK_CONNECTION) {
                        inputStream = url.openStream();
                        in = new BufferedReader(new InputStreamReader(inputStream));
                    } else {
                        inputStream = mApplicationContext.getResources()
                                .openRawResource(txtFeeds[idx]);
                        in = new BufferedReader(new InputStreamReader(inputStream));
                    }

                    String readLine;
                    StringBuffer buf = new StringBuffer();

                    while ((readLine = in.readLine()) != null) {
                        buf.append(readLine);
                    }

                    mFeeds[idx] = buf.toString();

                    if (null != in) {
                        in.close();
                    }
                }

                downloadCompleted = true;

            } catch (IOException e) {
                e.printStackTrace();
            }



            return mFeeds;

        }

        // Call back to the MainActivity to update the feed display

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);


        }
    }


}