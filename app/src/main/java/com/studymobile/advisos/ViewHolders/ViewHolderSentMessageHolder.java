package com.studymobile.advisos.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.studymobile.advisos.Models.ChatMessage;
import com.studymobile.advisos.R;

public class ViewHolderSentMessageHolder extends RecyclerView.ViewHolder {

    TextView messageText, timeText;

    public ViewHolderSentMessageHolder(View itemView) {
        super(itemView);

        messageText = (TextView) itemView.findViewById(R.id.text_message_body_sent);
        timeText = (TextView) itemView.findViewById(R.id.text_message_time_sent);
    }

    public void setMessageText(String i_messageText) {
        this.messageText.setText(i_messageText);

    }

    public void setTimeText(String i_timeText) {
        this.timeText.setText(i_timeText);
    }
}
