# My-SMS-App
This App lets users send and receive SMS through their phones.

This app has three screens:
<ol>
  <li>The first screen shows all the messages that the user has received. This list shows the recent messages received by a sender.</li>
  <li>The second screen opens when a user touches any of the list items in the first screen. This opens up the second screen which shows all the messages
  the user has received from the particular sender, whose message was touched on the first screen.</li>
  <li>The third screen opens when the user touches the New SMS button on the first screen. Here the user can send an SMS to a phone number.</li>
</ol>

<h2>Features</h2>
The app has following features
<ol>
  <li>Messages can be searched for words. These words will be highlighted if present in the message body or the sender name/number.</li>
  <li>A notification is shown whenever the user receives a new SMS.</li>
  <li>In the message sending screen, there is a field that show the number of characters that the user has typed(Max allowed is 160). A warning message
  is also displayed, if the user tries to send the message without entering the phone number or the message.
  </li>
</ol>

<h2>How To Setup The Project</h2>
<ol>
  <li>Clone this repository in your local machine</li>
  <li>Install and open the latest version of Android Studio(can be downloaded from <a href="http://developer.android.com/sdk/index.html">here</a>)</li>
  <li>Select File > New Project > Import Project</li>
  <li>Browse and select the folder that you cloned on your machine</li>
</ol>

<h2>Libraries Used In The Project</h2>
<ol>
  <li>com.android.support:appcompat-v7:23.3.0</li>
  <li>com.android.support:recyclerview-v7:23.3.0</li>
  <li>com.android.support:cardview-v7:23.3.0</li>
  <li>com.android.support:design:23.3.0</li>
</ol>

<h3>The debug build of the App can be found in the AppDebugBuildApk directory<h3>
