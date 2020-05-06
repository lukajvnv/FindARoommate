package rs.ac.uns.ftn.findaroommate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;


import rs.ac.uns.ftn.findaroommate.activity.HomepageActivity;
import rs.ac.uns.ftn.findaroommate.activity.LoginActivity;
import rs.ac.uns.ftn.findaroommate.activity.SignUpActivity;
import rs.ac.uns.ftn.findaroommate.activity.SignUpHomeActivity;
import rs.ac.uns.ftn.findaroommate.model.Message;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActiveAndroid.initialize(this);

        Message m = Message.builder().title("title").message("message").build();
//        Message m = new Message(1, 1, "My title", "My message");
        TextView textView = (TextView)findViewById(R.id.textView);
        textView.setText(m.getTitle() + m.getMessage());

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HomepageActivity.class);
                startActivity(intent);
            }
        });

        boolean logged = false;
        if(logged){
            Intent intent = new Intent(MainActivity.this, HomepageActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, SignUpHomeActivity.class);
            startActivity(intent);
        }

    }
}
