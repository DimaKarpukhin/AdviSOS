package com.studymobile.advisos.ViewHolders;



import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import com.studymobile.advisos.Models.ChatMessage;
import com.studymobile.advisos.R;

public class ViewHolderRecievedMessageHolder extends RecyclerView.ViewHolder {

    TextView messageText, timeText, nameText;
    CircleImageView profileImage;

    public ViewHolderRecievedMessageHolder(View itemView) {
        super(itemView);

        messageText = (TextView) itemView.findViewById(R.id.text_message_body_recived);
        timeText = (TextView) itemView.findViewById(R.id.text_message_time_recived);
        nameText = (TextView) itemView.findViewById(R.id.text_message_name_recived);
        profileImage = itemView.findViewById(R.id.image_message_profile_picture);
    }

    public void setMessageText(String i_messageText) {
        messageText.setText(i_messageText);
    }
    public void setTimeText(String i_timeText)
    {
        timeText.setText(i_timeText);
    }

    public void setNameText(String i_nameText) {
        this.nameText.setText(i_nameText);
    }

    public CircleImageView getProfileImage() {
        return profileImage;
    }
}

