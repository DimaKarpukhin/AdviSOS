package com.studymobile.advisos.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shaishavgandhi.loginbuttons.Utils;
import com.studymobile.advisos.Models.ChatMessage;
import com.studymobile.advisos.R;

public class ViewHolderSentMessageHolder extends RecyclerView.ViewHolder {

    TextView messageText, timeText;

    public ViewHolderSentMessageHolder(View itemView) {
        super(itemView);

        messageText = (TextView) itemView.findViewById(R.id.text_message_body_sent);
        timeText = (TextView) itemView.findViewById(R.id.text_message_time_sent);
    }

    void bind(ChatMessage message) {


        // Format the stored timestamp into a readable String using method.

    }
}
