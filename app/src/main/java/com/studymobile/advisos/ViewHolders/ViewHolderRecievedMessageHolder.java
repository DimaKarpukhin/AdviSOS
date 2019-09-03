package com.studymobile.advisos.ViewHolders;



import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.core.view.View;
import com.studymobile.advisos.Models.ChatMessage;
import com.studymobile.advisos.R;

public class ViewHolderRecievedMessageHolder extends RecyclerView.ViewHolder {

    TextView messageText, timeText, nameText;
    ImageView profileImage;

    public ViewHolderRecievedMessageHolder(View itemView) {
        super(itemView);

        messageText = (TextView) itemView.findViewById(R.id.text_message_body_recived);
        timeText = (TextView) itemView.findViewById(R.id.text_message_time_recived);
        nameText = (TextView) itemView.findViewById(R.id.text_message_name_recived);
        profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile_picture);
    }

    void bind(ChatMessage message) {


        // Format the stored timestamp into a readable String using method.


        // Insert the profile image from the URL into the ImageView.

    }
}
}
