package barqsoft.footballscores.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.widget.RemoteViews;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;

public class UpdateWidgetService extends IntentService {
    private myFetchService fetchService = new myFetchService();
    //private SimpleDateFormat systemFormat = new SimpleDateFormat("yyyy-dd-MM");
    //private SimpleDateFormat userFormat = new SimpleDateFormat("dd MMM");
    private final String GOALS_UNDEFINED = "-1";

    public UpdateWidgetService(){
        super("UpdateWidgetService");
    }

    public UpdateWidgetService(String name) {
        super(name);
    }

    @Override
    public void onHandleIntent(Intent intent) {
        fetchService.update();
        Cursor data = getScores();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
                .getApplicationContext());

        int[] allWidgetIds = intent
                .getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        for (int widgetId : allWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(this.getApplicationContext().getPackageName(), R.layout.scores_widget);

           if(data.getCount() > 0){
               data.moveToFirst();
               String date = data.getString(data.getColumnIndexOrThrow(DatabaseContract.scores_table.DATE_COL));
               String time = data.getString(data.getColumnIndexOrThrow(DatabaseContract.scores_table.TIME_COL));
               String home = data.getString(data.getColumnIndexOrThrow(DatabaseContract.scores_table.HOME_COL));
               String away = data.getString(data.getColumnIndexOrThrow(DatabaseContract.scores_table.AWAY_COL));
               String homeGoals = data.getString(data.getColumnIndexOrThrow(DatabaseContract.scores_table.HOME_GOALS_COL));
               String awayGoals = data.getString(data.getColumnIndexOrThrow(DatabaseContract.scores_table.AWAY_GOALS_COL));
               //String matchDay = data.getString(data.getColumnIndexOrThrow(DatabaseContract.scores_table.MATCH_DAY));

            /*   if(date != null){
                   try{
                       date = userFormat.format(systemFormat.parse(date));
                   } catch(Exception e){
                       //TODO: proper handling
                   }
               }*/

               if (GOALS_UNDEFINED.equals(homeGoals) || GOALS_UNDEFINED.equals(awayGoals)) {
                   homeGoals = awayGoals = "0";
               }

               //remoteViews.setTextViewText(R.id.date, date);
               remoteViews.setTextViewText(R.id.time, time);
               remoteViews.setTextViewText(R.id.home, home);
               remoteViews.setTextViewText(R.id.away, away);
               remoteViews.setTextViewText(R.id.home_goals, homeGoals);
               remoteViews.setTextViewText(R.id.away_goals, awayGoals);
               //remoteViews.setTextViewText(R.id.match_day, matchDay);
               remoteViews.setImageViewResource(R.id.home_logo, Utilies.getTeamCrestByTeamName(home));
               remoteViews.setImageViewResource(R.id.away_logo, Utilies.getTeamCrestByTeamName(away));

           }
            // Register an onClickListener
            Intent clickIntent = new Intent(this.getApplicationContext(),
                    MainActivity.class);

            clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
                    allWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, clickIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.scores_widget, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Cursor getScores(){

        Cursor data = getContentResolver().query(DatabaseContract.BASE_CONTENT_URI,
                null,
                null,
                null,
                DatabaseContract.scores_table.DATE_COL + " DESC limit 1" );

        return data;

    }
}