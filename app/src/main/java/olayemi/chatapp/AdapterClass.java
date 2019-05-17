package olayemi.chatapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import java.util.List;

public class AdapterClass extends BaseAdapter {

    private List<Messages> listItems;
    private Context context;
    private LayoutInflater inflater ;



    public AdapterClass(List<Messages> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int i) {
        return listItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.list_item, viewGroup, false);
        }
        Messages messages = listItems.get(i);

        TextView txtDisplayName, txtMessage, txtDateTime;
        CardView cardView;


        txtMessage = (TextView)view.findViewById(R.id.message);
        txtDateTime = (TextView)view.findViewById(R.id.dateTime);
        txtDisplayName = (TextView)view.findViewById(R.id.displayName);
        cardView = (CardView)view.findViewById(R.id.cv);


        txtDateTime.setText(messages.getDateTime());
        txtDisplayName.setText(messages.getDisplayName());
        txtMessage.setText(messages.getMessage());
        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                MainActivity.deleteMessage(i);
                return true;
            }
        });



        return view;
    }
}