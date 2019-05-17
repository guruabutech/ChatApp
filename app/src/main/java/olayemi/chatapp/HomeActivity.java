package olayemi.chatapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.jaredrummler.android.widget.AnimatedSvgView;

import static olayemi.chatapp.Utils.Utils.CANCEL;
import static olayemi.chatapp.Utils.Utils.CONNECT;
import static olayemi.chatapp.Utils.Utils.getWindowSize;

public class HomeActivity extends AppCompatActivity {

    AppCompatButton chatBtn;
    EditText edittext;
    AnimatedSvgView svgView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        chatBtn = (AppCompatButton)findViewById(R.id.chat);

        svgView = (AnimatedSvgView) findViewById(R.id.animated_svg_view);
        svgView.getLayoutParams().width = (int) (getWindowSize(this).x * 1.0);
        svgView.start();

        chat(chatBtn);

    }

    public void EditText(String message, String Title, final String PositiveButton){

         AlertDialog.Builder alert = new AlertDialog.Builder(this);

         edittext = new EditText(HomeActivity.this);
         final Editable UserID = edittext.getText();
        alert.setMessage(message);
        alert.setTitle(Title);
        alert.setView(edittext);
        alert.setPositiveButton(PositiveButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                //send the userID to the next activity to register it as a chat topic
                if (PositiveButton.equalsIgnoreCase(CONNECT)){
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                intent.putExtra("userID", UserID.toString());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                }else {
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    intent.putExtra("userID", UserID.toString());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }


            }
        });
        alert.setNegativeButton(CANCEL, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();
    }

    public void chat(View view) {
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText("Enter your UserID below", "Join Chat", CONNECT);
            }
        });
    }
}
