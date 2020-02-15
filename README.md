# *"AdviSOS"*

## *An instant messaging service, that allows to get real time expert advice on any topic. Implemented in Java using Firebase real-time database and other Firebase services*

<p align="center">
 <img src="https://github.com/DimaKarpukhin/AdviSOS/blob/master/screenshots/splash_screen.jpeg" width="240"/>
 <img src="https://github.com/DimaKarpukhin/AdviSOS/blob/master/screenshots/connect_with.jpeg" width="240"/>
</p>

# *How it works*
## *1. Authentication:*
***The system is integrated with quick authentication options and support appropriate verification and format checks. After the first sign up the system fill identify your user automatically, but you will still able to edit your personal information.***

### _Google and Facebook authentication:_ 
<p align="center">
 <img src="https://github.com/DimaKarpukhin/AdviSOS/blob/master/screenshots/auth_social.jpeg" width="220"/>
 <img src="https://github.com/DimaKarpukhin/AdviSOS/blob/master/screenshots/auth_google.jpeg" width="220"/>
 <img src="https://github.com/DimaKarpukhin/AdviSOS/blob/master/screenshots/aut_facebook.jpeg" width="220"/>
</p>

### _Phone number authentication with code varification:_ 
<p align="center">
 <img src="https://github.com/DimaKarpukhin/AdviSOS/blob/master/screenshots/aut_phone_num.jpeg" width="220"/>
 <img src="https://github.com/DimaKarpukhin/AdviSOS/blob/master/screenshots/code_varification_2.jpeg" width="220"/>
 <img src="https://github.com/DimaKarpukhin/AdviSOS/blob/master/screenshots/code_varification_1.jpeg" width="220"/>
</p>

### _Email and Password authentication with 'reset password' option:_
<p align="center">
  <img src="https://github.com/DimaKarpukhin/AdviSOS/blob/master/screenshots/sign_up.jpeg" width="240"/>
  <img src="https://github.com/DimaKarpukhin/AdviSOS/blob/master/screenshots/reset_password.jpeg" width="240"/>
</p>

## *2. Confirm personal details:*
***The app will autofill an appropriate form fields according to your authentication method. Just complete the missing information and confirm.***
<p align="center">
  <img src="https://github.com/DimaKarpukhin/AdviSOS/blob/master/screenshots/confirm_details.jpeg" width="240"/>
  <img src="https://github.com/DimaKarpukhin/AdviSOS/blob/master/screenshots/user_photo.jpeg" width="240"/>
</p>

## *3. Expert settings:*
***Choose/add the subjects that you are an expert on from/to the subject's list and set the time of your week availability. You can change these setting any time you use the app***
<p align="center">
  <img src="https://github.com/DimaKarpukhin/AdviSOS/blob/master/screenshots/availability.jpeg" width="240"/>
  <img src="https://github.com/DimaKarpukhin/AdviSOS/blob/master/screenshots/choose_subjects.jpeg" width="240"/>
</p
 
## -
## ***There are 4 different tabs on the main screen to provide intuitive and easy use:***
- _**"subjects"** tab - most popular subjects for asking advice_
- _**"advice me"** tab - all active chats with experts on the topic you ask_
- _**"advice others"** tab - all active chats in which you are one of the experts_
- _**"chat requests"** tab - all active chat requests as an expert_
<p align="center">
 <img src="https://github.com/DimaKarpukhin/AdviSOS/blob/master/screenshots/subjects.jpeg" width="200"/>
 <img src="https://github.com/DimaKarpukhin/AdviSOS/blob/master/screenshots/advice_me.jpeg" width="200"/>
 <img src="https://github.com/DimaKarpukhin/AdviSOS/blob/master/screenshots/avice_others.jpeg" width="200"/>
 <img src="https://github.com/DimaKarpukhin/AdviSOS/blob/master/screenshots/group_request.jpeg" width="200"/>
</p>

## *4. To get an advice from the experts:*
1. _Choose the subject from the list or create a new one in a case it's not exists_
2. _Write a topic in which you need an advice, and click **"ask"** to create a chat_
3. _The system will look for the similar topic in the previous chats and will offer to show them to you. In case nothing was found, or if you decided to create a new chat, see the next step._
4. _The system will find up to 5 experts with the highest rating in this or similar subject and will join them to the group chat with you_
5. _While the chat is active you can find it in **"advice me"** tab and when the chat is closed the system removes it from this tab to **"my closed chats"** of navigation menu on the top left side of the screen_
6. _The user who asked an advice is only the one, who can close the chat, after he got an expected answer from the experts_
7. _At the time of closing chat, each of the experts who participated in the chat should rate each other_
<p align="center">
  <img src="https://github.com/DimaKarpukhin/AdviSOS/blob/master/screenshots/topic.jpeg" width="240"/>
  <img src="https://github.com/DimaKarpukhin/AdviSOS/blob/master/screenshots/nav_menu.jpeg" width="240"/>
</p
 
 
## -
## *5. To give an advice as an experts:*
1. _Mark/add the subjects you are an expert on and set your availability in the navigation menu on the top left side of the screen_
2. _The system will send you push notification with chat request that you can find in the **"chat requests"** tab_
3. _The system will send you chat request only at the time you set as available, that why if you reject the request or accept it too late, you will lose 2% of your current rating on this subject._
4. _In the navigation menu there is **"online/offline"** option  to anable/disable chat requests in one click, without changing the availability settings_
5. _While the chat is active you can find it in **"advice others"** tab and when the chat is closed the system deletes it from this tab._
6. _There is no option to exit the chat. You will exit automaticaly when it will closed by chat creator._
7. _At the time of closing chat, you will asked to rate other experts._
