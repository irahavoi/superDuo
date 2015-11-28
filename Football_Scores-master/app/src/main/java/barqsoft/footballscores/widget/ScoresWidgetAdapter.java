package barqsoft.footballscores.widget;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;

public class ScoresWidgetAdapter  extends CursorAdapter{
    public ScoresWidgetAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.scores_list_item, viewGroup);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView testDesc = (TextView) view.findViewById(R.id.testdesc);

        String testValue = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.scores_table.LEAGUE_COL));
        testDesc.setText(testValue);

    }
}
